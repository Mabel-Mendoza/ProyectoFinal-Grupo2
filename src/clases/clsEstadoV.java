/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;

/**
 *
 * @author mabel
 */
public class clsEstadoV {
    public static int obtenerEstadoVehiculo(Connection con, int idVehiculo) {
        int estado = -1;

        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT idestadovehiculo FROM vehiculos WHERE idvehiculo = ?"
            );
            ps.setInt(1, idVehiculo);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                estado = rs.getInt("idestadovehiculo");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al obtener estado: " + e.getMessage());
        }

        return estado;
    }
    
    public static boolean validarVenta(Connection con, int idVehiculo) {
        int estado = obtenerEstadoVehiculo(con, idVehiculo);

        switch (estado) {
            case 2:
                JOptionPane.showMessageDialog(null, "El vehículo ya está VENDIDO.");
                return false;
            case 4:
                JOptionPane.showMessageDialog(null, "El vehículo está ALQUILADO, no se puede vender.");
                return false;
            case 3:
                JOptionPane.showMessageDialog(null, "El vehículo está en REPARACIÓN.");
                return false;
            case 5:
                JOptionPane.showMessageDialog(null, "El vehículo está en LIMPIEZA por 24h.");
                return false;
        }

        return true; // Todo bien
    }
    
     public static boolean validarAlquiler(Connection con, int idVehiculo) {
        int estado = obtenerEstadoVehiculo(con, idVehiculo);

        switch (estado) {
            case 2:
                JOptionPane.showMessageDialog(null, "El vehículo está VENDIDO y no se puede alquilar.");
                return false;
            case 4:
                JOptionPane.showMessageDialog(null, "El vehículo ya está ALQUILADO.");
                return false;
            case 3:
                JOptionPane.showMessageDialog(null, "El vehículo está en REPARACIÓN.");
                return false;
            case 5:
                JOptionPane.showMessageDialog(null, "El vehículo está en LIMPIEZA por 24h.");
                return false;
        }

        return true;
    }

    public static boolean validarDevolucion(Connection con, int idVehiculo) {
        int estado = obtenerEstadoVehiculo(con, idVehiculo);

        if (estado != 4) { // 4 = alquilado
            JOptionPane.showMessageDialog(null, "Este vehículo no está ALQUILADO actualmente.");
            return false;
        }

        return true;
    }

    
}
