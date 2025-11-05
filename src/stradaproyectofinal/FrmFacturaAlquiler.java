/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package stradaproyectofinal;

import java.awt.Image;
import javax.swing.ImageIcon;
import clases.clsConexion;
import clases.clsUtilidades;
import clases.clsCarga;
import clases.Estilos;
import java.awt.Image;
import java.sql.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author mabel
 */
public class FrmFacturaAlquiler extends javax.swing.JFrame {
    
    clsConexion con = new clsConexion();
    Connection cn = con.Sql_Conexion();
    clsUtilidades ut = new clsUtilidades();
    clsCarga car = new clsCarga();
    private int idAlquiler;
    private double totalAlquiler = 0;
    int idAlq;
    
    public FrmFacturaAlquiler(int idAlquiler) {
        initComponents();
        this.idAlq = idAlquiler;
        lblAlquiler.setText(String.valueOf(idAlq)); // opcional, mostrar ID
        cargarDatosAlquiler();
        Estilos.aplicarEstiloComboBox(cmbEstado);
        Estilos.aplicarEstiloComboBox(cmbPago);

        Estilos.aplicarEstiloTextField(txtBuscar);
        
        car.cargarDatos(cmbPago, "formapago", "idformapago", "descripcion");
        car.cargarDatos(cmbEstado, "estadofactura", "idestadofactura", "descripcion");
    
        mostrarFacturas();
    }

