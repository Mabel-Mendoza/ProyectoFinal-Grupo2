/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package stradaproyectofinal;

import clases.Estilos;
import clases.clsCarga;
import clases.clsConexion;
import clases.clsUtilidades;
import java.sql.Connection;
import clases.clsUtilidades;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.sql.Connection;
import javax.swing.JOptionPane;
import com.toedter.calendar.JDateChooser;
import java.text.DecimalFormat;
import java.sql.*;
import java.util.Calendar;
import javax.swing.*;


/**
 *
 * @author mabel
 */
public class FrmDevolucion extends javax.swing.JFrame {
    
    clsConexion con = new clsConexion();
    Connection cn = con.Sql_Conexion();
    clsUtilidades ut = new clsUtilidades();
    clsCarga car = new clsCarga();
    
    
    
    ButtonGroup grupoDanio = new ButtonGroup(); 
    java.sql.Date fechaMinimaPermitida = null;
    
    public FrmDevolucion() {
        initComponents();
        
        Estilos.aplicarEstiloComboBox(cmbId);
        Estilos.aplicarEstiloTextField(txtBuscar);
        Estilos.aplicarEstiloTextField(txtKiloF);
        
        Estilos.aplicarEstiloTabla(jTable1);
        
        
         jScrollPane2.setVisible(false); // oculta el recuadro completo del JTextArea
         lblcargo.setVisible(false);
         
          lblDes.setVisible(false);        
          lblCargoEx.setVisible(false);
        
        grupoDanio.add(rbtSi);
        grupoDanio.add(rbtNo);
        
        
         this.setSize(1366, 768); 
         this.setLocationRelativeTo(null); 
         
         
        
         cargarIdsAlquiler();
        
       
        ut.mostrarDatos(sqlMostrar, jTable1, new String[]{
            "ID", "ID Alquiler", "Fecha Final", "Kilometraje Final", "Daño", "Cargo Extra"
        });
        
        
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                seleccion();
            }
        });

        
        cmbId.addActionListener(e -> {
            cargarFechaMinima();
            calcularCargoExtra(); 
        });
       
        rbtSi.addActionListener(e -> mostrarCampoDanio(true));
        rbtNo.addActionListener(e -> mostrarCampoDanio(false));
        
        
        rbtSi.addActionListener(e -> calcularCargoExtra());
        rbtNo.addActionListener(e -> calcularCargoExtra());

        
        
    }
    
     
    String sqlMostrar = "SELECT iddevolucion, idalquiler, fechafinal, kilometrajefinal, dano, cargoextra FROM devolucionalquiler";

   
    private void cargarIdsAlquiler() {
        try {
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery("SELECT idalquiler FROM alquiler");
            cmbId.removeAllItems();
            while (rs.next()) {
                cmbId.addItem(rs.getString("idalquiler"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar IDs de alquiler: " + e.getMessage());
        }
    }

    
    private void cargarFechaMinima() {
        try {
            if (cmbId.getSelectedItem() == null) return;
            
            String idAlquiler = cmbId.getSelectedItem().toString();
            String sql = "SELECT fechainicio, totaldias FROM alquiler WHERE idalquiler = ?";
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, idAlquiler);
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                Date fechaInicio = rs.getDate("fechainicio");
                int dias = rs.getInt("totaldias");
                
                Calendar cal = Calendar.getInstance();
                cal.setTime(fechaInicio);
                cal.add(Calendar.DAY_OF_MONTH, dias);
                fechaMinimaPermitida = new java.sql.Date(cal.getTimeInMillis());
                
                jDateFinal.setDate(fechaMinimaPermitida); // establecer como predeterminada
            
                 jDateFinal.getJCalendar().setMinSelectableDate(fechaMinimaPermitida);
            
     
            
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener la fecha mínima: " + e.getMessage());
        }
        
         
    }

    
    private void mostrarCampoDanio(boolean mostrar) {
         jScrollPane2.setVisible(mostrar); // ocultar/mostrar el área de daño completa
    lblcargo.setVisible(mostrar);
    
    lblCargoEx.setVisible(mostrar);  // label "Cargo extra:"
    lblcargo.setVisible(mostrar); 
    
    if (!mostrar) {
        txtAreaDano.setText("");
        lblcargo.setText("Cargo extra");
    }

    // Esto ayuda a que el formulario se redibuje correctamente
    this.revalidate();
    this.repaint();
    }

    
    private void registrarDevolucion() {
        if (cmbId.getSelectedItem() == null || jDateFinal.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Por favor seleccione un alquiler y una fecha.");
            return;
        }

        String idAlquiler = cmbId.getSelectedItem().toString();
        java.sql.Date fechaFinal = new java.sql.Date(jDateFinal.getDate().getTime());
        
        // ✅ Validar que la fecha final no sea antes de la mínima
        if (fechaMinimaPermitida != null && fechaFinal.before(fechaMinimaPermitida)) {
            JOptionPane.showMessageDialog(null, 
                "La fecha de devolución no puede ser antes del " + fechaMinimaPermitida.toString());
            return;
        }

        int kilometrajeFinal = Integer.parseInt(txtKiloF.getText());
        boolean hayDano = rbtSi.isSelected();
        String descripcion = null;
        Double cargoExtra = null;
        
        if (hayDano) {
            descripcion = txtAreaDano.getText();
            try {
                String sqlVehiculo = "SELECT a.idvehiculo, v.precio FROM alquiler a JOIN vehiculos v ON a.idvehiculo = v.idvehiculo WHERE a.idalquiler = ?";
                PreparedStatement ps = cn.prepareStatement(sqlVehiculo);
                ps.setString(1, idAlquiler);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    double precio = rs.getDouble("precio");
                    cargoExtra = precio * 0.05;
                    lblcargo.setText(String.format("%.2f", cargoExtra));
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al calcular cargo extra: " + e.getMessage());
            }
        }

        String sqlInsert = "INSERT INTO devolucionalquiler (idalquiler, fechafinal, kilometrajefinal, dano, cargoextra) VALUES (?, ?, ?, ?, ?)";
        Object[] parametros = {
            idAlquiler,
            fechaFinal,
            kilometrajeFinal,
            hayDano ? descripcion : null,
            cargoExtra
        };
        
        if (ut.ejecutarActualizacion(sqlInsert, parametros)) {
            JOptionPane.showMessageDialog(null, "Devolución registrada correctamente.");
            
            actualizarKilometrajeVehiculo(idAlquiler, kilometrajeFinal);
            
            ut.mostrarDatos(sqlMostrar, jTable1, new String[]{
                "ID", "ID Alquiler", "Fecha Final", "Kilometraje Final", "Daño", "Cargo Extra"
            });
        }
    }
    
    
    
    
private void calcularCargoExtra() {
    if (cmbId.getSelectedItem() == null) return;
    
    
    if (rbtSi.isSelected()) {
        txtAreaDano.setVisible(true);
        lblDes.setVisible(true);        
        lblCargoEx.setVisible(true);    
        lblcargo.setVisible(true); 

        try {
            String idAlquiler = cmbId.getSelectedItem().toString();
            String sqlVehiculo = "SELECT v.precio FROM alquiler a JOIN vehiculos v ON a.idvehiculo = v.idvehiculo WHERE a.idalquiler = ?";
            PreparedStatement ps = cn.prepareStatement(sqlVehiculo);
            ps.setString(1, idAlquiler);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double precio = rs.getDouble("precio");
                double cargoExtra = precio * 0.05;
                lblcargo.setText("Cargo extra: L. " + String.format("%.2f", cargoExtra));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al calcular cargo extra: " + e.getMessage());
        }
    } else if (rbtNo.isSelected()) {
         txtAreaDano.setVisible(false);
        lblDes.setVisible(false);        
        lblCargoEx.setVisible(false);    
        lblcargo.setVisible(false);      
        txtAreaDano.setText("");
        lblcargo.setText("");
    } else {
        // Si no ha seleccionado ninguno aún
        lblDes.setVisible(false);
        lblCargoEx.setVisible(false);
        lblcargo.setVisible(false);
        txtAreaDano.setVisible(false);
    }
}

    
    
    private void actualizarKilometrajeVehiculo(String idAlquiler, int kilometrajeFinal) {
        try {
            String sqlVeh = "SELECT idvehiculo FROM alquiler WHERE idalquiler = ?";
            PreparedStatement ps1 = cn.prepareStatement(sqlVeh);
            ps1.setString(1, idAlquiler);
            ResultSet rs = ps1.executeQuery();
            if (rs.next()) {
                int idVehiculo = rs.getInt("idvehiculo");
                String sqlUpdate = "UPDATE vehiculos SET kilometraje = ? WHERE idvehiculo = ?";
                PreparedStatement ps2 = cn.prepareStatement(sqlUpdate);
                ps2.setInt(1, kilometrajeFinal);
                ps2.setInt(2, idVehiculo);
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar kilometraje del vehículo: " + e.getMessage());
        }
    }

    
    private void seleccion() {
    int fila = jTable1.getSelectedRow();

    if (fila != -1) {
        try {
            // ID alquiler
            String idAlquiler = jTable1.getValueAt(fila, 1).toString();
            cmbId.setSelectedItem(idAlquiler);

            // Fecha final
            Object fechaObj = jTable1.getValueAt(fila, 2);
            if (fechaObj != null) {
                java.util.Date fecha = java.sql.Date.valueOf(fechaObj.toString());
                jDateFinal.setDate(fecha);
            } else {
                jDateFinal.setDate(null);
            }

            // Kilometraje final
            txtKiloF.setText(jTable1.getValueAt(fila, 3).toString());

            // Daño (si existe)
            Object danoObj = jTable1.getValueAt(fila, 4);
            if (danoObj != null && !danoObj.toString().isEmpty()) {
                rbtSi.setSelected(true);
                txtAreaDano.setVisible(true);
                lblDes.setVisible(true);
                lblCargoEx.setVisible(true);
                lblcargo.setVisible(true);
                txtAreaDano.setText(danoObj.toString());
            } else {
                rbtNo.setSelected(true);
                txtAreaDano.setVisible(false);
                lblDes.setVisible(false);
                lblCargoEx.setVisible(false);
                lblcargo.setVisible(false);
                txtAreaDano.setText("");
            }

            // Cargo extra
            Object cargoObj = jTable1.getValueAt(fila, 5);
            if (cargoObj != null) {
                lblcargo.setText("Cargo extra: L. " + cargoObj.toString());
            } else {
                lblcargo.setText("");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al cargar los datos seleccionados: " + e.getMessage());
        }
    } else {
        JOptionPane.showMessageDialog(null, "Seleccione un registro para editar.");
    }
}

    
   private void editar() {
    int filaSeleccionada = jTable1.getSelectedRow();

    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(null, "Seleccione una devolución para editar.");
        return;
    }

    // Obtener el id de la devolución
    int idDevolucion = Integer.parseInt(jTable1.getValueAt(filaSeleccionada, 0).toString());

    String idAlquiler = cmbId.getSelectedItem().toString();
    java.sql.Date fechaFinal = new java.sql.Date(jDateFinal.getDate().getTime());
    int kilometrajeFinal = Integer.parseInt(txtKiloF.getText());
    boolean hayDano = rbtSi.isSelected();

    String descripcion = hayDano ? txtAreaDano.getText() : null;
    Double cargoExtra = null;

    // Si hay daño, calcular el cargo extra nuevamente
    if (hayDano) {
        try {
            String sqlVehiculo = "SELECT v.precio FROM alquiler a JOIN vehiculos v ON a.idvehiculo = v.idvehiculo WHERE a.idalquiler = ?";
            PreparedStatement ps = cn.prepareStatement(sqlVehiculo);
            ps.setString(1, idAlquiler);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                double precio = rs.getDouble("precio");
                cargoExtra = precio * 0.05;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al calcular cargo extra: " + e.getMessage());
        }
    }

    // Actualizar en la base de datos
    String sql = "UPDATE devolucionalquiler SET idalquiler=?, fechafinal=?, kilometrajefinal=?, dano=?, cargoextra=? WHERE iddevolucion=?";

    Object[] parametros = {
        idAlquiler,
        fechaFinal,
        kilometrajeFinal,
        descripcion,
        cargoExtra,
        idDevolucion
    };

    if (ut.ejecutarActualizacion(sql, parametros)) {
        JOptionPane.showMessageDialog(null, "Devolución actualizada correctamente.");

        // Refrescar la tabla
        ut.mostrarDatos(sqlMostrar, jTable1, new String[]{
            "ID", "ID Alquiler", "Fecha Final", "Kilometraje Final", "Daño", "Cargo Extra"
        });
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel6 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        lblRegresar = new javax.swing.JLabel();
        cmbId = new javax.swing.JComboBox<>();
        btnRegistrar = new javax.swing.JLabel();
        btnEditar = new javax.swing.JLabel();
        jDateFinal = new com.toedter.calendar.JDateChooser();
        lblcargo = new javax.swing.JLabel();
        rbtNo = new javax.swing.JRadioButton();
        rbtSi = new javax.swing.JRadioButton();
        txtKiloF = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblDes = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaDano = new javax.swing.JTextArea();
        lblCargoEx = new javax.swing.JLabel();
        lblFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Devolución");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 30, 230, 70));

        txtBuscar.setText("Buscar");
        getContentPane().add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 160, 500, -1));

        lblRegresar.setFont(new java.awt.Font("Times New Roman", 2, 26)); // NOI18N
        lblRegresar.setForeground(new java.awt.Color(255, 255, 255));
        lblRegresar.setText("Regresar");
        lblRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRegresarMouseClicked(evt);
            }
        });
        getContentPane().add(lblRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 100, -1));

        cmbId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbId, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 150, 180, -1));

        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Regi.png"))); // NOI18N
        btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseClicked(evt);
            }
        });
        getContentPane().add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 560, -1, 90));

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Editar.png"))); // NOI18N
        btnEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditarMouseClicked(evt);
            }
        });
        getContentPane().add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 560, -1, 90));
        getContentPane().add(jDateFinal, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 200, 210, -1));

        lblcargo.setFont(new java.awt.Font("PMingLiU-ExtB", 2, 18)); // NOI18N
        lblcargo.setForeground(new java.awt.Color(255, 255, 255));
        lblcargo.setText("cargo extra");
        getContentPane().add(lblcargo, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 510, 270, -1));

        rbtNo.setFont(new java.awt.Font("PMingLiU-ExtB", 2, 18)); // NOI18N
        rbtNo.setForeground(new java.awt.Color(255, 255, 255));
        rbtNo.setText("No");
        rbtNo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtNoActionPerformed(evt);
            }
        });
        getContentPane().add(rbtNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 320, 60, 30));

        rbtSi.setFont(new java.awt.Font("PMingLiU-ExtB", 2, 18)); // NOI18N
        rbtSi.setForeground(new java.awt.Color(255, 255, 255));
        rbtSi.setText("Si");
        rbtSi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtSiActionPerformed(evt);
            }
        });
        getContentPane().add(rbtSi, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 320, 60, 30));
        getContentPane().add(txtKiloF, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 260, 210, -1));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 210, 570, 470));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-buscar.png"))); // NOI18N
        jLabel17.setText("jLabel17");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1110, 120, 140, 90));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Id alquiler:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 100, 40));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Fecha de entrega:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 190, 160, 40));

        lblDes.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        lblDes.setForeground(new java.awt.Color(255, 255, 255));
        lblDes.setText("Descripción:");
        getContentPane().add(lblDes, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 370, 120, 40));

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Kilometraje final:");
        getContentPane().add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 250, 160, 40));

        jLabel24.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("daño:");
        getContentPane().add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 310, 50, 40));

        txtAreaDano.setColumns(20);
        txtAreaDano.setRows(5);
        jScrollPane2.setViewportView(txtAreaDano);

        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 380, 250, 100));

        lblCargoEx.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        lblCargoEx.setForeground(new java.awt.Color(255, 255, 255));
        lblCargoEx.setText("Cargo extra:");
        getContentPane().add(lblCargoEx, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 500, 110, 40));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Devolucion.png"))); // NOI18N
        lblFondo.setText("jLabel1");
        getContentPane().add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1365, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseClicked
        FrmMenu menu = new FrmMenu();
        menu.setVisible(true);
        dispose();
    }//GEN-LAST:event_lblRegresarMouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnRegistrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseClicked
       registrarDevolucion();
    }//GEN-LAST:event_btnRegistrarMouseClicked

    private void btnEditarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarMouseClicked
        editar();
    }//GEN-LAST:event_btnEditarMouseClicked

    private void rbtSiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtSiActionPerformed
         mostrarCampoDanio(true);
    }//GEN-LAST:event_rbtSiActionPerformed

    private void rbtNoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtNoActionPerformed
        mostrarCampoDanio(false);
    }//GEN-LAST:event_rbtNoActionPerformed

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
            java.util.logging.Logger.getLogger(FrmDevolucion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmDevolucion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmDevolucion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmDevolucion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmDevolucion().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnEditar;
    private javax.swing.JLabel btnRegistrar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cmbId;
    private com.toedter.calendar.JDateChooser jDateFinal;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblCargoEx;
    private javax.swing.JLabel lblDes;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JLabel lblRegresar;
    private javax.swing.JLabel lblcargo;
    private javax.swing.JRadioButton rbtNo;
    private javax.swing.JRadioButton rbtSi;
    private javax.swing.JTextArea txtAreaDano;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtKiloF;
    // End of variables declaration//GEN-END:variables
}
