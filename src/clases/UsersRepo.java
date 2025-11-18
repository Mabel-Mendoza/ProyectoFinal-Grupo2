package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.swing.JOptionPane;

public class UsersRepo {

    private Connection cn;

    public UsersRepo() {
        clsConexion con = new clsConexion();
        this.cn = con.Sql_Conexion();
    }

    private static class Bloqueo {
        int intentosFallidos;
        LocalDateTime bloqueoHasta;
        boolean bloqueoPermanente;

        public Bloqueo(int intentosFallidos, LocalDateTime bloqueoHasta, boolean bloqueoPermanente) {
            this.intentosFallidos = intentosFallidos;
            this.bloqueoHasta = bloqueoHasta;
            this.bloqueoPermanente = bloqueoPermanente;
        }
    }

    // ==============================================
    //  Mapea el puesto al enum Role
    // ==============================================
    private static Role mapRole(String puestoDescripcion) {
        if (puestoDescripcion == null) return null;

        String p = puestoDescripcion.trim().toLowerCase();

        switch (p) {
            case "administrador": return Role.ADMIN;
            case "jefe de almacen":
            case "jefe de almacén": return Role.JEFE_ALMACEN;
            case "vendedor": return Role.VENDEDOR;
            case "auditor": return Role.AUDITOR;
            case "contador": return Role.CONTADOR;
            default: return null;
        }
    }

    // ==============================================
    //  Carga los datos de bloqueo desde la BD
    // ==============================================
    private static Bloqueo cargarBloqueo(Connection cn, String usuario) throws SQLException {

        String sql = "SELECT intentosFallidos, bloqueoHasta, bloqueoPermanente FROM credenciales WHERE usuario=?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, usuario);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    Timestamp t = rs.getTimestamp("bloqueoHasta");

                    return new Bloqueo(
                        rs.getInt("intentosFallidos"),
                        (t != null ? t.toLocalDateTime() : null),
                        rs.getBoolean("bloqueoPermanente")
                    );
                }
            }
        }

        return new Bloqueo(0, null, false);
    }

    // ==============================================
    //  Guarda los valores de bloqueo en la BD
    // ==============================================
    public void guardarBloqueo(int intentosFallidos, LocalDateTime bloqueoHasta, boolean bloqueoPermanente, String usuario)
            throws SQLException {

        String sql = "UPDATE credenciales SET intentosFallidos=?, bloqueoHasta=?, bloqueoPermanente=? WHERE usuario=?";

        try (PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, intentosFallidos);

            if (bloqueoHasta == null) {
                ps.setNull(2, java.sql.Types.TIMESTAMP);
            } else {
                ps.setTimestamp(2, Timestamp.valueOf(bloqueoHasta));
            }

            ps.setBoolean(3, bloqueoPermanente);
            ps.setString(4, usuario);

            ps.executeUpdate();
        }
    }

    // ==============================================
    //  LOGIN
    // ==============================================
    public Optional<User> login(String username, String password) {

        try {

            if (cn == null) return Optional.empty();

            Bloqueo b = cargarBloqueo(cn, username);
            LocalDateTime ahora = LocalDateTime.now();

            // =======================================
            //  BLOQUEO PERMANENTE
            // =======================================
            if (b.bloqueoPermanente && !username.equalsIgnoreCase("admin")) {
                JOptionPane.showMessageDialog(null,
                    "El sistema está bloqueado permanentemente.\n" +
                    "Solo el administrador puede desbloquearlo.",
                    "Bloqueo Permanente",
                    JOptionPane.ERROR_MESSAGE
                );
                return Optional.empty();
            }

            // =======================================
            //  BLOQUEO TEMPORAL
            // =======================================
            if (b.bloqueoHasta != null && b.bloqueoHasta.isAfter(ahora)
                    && !username.equalsIgnoreCase("admin")) {

                long segundos = java.time.Duration.between(ahora, b.bloqueoHasta).getSeconds();

                JOptionPane.showMessageDialog(null,
                    "El sistema está temporalmente bloqueado.\n" +
                    "Espere " + segundos + " segundos.",
                    "Bloqueo Temporal",
                    JOptionPane.WARNING_MESSAGE
                );
                return Optional.empty();
            }

            // =======================================
            //  CONSULTAR LOGIN
            // =======================================
            String sql = "SELECT c.usuario, e.nombreempleado, e.apellidoempleado, p.descripcion AS puesto "
                       + "FROM credenciales c "
                       + "JOIN empleados e ON e.idempleado = c.idempleado "
                       + "JOIN puesto p ON p.idpuesto = e.idpuesto "
                       + "WHERE BINARY c.usuario = ? AND c.contrasenia = ?";

            try (PreparedStatement ps = cn.prepareStatement(sql)) {

                ps.setString(1, username);
                ps.setString(2, password);

                try (ResultSet rs = ps.executeQuery()) {

                    // =======================================
                    //  LOGIN CORRECTO
                    // =======================================
                    if (rs.next()) {

                        // ADMIN DESBLOQUEA TODO
                        if (username.equalsIgnoreCase("admin")) {
                            guardarBloqueo(0, null, false, username);
                            JOptionPane.showMessageDialog(null,
                                "El sistema fue desbloqueado correctamente por el administrador.");
                        }

                        // Reset de bloqueo para cualquier usuario que loguea bien
                        guardarBloqueo(0, null, false, username);

                        String nombre = rs.getString("nombreempleado") + " "
                                      + rs.getString("apellidoempleado");

                        Role role = mapRole(rs.getString("puesto"));

                        if (role == null) {
                            JOptionPane.showMessageDialog(null, "El puesto no tiene rol asignado.");
                            return Optional.empty();
                        }

                        return Optional.of(new User(username, nombre, role));
                    }

                    // =======================================
                    //  LOGIN INCORRECTO
                    // =======================================
                    JOptionPane.showMessageDialog(null,
                            "Usuario o contraseña incorrectos.\nVuelva a intentarlo.",
                            "Credenciales inválidas",
                            JOptionPane.WARNING_MESSAGE
                    );

                    b.intentosFallidos++;

                    // Bloqueo temporal
                    if (b.intentosFallidos == 3) {
                        b.bloqueoHasta = ahora.plusMinutes(1);
                        JOptionPane.showMessageDialog(null,
                            "Ha fallado 3 veces.\nSe bloqueará durante 1 minuto.");
                    }

                    // Bloqueo permanente
                    if (b.intentosFallidos >= 4) {
                        b.bloqueoPermanente = true;
                        JOptionPane.showMessageDialog(null, "Ha superado el límite. Bloqueo permanente.");
                    }

                    guardarBloqueo(b.intentosFallidos, b.bloqueoHasta, b.bloqueoPermanente, username);
                    return Optional.empty();
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error en la base de datos.");
            return Optional.empty();
        }
    }
}
