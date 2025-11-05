/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

/**
 *
 * @author mabel
 */
public class clsCarga {
    
    private Connection con;
     
     public clsCarga(){}

    public clsCarga(Connection con) {
        this.con = con;
    }

    public void probarConexion() {
        if (con != null) {
            System.out.println("Conexión exitosa en clsCarga.");
        } else {
            System.out.println("Conexión nula.");
        }
    }

    public void cargarDatos(JComboBox<String> combo, String tabla, String idColumna, String nombreColumna) {
    try (Connection con = new clsConexion().Sql_Conexion();
         PreparedStatement pst = con.prepareStatement("SELECT " + idColumna + ", " + nombreColumna + " FROM " + tabla);
         ResultSet rs = pst.executeQuery()) {

        combo.removeAllItems();
        combo.addItem("Seleccione...");

        while (rs.next()) {
            String item = rs.getInt(idColumna) + " - " + rs.getString(nombreColumna);
            combo.addItem(item); 
        }

    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(null, "Error al cargar combo: " + ex.getMessage());
    }
}
    
}
