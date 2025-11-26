/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package stradaproyectofinal;

import clases.User;
import clases.clsConexion;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.swing.JRViewer;
import net.sf.jasperreports.view.JasperViewer;


/**
 *
 * @author mabel
 */
public class FrmReportes extends javax.swing.JFrame {

    private User loggedUser;
    
    private Connection cn; 
    
    public FrmReportes() {
        initComponents();
        
         clsConexion con = new clsConexion();
         Connection cn = con.Sql_Conexion();
   
        
        
        setResizable(false); 
        
        this.setSize(1366, 768);
        this.setLocationRelativeTo(null);  
    }

     public FrmReportes(User user) {
        this(); // llama al constructor sin parámetros para inicializar la interfaz
        this.loggedUser = user;
        setTitle("Reportes - " + user.getDisplayName());
    }
    
     
     
    private void mostrarReporteEnPanel(String rutaReporte) {
    try {
        // Cargar reporte .jasper
        JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(rutaReporte);

        // Parámetros (si el reporte no usa parámetros, va vacío)
        Map<String, Object> parametros = new HashMap<>();

        Connection con = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/Strada",
            "root",
            "H$1994lt"
        );

        // Llenar el reporte
        JasperPrint print = JasperFillManager.fillReport(reporte, parametros, con);

        // Crear el viewer SIN FRAME externo
        JRViewer viewer = new JRViewer(print);

        // Limpia el panel antes de cargar otro reporte
        panelReportes.removeAll();
        panelReportes.setLayout(new BorderLayout());

        // Agregar viewer dentro del panel
        panelReportes.add(viewer, BorderLayout.CENTER);

        panelReportes.revalidate();
        panelReportes.repaint();

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al mostrar reporte: " + e.getMessage());
    }
}




    
    
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnAlquiler = new javax.swing.JLabel();
        lblRegresar = new javax.swing.JLabel();
        btnVentas = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnClientes = new javax.swing.JLabel();
        btnDevo = new javax.swing.JLabel();
        btnEmp = new javax.swing.JLabel();
        panelReportes = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAlquiler.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-BAlquiler.png"))); // NOI18N
        btnAlquiler.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAlquilerMouseClicked(evt);
            }
        });
        getContentPane().add(btnAlquiler, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 130, -1, 70));

        lblRegresar.setFont(new java.awt.Font("Times New Roman", 2, 26)); // NOI18N
        lblRegresar.setForeground(new java.awt.Color(255, 255, 255));
        lblRegresar.setText("Regresar");
        lblRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRegresarMouseClicked(evt);
            }
        });
        getContentPane().add(lblRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 40, 100, 30));

        btnVentas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-BVentas.png"))); // NOI18N
        btnVentas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnVentasMouseClicked(evt);
            }
        });
        getContentPane().add(btnVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, -1, 70));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Reportes");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 190, 70));

        btnClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Bclientes.png"))); // NOI18N
        btnClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClientesMouseClicked(evt);
            }
        });
        getContentPane().add(btnClientes, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 330, -1, 80));

        btnDevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Bdevo.png"))); // NOI18N
        btnDevo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDevoMouseClicked(evt);
            }
        });
        getContentPane().add(btnDevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 430, -1, 80));

        btnEmp.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-bEmp.png"))); // NOI18N
        btnEmp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEmpMouseClicked(evt);
            }
        });
        getContentPane().add(btnEmp, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 540, -1, 90));

        javax.swing.GroupLayout panelReportesLayout = new javax.swing.GroupLayout(panelReportes);
        panelReportes.setLayout(panelReportesLayout);
        panelReportesLayout.setHorizontalGroup(
            panelReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 850, Short.MAX_VALUE)
        );
        panelReportesLayout.setVerticalGroup(
            panelReportesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 540, Short.MAX_VALUE)
        );

        getContentPane().add(panelReportes, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 110, 850, 540));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Reportes.png"))); // NOI18N
        jLabel1.setText("jLabel1");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(-4, 0, 1360, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnVentasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnVentasMouseClicked
        mostrarReporteEnPanel("C:/Users/mabel/Documents/ProyectoFinal-Grupo2/src/Reportes/repVentas.jasper");
    }//GEN-LAST:event_btnVentasMouseClicked

    private void btnAlquilerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlquilerMouseClicked
        
         mostrarReporteEnPanel("C:/Users/mabel/Documents/ProyectoFinal-Grupo2/src/Reportes/repAlquiler.jasper");
        
        
        
        /* try {
        // Ruta directa del archivo
        String path = "C:/Users/mabel/Documents/ProyectoFinal-Grupo2/src/Reportes/repAlquiler.jasper";

        JasperReport reporte = (JasperReport) JRLoader.loadObjectFromFile(path);

        Map<String, Object> parametros = new HashMap<>();

        Connection con = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/Strada",
                "root",
                "H$1994lt"
        );

        JasperPrint jp = JasperFillManager.fillReport(reporte, parametros, con);

        JasperViewer viewer = new JasperViewer(jp, false);
        viewer.setTitle("Reporte de Alquiler");
        viewer.setVisible(true);

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al generar el reporte: " + e.getMessage());
    }*/

    }//GEN-LAST:event_btnAlquilerMouseClicked

    private void lblRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseClicked
        FrmMenu alq = new FrmMenu(loggedUser);
        alq.setVisible(true);
        dispose();
    }//GEN-LAST:event_lblRegresarMouseClicked

    private void btnClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClientesMouseClicked
       mostrarReporteEnPanel("C:/Users/mabel/Documents/ProyectoFinal-Grupo2/src/Reportes/repClientes.jasper");
    }//GEN-LAST:event_btnClientesMouseClicked

    private void btnDevoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDevoMouseClicked
        mostrarReporteEnPanel("C:/Users/mabel/Documents/ProyectoFinal-Grupo2/src/Reportes/repDevoluciones.jasper");
    }//GEN-LAST:event_btnDevoMouseClicked

    private void btnEmpMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEmpMouseClicked
        mostrarReporteEnPanel("C:/Users/mabel/Documents/ProyectoFinal-Grupo2/src/Reportes/repEmpleados.jasper");
    }//GEN-LAST:event_btnEmpMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmReportes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmReportes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmReportes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmReportes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmReportes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnAlquiler;
    private javax.swing.JLabel btnClientes;
    private javax.swing.JLabel btnDevo;
    private javax.swing.JLabel btnEmp;
    private javax.swing.JLabel btnVentas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel lblRegresar;
    private javax.swing.JPanel panelReportes;
    // End of variables declaration//GEN-END:variables
}
