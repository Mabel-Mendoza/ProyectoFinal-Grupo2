/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author mabel
 */
public class clsUtilidades {
    
    private Connection cn;
    
    public clsUtilidades(){}

 
    public clsUtilidades(Connection cn) {
        this.cn = cn;
    }

    // Mostrar

    // Consulta sin parámetros
    public void mostrarDatos(String sql, JTable tabla, String[] columnas) {
        try (Connection cn = new clsConexion().Sql_Conexion();
         Statement st = cn.createStatement();
         ResultSet rs = st.executeQuery(sql)){
            
            cargarTabla(rs, tabla, columnas);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos: " + e.getMessage());
        }
    }

    // Consulta con parámetros 
    public void mostrarDatos(String sql, JTable tabla, String[] columnas, Object[] parametros) {
        try (Connection cn = new clsConexion().Sql_Conexion();
         PreparedStatement pst = cn.prepareStatement(sql)){
            

            // Asignar parámetros
            for (int i = 0; i < parametros.length; i++) {
                pst.setObject(i + 1, parametros[i]);
            }

            ResultSet rs = pst.executeQuery();
            cargarTabla(rs, tabla, columnas);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar datos con parámetros: " + e.getMessage());
        }
    }

    // Método para llenar JTable
    private void cargarTabla(ResultSet rs, JTable tabla, String[] columnas) throws SQLException {
        DefaultTableModel modelo = new DefaultTableModel();
        tabla.setModel(modelo);

        // columnas
        for (String col : columnas) {
            modelo.addColumn(col);
        }

        // n columnas
        int numCols = columnas.length;

        // n filas
        while (rs.next()) {
            Object[] fila = new Object[numCols];
            for (int i = 0; i < numCols; i++) {
                fila[i] = rs.getObject(i + 1);
            }
            modelo.addRow(fila);
        }
    }

    // Insertar, Actualizar, Eliminar

    // Método actualizaciones
    public boolean ejecutarActualizacion(String sql, Object[] parametros) {
        try (Connection cn = new clsConexion().Sql_Conexion();
         PreparedStatement pst = cn.prepareStatement(sql)) {
           

            // Asignar parámetros
            for (int i = 0; i < parametros.length; i++) {
                pst.setObject(i + 1, parametros[i]);
            }

            int filas = pst.executeUpdate();
            return filas > 0;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en la operación: " + e.getMessage());
            return false;
        }
    }
    
}
