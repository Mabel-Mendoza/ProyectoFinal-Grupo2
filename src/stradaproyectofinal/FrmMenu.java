/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package stradaproyectofinal;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 *
 * @author mabel
 */
public class FrmMenu extends javax.swing.JFrame {

    public FrmMenu() {
        initComponents();
        
        this.setSize(1366, 768); 
         this.setLocationRelativeTo(null); // Centrar pantalla

    // Escalar la imagen al tamaño del JFrame
   /* ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Menu.png"));
    Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
            this.getWidth(),   
            this.getHeight(),  
            Image.SCALE_SMOOTH
    );*/
   // lblFondoM.setIcon(new ImageIcon(imagenEscalada));
        
        
        
        
    }

   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnDevolucion = new javax.swing.JButton();
        btnVehiculos = new javax.swing.JButton();
        btnVentas = new javax.swing.JButton();
        btnEmpleados = new javax.swing.JButton();
        btnAlquiler = new javax.swing.JButton();
        btnReporte = new javax.swing.JButton();
        btnPagos = new javax.swing.JButton();
        btnClientes1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblRegresar = new javax.swing.JLabel();
        lblFondoM = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnDevolucion.setBackground(new java.awt.Color(153, 0, 0));
        btnDevolucion.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        btnDevolucion.setForeground(new java.awt.Color(255, 253, 253));
        btnDevolucion.setText("Devolucion de alquiler");
        btnDevolucion.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 4, 4, 0, new java.awt.Color(255, 255, 255)));
        btnDevolucion.setMaximumSize(new java.awt.Dimension(66, 27));
        btnDevolucion.setMinimumSize(new java.awt.Dimension(66, 27));
        btnDevolucion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDevolucionActionPerformed(evt);
            }
        });
        getContentPane().add(btnDevolucion, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 320, 250, 70));

        btnVehiculos.setBackground(new java.awt.Color(153, 0, 0));
        btnVehiculos.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        btnVehiculos.setForeground(new java.awt.Color(255, 253, 253));
        btnVehiculos.setText("Vehículos");
        btnVehiculos.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 4, 4, 0, new java.awt.Color(255, 255, 255)));
        btnVehiculos.setMaximumSize(new java.awt.Dimension(66, 27));
        btnVehiculos.setMinimumSize(new java.awt.Dimension(66, 27));
        btnVehiculos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVehiculosActionPerformed(evt);
            }
        });
        getContentPane().add(btnVehiculos, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 170, 250, 70));

        btnVentas.setBackground(new java.awt.Color(153, 0, 0));
        btnVentas.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        btnVentas.setForeground(new java.awt.Color(255, 253, 253));
        btnVentas.setText("Ventas");
        btnVentas.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 4, 4, 0, new java.awt.Color(255, 255, 255)));
        btnVentas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVentasActionPerformed(evt);
            }
        });
        getContentPane().add(btnVentas, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 610, 260, 70));

        btnEmpleados.setBackground(new java.awt.Color(153, 0, 0));
        btnEmpleados.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        btnEmpleados.setForeground(new java.awt.Color(255, 253, 253));
        btnEmpleados.setText("Empleados");
        btnEmpleados.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 4, 4, 0, new java.awt.Color(255, 255, 255)));
        btnEmpleados.setMaximumSize(new java.awt.Dimension(66, 27));
        btnEmpleados.setMinimumSize(new java.awt.Dimension(66, 27));
        btnEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmpleadosActionPerformed(evt);
            }
        });
        getContentPane().add(btnEmpleados, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 460, 250, 70));

        btnAlquiler.setBackground(new java.awt.Color(153, 0, 0));
        btnAlquiler.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        btnAlquiler.setForeground(new java.awt.Color(255, 253, 253));
        btnAlquiler.setText("Alquiler");
        btnAlquiler.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 4, 4, 0, new java.awt.Color(255, 255, 255)));
        btnAlquiler.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlquilerActionPerformed(evt);
            }
        });
        getContentPane().add(btnAlquiler, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 170, 260, 70));

        btnReporte.setBackground(new java.awt.Color(153, 0, 0));
        btnReporte.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        btnReporte.setForeground(new java.awt.Color(255, 253, 253));
        btnReporte.setText("Reporte");
        btnReporte.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 4, 4, 0, new java.awt.Color(255, 255, 255)));
        btnReporte.setMaximumSize(new java.awt.Dimension(66, 27));
        btnReporte.setMinimumSize(new java.awt.Dimension(66, 27));
        btnReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteActionPerformed(evt);
            }
        });
        getContentPane().add(btnReporte, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 610, 260, 70));

        btnPagos.setBackground(new java.awt.Color(153, 0, 0));
        btnPagos.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        btnPagos.setForeground(new java.awt.Color(255, 253, 253));
        btnPagos.setText("Pagos");
        btnPagos.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 4, 4, 0, new java.awt.Color(255, 255, 255)));
        btnPagos.setMaximumSize(new java.awt.Dimension(66, 27));
        btnPagos.setMinimumSize(new java.awt.Dimension(66, 27));
        btnPagos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPagosActionPerformed(evt);
            }
        });
        getContentPane().add(btnPagos, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 470, 260, 70));

        btnClientes1.setBackground(new java.awt.Color(153, 0, 0));
        btnClientes1.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        btnClientes1.setForeground(new java.awt.Color(255, 253, 253));
        btnClientes1.setText("Clientes");
        btnClientes1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 4, 4, 0, new java.awt.Color(255, 255, 255)));
        btnClientes1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClientes1ActionPerformed(evt);
            }
        });
        getContentPane().add(btnClientes1, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 320, 250, 70));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Menú");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 50, 140, 70));

        lblRegresar.setFont(new java.awt.Font("Times New Roman", 2, 26)); // NOI18N
        lblRegresar.setForeground(new java.awt.Color(255, 255, 255));
        lblRegresar.setText("Regresar");
        lblRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRegresarMouseClicked(evt);
            }
        });
        getContentPane().add(lblRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 100, -1));

        lblFondoM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Menu12.png"))); // NOI18N
        lblFondoM.setText("jLabel1");
        getContentPane().add(lblFondoM, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1365, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDevolucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDevolucionActionPerformed
        FrmDevolucion ventana = new FrmDevolucion();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnDevolucionActionPerformed

    private void btnVehiculosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVehiculosActionPerformed
        FrmVehiculos ventana = new FrmVehiculos();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnVehiculosActionPerformed

    private void btnVentasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVentasActionPerformed
            FrmVentas ventana = new FrmVentas();
            ventana.setVisible(true);
            this.dispose();
    }//GEN-LAST:event_btnVentasActionPerformed

    private void btnEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmpleadosActionPerformed
        FrmEmpleados ventana = new FrmEmpleados();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnEmpleadosActionPerformed

    private void btnAlquilerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlquilerActionPerformed
        FrmAlquiler ventana = new FrmAlquiler();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnAlquilerActionPerformed

    private void btnReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnReporteActionPerformed

    private void btnPagosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPagosActionPerformed
        FrmPagos ventana = new FrmPagos();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnPagosActionPerformed

    private void btnClientes1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClientes1ActionPerformed
        FrmClientes ventana = new FrmClientes();
        ventana.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnClientes1ActionPerformed

    private void lblRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseClicked
        FrmMenu menu = new FrmMenu();
        menu.setVisible(true);
        dispose();
    }//GEN-LAST:event_lblRegresarMouseClicked

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
            java.util.logging.Logger.getLogger(FrmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlquiler;
    private javax.swing.JButton btnClientes1;
    private javax.swing.JButton btnDevolucion;
    private javax.swing.JButton btnEmpleados;
    private javax.swing.JButton btnPagos;
    private javax.swing.JButton btnReporte;
    private javax.swing.JButton btnVehiculos;
    private javax.swing.JButton btnVentas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblFondoM;
    private javax.swing.JLabel lblRegresar;
    // End of variables declaration//GEN-END:variables
}