    public FrmFacturaAlquiler() {
        initComponents();
        
        this.idAlquiler = idAlquiler;
        
        this.setSize(1366, 768); 
         this.setLocationRelativeTo(null);
        
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Factura11.png"));
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
            this.getWidth(),   
            this.getHeight(),  
            Image.SCALE_SMOOTH
        );
        lblFondoFV.setIcon(new ImageIcon(imagenEscalada));
        
    }
    
    
    private void cargarDatosAlquiler() {
        try {
            String sql = "SELECT totalpagar FROM alquiler WHERE idalquiler = ?";
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, idAlquiler);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                totalAlquiler = rs.getDouble("totalpagar");
                lblTotal.setText(String.format("%.2f", totalAlquiler));
                lblAlquiler.setText("ID Alquiler: " + idAlquiler);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar total del alquiler: " + e.getMessage());
        }
    }

    private void registrarFactura() {
        try {
            String itemPago = cmbPago.getSelectedItem().toString();
            int idPago = Integer.parseInt(itemPago.split(" - ")[0]);

            String itemEstado = cmbEstado.getSelectedItem().toString();
            int idEstado = Integer.parseInt(itemEstado.split(" - ")[0]);

            String sql = "INSERT INTO facturaalquiler (idalquiler, fechafactura, idformapago, montopagado, idestadofactura) VALUES (?, ?, ?, ?, ?)";
            Object[] parametros = {
                idAlq,
                new java.sql.Date(jDateFactura.getDate().getTime()),
                idPago,
                totalAlquiler,
                idEstado
            };

            if (ut.ejecutarActualizacion(sql, parametros)) {
                JOptionPane.showMessageDialog(null, "Factura registrada correctamente.");
                mostrarFacturas();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al registrar factura: " + e.getMessage());
        }
    }

    private void mostrarFacturas() {
        String sql = "SELECT f.idfacturaalquiler, f.idalquiler, f.fechafactura, fp.descripcion AS 'Forma de pago', f.montopagado, ef.descripcion AS 'Estado' "
                   + "FROM facturaalquiler f "
                   + "JOIN formapago fp ON f.idformapago = fp.idformapago "
                   + "JOIN estadofactura ef ON f.idestadofactura = ef.idestadofactura";

        ut.mostrarDatos(sql, jTable1, new String[]{"ID Factura", "ID Alquiler", "Fecha", "Forma de Pago", "Monto", "Estado"});
    }
    
    private void editar() {
        String montoText = lblTotal.getText(); // o de donde obtengas el valor
        montoText = montoText.replace(",", "."); // cambia la coma por punto
        double monto = Double.parseDouble(montoText);
        
        int fila = jTable1.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una factura para editar.");
            return;
        }

        int idFactura = Integer.parseInt(jTable1.getValueAt(fila, 0).toString());

        String itemF = cmbPago.getSelectedItem().toString();
        int idF = Integer.parseInt(itemF.split(" - ")[0]);

        String itemE = cmbEstado.getSelectedItem().toString();
        int idE = Integer.parseInt(itemE.split(" - ")[0]);

        String sql = "UPDATE facturaalquiler SET idventa=?, fechafactura=?, idformapago=?, montopagado=?, idestadofactura=? "
                + "WHERE idfacturaalquiler=?";

        Object[] parametros = {
            idAlq,
            new java.sql.Date(jDateFactura.getDate().getTime()),
            idF,
            monto,
            idE,
            idFactura
        };

        if (ut.ejecutarActualizacion(sql, parametros)) {
            JOptionPane.showMessageDialog(null, "Factura actualizada correctamente.");
            mostrarFacturas();
        }
    }
    
    private void seleccionarFactura() {
    int fila = jTable1.getSelectedRow();
    if (fila != -1) {
        try {
            
            lblAlquiler.setText(jTable1.getValueAt(fila, 1).toString());
            
            try {
                java.util.Date fecha = java.sql.Date.valueOf(jTable1.getValueAt(fila, 2).toString());
                jDateFactura.setDate(fecha);
            } catch (Exception e) {
                jDateFactura.setDate(null);
            }
            
            String formaP = jTable1.getValueAt(fila, 3).toString();
            for (int i = 0; i < cmbPago.getItemCount(); i++) {
                if (cmbPago.getItemAt(i).contains(formaP)) {
                    cmbPago.setSelectedIndex(i);
                    break;
                }
            }
            
            lblTotal.setText(jTable1.getValueAt(fila, 4).toString());
            
            String estado = jTable1.getValueAt(fila, 5).toString();
            for (int i = 0; i < cmbEstado.getItemCount(); i++) {
                if (cmbEstado.getItemAt(i).contains(estado)) {
                    cmbEstado.setSelectedIndex(i);
                    break;
                }
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al seleccionar factura: " + e.getMessage());
        }
    }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        lblRegresar = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        lblTotal = new javax.swing.JLabel();
        cmbPago = new javax.swing.JComboBox<>();
        jDateFactura = new com.toedter.calendar.JDateChooser();
        lblAlquiler = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JLabel();
        btnEditar = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txtBuscar = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        lblFondoFV = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Facturación");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 40, 230, 70));

        lblRegresar.setFont(new java.awt.Font("Times New Roman", 2, 26)); // NOI18N
        lblRegresar.setForeground(new java.awt.Color(255, 255, 255));
        lblRegresar.setText("Regresar");
        lblRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRegresarMouseClicked(evt);
            }
        });
        getContentPane().add(lblRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 100, -1));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Id de alquiler:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, 130, 40));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Fecha de factura:");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 240, 160, 40));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Forma de pago:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 290, 140, 40));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Monto pagado:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 350, 140, 40));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Estado de la factura:");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 410, 190, 40));

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEstadoActionPerformed(evt);
            }
        });
        getContentPane().add(cmbEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 420, 220, -1));

        lblTotal.setFont(new java.awt.Font("PMingLiU-ExtB", 2, 18)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(242, 242, 242));
        lblTotal.setText("Monto");
        getContentPane().add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 360, 130, -1));

        cmbPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 310, 220, -1));
        getContentPane().add(jDateFactura, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 250, 220, -1));

        lblAlquiler.setFont(new java.awt.Font("PMingLiU-ExtB", 2, 18)); // NOI18N
        lblAlquiler.setForeground(new java.awt.Color(242, 242, 242));
        lblAlquiler.setText("Id de alquiler");
        getContentPane().add(lblAlquiler, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 190, 110, -1));

        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Regi.png"))); // NOI18N
        btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseClicked(evt);
            }
        });
        getContentPane().add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 550, -1, 90));

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Editar.png"))); // NOI18N
        btnEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditarMouseClicked(evt);
            }
        });
        getContentPane().add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 550, -1, 90));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 190, 510, -1));

        txtBuscar.setText("Buscar");
        getContentPane().add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 140, 470, -1));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-buscar.png"))); // NOI18N
        jLabel17.setText("jLabel17");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1130, 100, 130, 90));

        lblFondoFV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Factura11.png"))); // NOI18N
        lblFondoFV.setText("jLabel1");
        getContentPane().add(lblFondoFV, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -30, 1370, 830));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseClicked
        FrmMenu menu = new FrmMenu();
        menu.setVisible(true);
        dispose();
    }//GEN-LAST:event_lblRegresarMouseClicked

    private void cmbEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEstadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEstadoActionPerformed

    private void btnRegistrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseClicked
        // TODO add your handling code here:
        registrarFactura();
    }//GEN-LAST:event_btnRegistrarMouseClicked

    private void btnEditarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarMouseClicked
        // TODO add your handling code here:
        editar();
    }//GEN-LAST:event_btnEditarMouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:
        seleccionarFactura();
    }//GEN-LAST:event_jTable1MouseClicked

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
            java.util.logging.Logger.getLogger(FrmFacturaAlquiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmFacturaAlquiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmFacturaAlquiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmFacturaAlquiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmFacturaAlquiler().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnEditar;
    private javax.swing.JLabel btnRegistrar;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbPago;
    private com.toedter.calendar.JDateChooser jDateFactura;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblAlquiler;
    private javax.swing.JLabel lblFondoFV;
    private javax.swing.JLabel lblRegresar;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
