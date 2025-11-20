/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.sql.CallableStatement;
import java.sql.Connection;
import javax.swing.JOptionPane;

/**
 *
 * @author mabel
 */
public class clsProc {
    private Connection con;
    public clsProc() {
        try {
            clsConexion cn = new clsConexion();
            this.con = cn.Sql_Conexion(); 
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al conectar con la base de datos: " + e.getMessage());
        }
    }

    public void ejecutarSP(String nombreSP, Object... params) {
        String query = "{ call " + nombreSP + "(";
        for (int i = 0; i < params.length; i++) {
            query += (i == params.length - 1) ? "?" : "?,";
        }
        query += ") }";

        try (CallableStatement cs = con.prepareCall(query)) {
            for (int i = 0; i < params.length; i++) {
                cs.setObject(i + 1, params[i]);
            }
            cs.execute();
            System.out.println("SP ejecutado: " + nombreSP);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error ejecutando SP: " + e.getMessage());
        }
    }
}
