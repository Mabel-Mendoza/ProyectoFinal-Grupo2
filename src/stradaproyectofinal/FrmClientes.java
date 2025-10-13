/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package stradaproyectofinal;

import clases.Estilos;
import clases.clsCarga;
import clases.clsCliente;
import clases.clsConexion;
import clases.clsUtilidades;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.sql.Connection;
import javax.swing.JOptionPane;



/**
 *
 * @author mabel
 */
public class FrmClientes extends javax.swing.JFrame {
    
    

    clsConexion con = new clsConexion();
    Connection cn = con.Sql_Conexion();
    clsUtilidades ut = new clsUtilidades();
    clsCarga car = new clsCarga();
    clsCliente cli = new clsCliente();
    
    
    public FrmClientes() {
        initComponents();
        
        
    Estilos.aplicarEstiloTextField(txtNombre);
    Estilos.aplicarEstiloTextField(TxtApellido);
    Estilos.aplicarEstiloTextField(txtCorreo);
    Estilos.aplicarEstiloTextField(txtIdentidad);
    Estilos.aplicarEstiloTextField(txtDireccion);
    Estilos.aplicarEstiloTextField(txtTelefono);
    Estilos.aplicarEstiloTextField(txtBuscar);
    
    Estilos.aplicarEstiloTabla(jTable1);
    
    
    Estilos.aplicarEstiloComboBox(cmbsseg);
        
        this.setSize(1366, 768); 
         this.setLocationRelativeTo(null); 

    // Escalar la imagen al tamaño del JFrame
    ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Clientes1.png"));
    Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
            this.getWidth(),   
            this.getHeight(),  
            Image.SCALE_SMOOTH
    );
    lblFondoC.setIcon(new ImageIcon(imagenEscalada));
    
    
    
    car.cargarDatos(cmbsseg,"segmentacion","idsegmentacion", "nombresegmentacion");
        
