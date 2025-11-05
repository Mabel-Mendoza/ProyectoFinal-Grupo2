package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.swing.JOptionPane;

/**
 * Repositorio de usuarios contra la BD "Strada".
 * - Valida usuario/contraseña en tabla credenciales
 * - Obtiene el nombre del empleado y su puesto
 * - Mapea el puesto -> Role
 */
public class UsersRepo {
    
    private static int fallosSesion = 0;
    private static long bloqueoHasta = 0;
    private static boolean bloqueoPermanente = false;

    private static Role mapRole(String puestoDescripcion) {
        if (puestoDescripcion == null) return null;
        String p = puestoDescripcion.trim().toLowerCase();

        switch (p) {
            case "administrador":
                return Role.ADMIN;             
            case "jefe de almacen":
            case "jefe de almacén":
                return Role.JEFE_ALMACEN;
            case "vendedor":
                return Role.VENDEDOR;
            case "auditor":
                return Role.AUDITOR;
            case "contador":
                return Role.CONTADOR;
            default:
                return null; // puesto desconocido
        }
    }
        private static boolean estaBloqueado() {
            if (bloqueoPermanente) return true;

            long ahora = System.currentTimeMillis();
            if (bloqueoHasta > ahora) {
                return true; // sigue bloqueado
            }

            // Si ya pasó el tiempo, desbloqueamos
            bloqueoHasta = 0;
            return false;
        }
    public static Optional<User> login(String username, String password) {
 
     boolean esAdmin = username.equalsIgnoreCase("admin");

    if (bloqueoPermanente && !esAdmin) {
        JOptionPane.showMessageDialog(null, 
            "El sistema está bloqueado permanentemente. Solo el administrador puede desbloquearlo.", 
            "Bloqueo permanente", JOptionPane.ERROR_MESSAGE);
        return Optional.empty();
    }

    if (!esAdmin && estaBloqueado()) {
        long tiempoRestante = (bloqueoHasta - System.currentTimeMillis()) / 1000;
        JOptionPane.showMessageDialog(null, 
            "El sistema está temporalmente bloqueado.\nIntente nuevamente en " + tiempoRestante + " segundos.", 
            "Bloqueo temporal", JOptionPane.WARNING_MESSAGE);
        return Optional.empty();
    }

    clsConexion con = new clsConexion();
    try (Connection cn = con.Sql_Conexion()) {
        if (cn == null) return Optional.empty();

        String sql = "SELECT c.usuario, e.nombreempleado, "
                + "e.apellidoempleado, p.descripcion AS puesto "
                + "FROM credenciales c "
                + "JOIN empleados e ON e.idempleado = c.idempleado "
                + "JOIN puesto p ON p.idpuesto = e.idpuesto "
                + "WHERE BINARY c.usuario = ? AND c.contrasenia = ?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    fallosSesion = 0;

                    String nombre = rs.getString("nombreempleado") + " " + rs.getString("apellidoempleado");
                    String puesto = rs.getString("puesto");
                    Role role = mapRole(puesto);

                    if (role == null) {
                        JOptionPane.showMessageDialog(null, 
                            "El puesto no tiene rol asignado.", 
                            "Error", JOptionPane.ERROR_MESSAGE);
                        return Optional.empty();
                    }

                  
                    if (role == Role.ADMIN && username.equalsIgnoreCase("admin")) {
                        if (bloqueoPermanente || bloqueoHasta > 0) {
                            bloqueoPermanente = false;
                            bloqueoHasta = 0;
                            fallosSesion = 0;
                            JOptionPane.showMessageDialog(null, 
                                "El sistema fue desbloqueado correctamente por el administrador.", 
                                "Administrador", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }

                    return Optional.of(new User(username, nombre, role));
                } else {
                   
                    fallosSesion++;

                    switch (fallosSesion) {
                        case 3:
                            bloqueoHasta = System.currentTimeMillis() + (1 * 60_000); 
                            JOptionPane.showMessageDialog(null, 
                                "Demasiados intentos. El sistema se bloqueará por 1 minuto.", // deberia ser mas tiempo pero solo pusimos 1 para probar
                                "Bloqueo temporal", JOptionPane.WARNING_MESSAGE);
                            break;

                        case 4:
                            bloqueoPermanente = true;
                            JOptionPane.showMessageDialog(null, 
                                "El sistema ha sido bloqueado permanentemente.\nSolo el administrador puede desbloquearlo.", 
                                "Bloqueo permanente", JOptionPane.ERROR_MESSAGE);
                            break;

                        default:
                            JOptionPane.showMessageDialog(null, 
                                "Usuario o contraseña incorrectos. Intento " + fallosSesion + ".", 
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }

                    return Optional.empty();
                }
            }
        }
    } catch (SQLException ex) {
        ex.printStackTrace();
        return Optional.empty();
    }
}


    // (Opcional) Si más adelante usas contraseñas con hash (BCrypt/Argon2),
    // puedes crear otro método loginHashed(...) que compare el hash acá.
}
