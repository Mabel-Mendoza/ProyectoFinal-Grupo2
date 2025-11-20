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
    
    private static class Bloqueo {
        int fallos;
        long bloqueoHasta;
        boolean bloqueoPermanente;

        public Bloqueo(int f, long h, boolean p) {
            this.fallos = f;
            this.bloqueoHasta = h;
            this.bloqueoPermanente = p;
        }
    }

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
    private static boolean estaBloqueado(Bloqueo b) {
        if (b.bloqueoPermanente) return true;
        long ahora = System.currentTimeMillis();
        if (b.bloqueoHasta > ahora) return true;

        b.bloqueoHasta = 0; // desbloquear si tiempo pasó
        return false;
    }
    
    private static Bloqueo cargarBloqueo(clsConexion con, String usuario) {
        try (Connection cn = con.Sql_Conexion()) {
            String sql = "SELECT intentosFallidos, bloqueoHasta, bloqueoPermanente FROM credenciales WHERE usuario=?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setString(1, usuario);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        long bloqueo = rs.getTimestamp("bloqueoHasta") != null
                            ? rs.getTimestamp("bloqueoHasta").getTime() : 0;
                        return new Bloqueo(rs.getInt("intentosFallidos"), bloqueo, rs.getBoolean("bloqueoPermanente"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Bloqueo(0,0,false);
    }

    private static void guardarBloqueo(Connection cn, String usuario, Bloqueo b) {
        try {
            String sql = "UPDATE credenciales SET intentosFallidos=?, bloqueoHasta=?, bloqueoPermanente=? WHERE usuario=?";
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setInt(1, b.fallos);
                if (b.bloqueoHasta > 0)
                    ps.setTimestamp(2, new java.sql.Timestamp(b.bloqueoHasta));
                else
                    ps.setNull(2, java.sql.Types.TIMESTAMP);
                ps.setBoolean(3, b.bloqueoPermanente);
                ps.setString(4, usuario);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public static Optional<User> login(String username, String password) {
        
        clsConexion con = new clsConexion();
        
        Bloqueo b = cargarBloqueo(con, username); 
        if (b == null) b = new Bloqueo(0,0,false); // usuario no existe aún
    
        boolean esAdmin = username.equalsIgnoreCase("admin");

        if (b.bloqueoPermanente && !esAdmin) {
            JOptionPane.showMessageDialog(null, 
                "El sistema está bloqueado permanentemente. Solo el administrador puede desbloquearlo.", 
                "Bloqueo permanente", JOptionPane.ERROR_MESSAGE);
            return Optional.empty();
        }

        if (!esAdmin && estaBloqueado(b)) {
            long tiempoRestante = (b.bloqueoHasta - System.currentTimeMillis()) / 1000;
            JOptionPane.showMessageDialog(null, 
                "El sistema está temporalmente bloqueado.\nIntente nuevamente en " + tiempoRestante + " segundos.", 
                "Bloqueo temporal", JOptionPane.WARNING_MESSAGE);
            return Optional.empty();
        }
        
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
                        b.fallos = 0;

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
                            if (b.bloqueoPermanente || b.bloqueoHasta > 0) {
                                b.bloqueoPermanente = false;
                                b.bloqueoHasta = 0;
                                b.fallos = 0;
                                guardarBloqueo(cn, username, b);
                                JOptionPane.showMessageDialog(null, 
                                    "El sistema fue desbloqueado correctamente por el administrador.", 
                                    "Administrador", JOptionPane.INFORMATION_MESSAGE);
                            }
                        }
                        return Optional.of(new User(username, nombre, role));
                    } else {

                        b.fallos++;

                        switch (b.fallos) {
                            case 3:
                                b.bloqueoHasta = System.currentTimeMillis() + (1 * 60_000); 
                                JOptionPane.showMessageDialog(null, 
                                    "Demasiados intentos. El sistema se bloqueará por 1 minuto.", // deberia ser mas tiempo pero solo pusimos 1 para probar
                                    "Bloqueo temporal", JOptionPane.WARNING_MESSAGE);
                                break;

                            case 4:
                                b.bloqueoPermanente = true;
                                JOptionPane.showMessageDialog(null, 
                                    "El sistema ha sido bloqueado permanentemente.\nSolo el administrador puede desbloquearlo.", 
                                    "Bloqueo permanente", JOptionPane.ERROR_MESSAGE);
                                break;

                            default:
                                JOptionPane.showMessageDialog(null, 
                                    "Usuario o contraseña incorrectos. Intento " + b.fallos + ".", 
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        }
                        
                        guardarBloqueo(cn, username, b);

                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            return Optional.empty();
        }
    }
}