        ut.mostrarDatos(sqlse, jTable1, new String[]{
            "ID", "Nombre", "Apellido", "No Identidad", "Edad", "Sexo", "Correo", "Dirección", "Teléfono", "Segmentación"
        });
    }
    
    String sqlse = "SELECT c.idcliente, c.nombrecliente, c.apellidocliente, c.noidentidad, "
       + "c.fechanacimiento, c.sexo, c.correo, c.direccion, c.telefono, s.nombresegmentacion "
       + "FROM clientes c "
       + "JOIN segmentacion s ON c.idsegmentacion = s.idsegmentacion";


    
    private void registrar() {
        
        String sexo = "";

        if(rbtfem.isSelected()){
            sexo = "F";
        }else{
            sexo = "M";
        }

        String item = cmbsseg.getSelectedItem().toString(); 
        int idSeg = Integer.parseInt(item.split(" - ")[0]); 

        String sql = "INSERT INTO clientes "
                   + "(nombrecliente, apellidocliente, noidentidad, fechanacimiento, sexo, correo, direccion, telefono, idsegmentacion) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";

            Object[] parametros = {
            txtNombre.getText(),
            TxtApellido.getText(),
            txtIdentidad.getText(),
            new java.sql.Date(jDatenacimiento.getDate().getTime()), 
            sexo,
            txtCorreo.getText(),
            txtDireccion.getText(),
            txtTelefono.getText(),
            idSeg
        };


        // 4. Ejecutar la inserción
            if(ut.ejecutarActualizacion(sql, parametros)) {
                JOptionPane.showMessageDialog(null, "Cliente registrado correctamente.");
                // 5. Limpiar campos
               // limpiarCampos();
                // 6. Actualizar tabla

                ut.mostrarDatos(sqlse, jTable1, 
                    new String[]{"ID", "Nombre", "Apellido", "No Identidad", "Fecha de nacimiento", "Sexo", "Correo", "Dirección", "Teléfono", "Segmentación"});
            }
    }
    
    private void editar() {
        int filaSeleccionada = jTable1.getSelectedRow();

        if(filaSeleccionada == -1){
            JOptionPane.showMessageDialog(null, "Seleccione un cliente para editar.");
            return;
        }

        int idcliente = Integer.parseInt(jTable1.getValueAt(filaSeleccionada, 0).toString());

        String sexo = rbtfem.isSelected() ? "F" : "M";

        String item = cmbsseg.getSelectedItem().toString(); 
        int idSeg = Integer.parseInt(item.split(" - ")[0]);

        String sql = "UPDATE clientes SET nombrecliente=?, apellidocliente=?, noidentidad=?, fechanacimiento=?, sexo=?, correo=?, direccion=?, telefono=?, idsegmentacion=? "
           + "WHERE idcliente=?";


        Object[] parametros = {
            txtNombre.getText(),
            TxtApellido.getText(),
            txtIdentidad.getText(),
            new java.sql.Date(jDatenacimiento.getDate().getTime()), 
            sexo,
            txtCorreo.getText(),
            txtDireccion.getText(),
            txtTelefono.getText(),
            idSeg,
            idcliente  
        };


        if(ut.ejecutarActualizacion(sql, parametros)){
            JOptionPane.showMessageDialog(null, "Cliente actualizado correctamente.");
            
            ut.mostrarDatos(sqlse, jTable1, new String[]{
                "ID", "Nombre", "Apellido", "No Identidad", "Edad", "Sexo", "Correo", "Dirección", "Teléfono", "Segmentación"
            });
        }
    }
    
    private void seleccion() {
       int fila = jTable1.getSelectedRow();

        if(fila != -1){
            txtNombre.setText(jTable1.getValueAt(fila, 1).toString());
            TxtApellido.setText(jTable1.getValueAt(fila, 2).toString());
            txtIdentidad.setText(jTable1.getValueAt(fila, 3).toString());
            try {
                    java.util.Date fecha = java.sql.Date.valueOf(jTable1.getValueAt(fila, 4).toString());
                    jDatenacimiento.setDate(fecha);
                } catch (Exception e) {
                    jDatenacimiento.setDate(null);
                }


            String sexo = jTable1.getValueAt(fila, 5).toString();
            if(sexo.equals("f")){
                rbtfem.setSelected(true);
            } else {
                rbtnmas.setSelected(true);
            }

            txtCorreo.setText(jTable1.getValueAt(fila, 6).toString());
            txtDireccion.setText(jTable1.getValueAt(fila, 7).toString());
            txtTelefono.setText(jTable1.getValueAt(fila, 8).toString());

            // Segmentación: buscamos el item del combo que contenga la descripción
            String descSeg = jTable1.getValueAt(fila, 9).toString();
            for(int i = 0; i < cmbsseg.getItemCount(); i++){
                if(cmbsseg.getItemAt(i).contains(descSeg)){
                    cmbsseg.setSelectedIndex(i);
                    break;
                }
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

        txtTelefono = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        txtNombre = new javax.swing.JTextField();
        txtIdentidad = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();
        TxtApellido = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jDatenacimiento = new com.toedter.calendar.JDateChooser();
        btnRegistrar = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        lblRegresar = new javax.swing.JLabel();
        cmbsseg = new javax.swing.JComboBox<>();
        rbtfem = new javax.swing.JRadioButton();
        rbtnmas = new javax.swing.JRadioButton();
        lblFondoC1 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        lblFondoC = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtTelefono.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoActionPerformed(evt);
            }
        });
        getContentPane().add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 500, 200, -1));

        txtDireccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionActionPerformed(evt);
            }
        });
        getContentPane().add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 450, 290, -1));
        getContentPane().add(txtNombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 200, -1));

        txtIdentidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdentidadActionPerformed(evt);
            }
        });
        getContentPane().add(txtIdentidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 250, 290, -1));
        getContentPane().add(txtCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 400, 250, -1));
        getContentPane().add(TxtApellido, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 200, 200, -1));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(760, 190, 530, 440));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Clientes");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 190, 70));

        txtBuscar.setText("Buscar");
        getContentPane().add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 140, 430, -1));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-buscar.png"))); // NOI18N
        jLabel17.setText("jLabel17");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 100, 130, 90));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Nombre:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 140, 80, 40));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Apellido:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 190, 90, 40));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("No. de identidad:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 240, 160, 40));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Fecha de nacimiento:");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 290, 190, 40));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Género:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 350, 80, 40));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Correo:");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 390, 70, 40));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Segmentación:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 550, 130, 40));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Dirección:");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 440, 100, 40));
        getContentPane().add(jDatenacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 300, 210, -1));

        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Regi.png"))); // NOI18N
        btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseClicked(evt);
            }
        });
        getContentPane().add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 650, -1, 90));

        jLabel16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Editar.png"))); // NOI18N
        jLabel16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel16MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 650, -1, 90));

        lblRegresar.setFont(new java.awt.Font("Times New Roman", 2, 26)); // NOI18N
        lblRegresar.setForeground(new java.awt.Color(255, 255, 255));
        lblRegresar.setText("Regresar");
        lblRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRegresarMouseClicked(evt);
            }
        });
        getContentPane().add(lblRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 100, -1));

        cmbsseg.setFont(new java.awt.Font("Times New Roman", 2, 14)); // NOI18N
        getContentPane().add(cmbsseg, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 550, 170, 30));

        rbtfem.setFont(new java.awt.Font("PMingLiU-ExtB", 2, 18)); // NOI18N
        rbtfem.setForeground(new java.awt.Color(255, 255, 255));
        rbtfem.setText("Femenino");
        getContentPane().add(rbtfem, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 360, 90, 30));

        rbtnmas.setFont(new java.awt.Font("PMingLiU-ExtB", 2, 18)); // NOI18N
        rbtnmas.setForeground(new java.awt.Color(255, 255, 255));
        rbtnmas.setText("Masculino");
        getContentPane().add(rbtnmas, new org.netbeans.lib.awtextra.AbsoluteConstraints(430, 360, 100, 30));

        lblFondoC1.setText("jLabel1");
        getContentPane().add(lblFondoC1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1366, -1));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Teléfono:");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 490, 90, 40));

        lblFondoC.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Clientes1.png"))); // NOI18N
        lblFondoC.setText("jLabel1");
        getContentPane().add(lblFondoC, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1366, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdentidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdentidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdentidadActionPerformed

    private void txtDireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionActionPerformed

    private void txtTelefonoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoActionPerformed

    private void lblRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseClicked
        FrmMenu menu = new FrmMenu();   
        menu.setVisible(true);          
        dispose();
    }//GEN-LAST:event_lblRegresarMouseClicked

    private void btnRegistrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseClicked
        registrar();
    }//GEN-LAST:event_btnRegistrarMouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        seleccion();
    }//GEN-LAST:event_jTable1MouseClicked

    private void jLabel16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel16MouseClicked
        editar();
    }//GEN-LAST:event_jLabel16MouseClicked

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
            java.util.logging.Logger.getLogger(FrmClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmClientes().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField TxtApellido;
    private javax.swing.JLabel btnRegistrar;
    private javax.swing.JComboBox<String> cmbsseg;
    private com.toedter.calendar.JDateChooser jDatenacimiento;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblFondoC;
    private javax.swing.JLabel lblFondoC1;
    private javax.swing.JLabel lblRegresar;
    private javax.swing.JRadioButton rbtfem;
    private javax.swing.JRadioButton rbtnmas;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtIdentidad;
    private javax.swing.JTextField txtNombre;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
