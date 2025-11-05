/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author mabel
 */
public class clsConexion {
    java.sql.Connection cn;
             java.beans.Statement st;
    
    
    public java.sql.Connection Sql_Conexion(){
      try {
          try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(clsConexion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         
        cn=(Connection) DriverManager.getConnection
        ("jdbc:mysql://127.0.0.1:3306/strada?useSSL=false",
                "root","root");
      
      
            
        
        } catch (SQLException ex) {
          
            JOptionPane.showMessageDialog(null,"Error en la conexión " + ex);
        }
        return cn;
    
}
}
