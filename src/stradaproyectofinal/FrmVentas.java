/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package stradaproyectofinal;

import clases.Estilos;
import clases.clsCarga;
import clases.clsConexion;
import clases.clsUtilidades;
import java.awt.Image;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author mabel
 */
public class FrmVentas extends javax.swing.JFrame {

    clsConexion con = new clsConexion();
    Connection cn = con.Sql_Conexion();
    clsUtilidades ut = new clsUtilidades();
    clsCarga car = new clsCarga();
    
    
    double precioVenta = 0;
    double isv = 0;
    double descuento = 0;
    double total = 0;
    
    
    
    public FrmVentas() {
        initComponents();
        
        this.setSize(1366, 768); 
         this.setLocationRelativeTo(null); // Centrar pantalla
         
         
         Estilos.aplicarEstiloTabla(jTable1);
    
        Estilos.aplicarEstiloComboBox(cmbCliente);
        Estilos.aplicarEstiloComboBox(cmbEmpleado);
        Estilos.aplicarEstiloComboBox(cmbVehiculo);
        Estilos.aplicarEstiloComboBox(cmbEstado);
        Estilos.aplicarEstiloComboBox(cmbDescuento);

        Estilos.aplicarEstiloTextField(txtBuscar);

    // Escalar la imagen al tamaño del JFrame
    ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Ventas1.png"));
    Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
            this.getWidth(),   
            this.getHeight(),  
            Image.SCALE_SMOOTH
    );
    lblFondoV.setIcon(new ImageIcon(imagenEscalada));
    
    
    
    
    // Llenar combos desde la BD
        car.cargarDatos(cmbCliente, "clientes", "idcliente", "CONCAT(nombrecliente, ' ', apellidocliente)");
        car.cargarDatos(cmbEmpleado, "empleados", "idempleado", "CONCAT(nombreempleado, ' ', apellidoempleado)");
        car.cargarDatos(cmbVehiculo, "vehiculos", "idvehiculo", "modelo");
        car.cargarDatos(cmbEstado, "estadoventa", "idestadoventa", "descripcion");

        // Descuentos manuales
        cmbDescuento.addItem("1 - Tercera Edad (10%)");
        cmbDescuento.addItem("2 - Promoción (5%)");

        // Mostrar datos
        ut.mostrarDatos(sqlse, jTable1, new String[]{
            "ID", "Cliente", "Empleado", "Vehículo", "Fecha", "Precio", "Descuento", "ISV", "Total", "Estado"
        });

        // Evento seleccionar vehículo
        cmbVehiculo.addActionListener(e -> cargarPrecioVehiculo());

        // Evento descuento
        cmbDescuento.addActionListener(e -> calcularTotales());
    }

    // Consulta principal
    String sqlse = "SELECT v.idventa, " +
            "CONCAT(c.nombrecliente,' ',c.apellidocliente) AS cliente, " +
            "CONCAT(e.nombreempleado,' ',e.apellidoempleado) AS empleado, " +
            "ve.modelo AS vehiculo, v.fecha, v.precioventa, v.descuentoventa, v.isv, v.totalventa, ev.descripcion " +
            "FROM ventas v " +
            "JOIN clientes c ON v.idcliente = c.idcliente " +
            "JOIN empleados e ON v.idempleado = e.idempleado " +
            "JOIN vehiculos ve ON v.idvehiculo = ve.idvehiculo " +
            "JOIN estadoventa ev ON v.idestadoventa = ev.idestadoventa";

    private void cargarPrecioVehiculo() {
        try {
            String item = cmbVehiculo.getSelectedItem().toString();
            int idVehiculo = Integer.parseInt(item.split(" - ")[0]);

            String sql = "SELECT precio FROM vehiculos WHERE idvehiculo = " + idVehiculo;
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                precioVenta = rs.getDouble("precio");
                lblPrecio.setText(String.valueOf(precioVenta));
                calcularTotales();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al cargar precio: " + ex.getMessage());
        }
    }

    private void calcularTotales() {
        descuento = 0;
        if (cmbDescuento.getSelectedItem() != null) {
            String desc = cmbDescuento.getSelectedItem().toString();
            if (desc.contains("Tercera Edad")) {
                descuento = precioVenta * 0.10;
            } else if (desc.contains("Promoción")) {
                descuento = precioVenta * 0.05;
            }
        }

        isv = precioVenta * 0.15; // 15% impuesto
        total = (precioVenta - descuento) + isv;

        lblISV.setText(String.valueOf(isv));
        lblTotal.setText(String.valueOf(total));
    }

    private void registrar() {
        String itemC = cmbCliente.getSelectedItem().toString();
        int idC = Integer.parseInt(itemC.split(" - ")[0]);

        String itemE = cmbEmpleado.getSelectedItem().toString();
        int idE = Integer.parseInt(itemE.split(" - ")[0]);

        String itemV = cmbVehiculo.getSelectedItem().toString();
        int idV = Integer.parseInt(itemV.split(" - ")[0]);

        String itemEV = cmbEstado.getSelectedItem().toString();
        int idEV = Integer.parseInt(itemEV.split(" - ")[0]);

        String sql = "INSERT INTO ventas (idcliente, idempleado, idvehiculo, fecha, precioventa, descuentoventa, isv, totalventa, idestadoventa) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] parametros = {
            idC,
            idE,
            idV,
            new java.sql.Date(jDateVenta.getDate().getTime()),
            precioVenta,
            descuento,
            isv,
            total,
            idEV
        };

        if (ut.ejecutarActualizacion(sql, parametros)) {
            JOptionPane.showMessageDialog(null, "Venta registrada correctamente.");
            ut.mostrarDatos(sqlse, jTable1, new String[]{
                "ID", "Cliente", "Empleado", "Vehículo", "Fecha", "Precio", "Descuento", "ISV", "Total", "Estado"
            });
        }
    }
    
    
    
    private void seleccionarVenta() {
    int fila = jTable1.getSelectedRow();
    if (fila != -1) {
        try {
            // Cliente (comboBox)
            String cliente = jTable1.getValueAt(fila, 1).toString();
            for (int i = 0; i < cmbCliente.getItemCount(); i++) {
                if (cmbCliente.getItemAt(i).contains(cliente)) {
                    cmbCliente.setSelectedIndex(i);
                    break;
                }
            }

            // Empleado (comboBox)
            String empleado = jTable1.getValueAt(fila, 2).toString();
            for (int i = 0; i < cmbEmpleado.getItemCount(); i++) {
                if (cmbEmpleado.getItemAt(i).contains(empleado)) {
                    cmbEmpleado.setSelectedIndex(i);
                    break;
                }
            }

            // Vehículo (comboBox)
            String vehiculo = jTable1.getValueAt(fila, 3).toString();
            for (int i = 0; i < cmbVehiculo.getItemCount(); i++) {
                if (cmbVehiculo.getItemAt(i).contains(vehiculo)) {
                    cmbVehiculo.setSelectedIndex(i);
                    break;
                }
            }

            // Fecha de venta (DateChooser)
            try {
                Date fecha = java.sql.Date.valueOf(jTable1.getValueAt(fila, 4).toString());
                jDateVenta.setDate(fecha);
            } catch (Exception e) {
                jDateVenta.setDate(null);
            }

            // Precio (label)
            precioVenta = Double.parseDouble(jTable1.getValueAt(fila, 5).toString());
            lblPrecio.setText(String.valueOf(precioVenta));

            // Descuento (comboBox + variable)
            String desc = jTable1.getValueAt(fila, 6).toString();
            if (!desc.equals("0.0")) {
                if (desc.equals("10.0")) { // Ejemplo para Tercera Edad
                    for (int i = 0; i < cmbDescuento.getItemCount(); i++) {
                        if (cmbDescuento.getItemAt(i).contains("Tercera Edad")) {
                            cmbDescuento.setSelectedIndex(i);
                            break;
                        }
                    }
                } else if (desc.equals("5.0")) { // Ejemplo para Promoción
                    for (int i = 0; i < cmbDescuento.getItemCount(); i++) {
                        if (cmbDescuento.getItemAt(i).contains("Promoción")) {
                            cmbDescuento.setSelectedIndex(i);
                            break;
                        }
                    }
                }
            } else {
                cmbDescuento.setSelectedIndex(-1); // sin descuento
            }
            descuento = Double.parseDouble(jTable1.getValueAt(fila, 6).toString());

            // ISV
            isv = Double.parseDouble(jTable1.getValueAt(fila, 7).toString());
            lblISV.setText(String.valueOf(isv));

            // Total
            total = Double.parseDouble(jTable1.getValueAt(fila, 8).toString());
            lblTotal.setText(String.valueOf(total));

            // Estado (comboBox)
            String estadoVenta = jTable1.getValueAt(fila, 9).toString();
            for (int i = 0; i < cmbEstado.getItemCount(); i++) {
                if (cmbEstado.getItemAt(i).contains(estadoVenta)) {
                    cmbEstado.setSelectedIndex(i);
                    break;
                }
            }

            // Recalcular por seguridad
            calcularTotales();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al seleccionar venta: " + e.getMessage());
        }
    }
}

    

    private void editar() {
        int fila = jTable1.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(null, "Seleccione una venta para editar.");
            return;
        }

        int idVenta = Integer.parseInt(jTable1.getValueAt(fila, 0).toString());

        String itemC = cmbCliente.getSelectedItem().toString();
        int idC = Integer.parseInt(itemC.split(" - ")[0]);

        String itemE = cmbEmpleado.getSelectedItem().toString();
        int idE = Integer.parseInt(itemE.split(" - ")[0]);

        String itemV = cmbVehiculo.getSelectedItem().toString();
        int idV = Integer.parseInt(itemV.split(" - ")[0]);

        String itemEV = cmbEstado.getSelectedItem().toString();
        int idEV = Integer.parseInt(itemEV.split(" - ")[0]);

        String sql = "UPDATE ventas SET idcliente=?, idempleado=?, idvehiculo=?, fecha=?, precioventa=?, descuentoventa=?, isv=?, totalventa=?, idestadoventa=? "
                + "WHERE idventa=?";

        Object[] parametros = {
            idC,
            idE,
            idV,
            new java.sql.Date(jDateVenta.getDate().getTime()),
            precioVenta,
            descuento,
            isv,
            total,
            idEV,
            idVenta
        };

        if (ut.ejecutarActualizacion(sql, parametros)) {
            JOptionPane.showMessageDialog(null, "Venta actualizada correctamente.");
            ut.mostrarDatos(sqlse, jTable1, new String[]{
                "ID", "Cliente", "Empleado", "Vehículo", "Fecha", "Precio", "Descuento", "ISV", "Total", "Estado"
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

        cmbCliente = new javax.swing.JComboBox<>();
        cmbEmpleado = new javax.swing.JComboBox<>();
        cmbVehiculo = new javax.swing.JComboBox<>();
        jDateVenta = new com.toedter.calendar.JDateChooser();
        lblPrecio = new javax.swing.JLabel();
        cmbDescuento = new javax.swing.JComboBox<>();
        lblISV = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        cmbEstado = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        txtBuscar = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btnFactura11 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblRegresar = new javax.swing.JLabel();
        btnEditar = new javax.swing.JLabel();
        lblFondoV = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 130, 260, -1));

        cmbEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbEmpleado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbEmpleadoActionPerformed(evt);
            }
        });
        getContentPane().add(cmbEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 180, 260, -1));

        cmbVehiculo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbVehiculo, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 230, 260, -1));
        getContentPane().add(jDateVenta, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 280, 260, -1));

        lblPrecio.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        lblPrecio.setForeground(new java.awt.Color(242, 242, 242));
        lblPrecio.setText("Precio");
        getContentPane().add(lblPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 330, 90, -1));

        cmbDescuento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Promoción", "Tercera Edad", " " }));
        getContentPane().add(cmbDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 380, 180, -1));

        lblISV.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        lblISV.setForeground(new java.awt.Color(242, 242, 242));
        lblISV.setText("ISV");
        getContentPane().add(lblISV, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 430, 90, -1));

        lblTotal.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 18)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(242, 242, 242));
        lblTotal.setText("Total");
        getContentPane().add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 480, 90, -1));

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 520, 190, -1));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 190, 570, 400));

        txtBuscar.setText("Buscar");
        getContentPane().add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 120, 520, -1));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-buscar.png"))); // NOI18N
        jLabel17.setText("jLabel17");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1090, 80, 130, 90));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Ventas");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 20, 150, 70));

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Regi.png"))); // NOI18N
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 570, -1, 90));

        btnFactura11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Factura.png"))); // NOI18N
        btnFactura11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFactura11MouseClicked(evt);
            }
        });
        getContentPane().add(btnFactura11, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 650, -1, 70));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Cliente:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 120, 70, 40));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Empleado:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 170, 100, 40));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Vehículo:");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 220, 90, 40));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Estado de venta:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(134, 510, -1, 40));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Total de venta:");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 470, 140, 40));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Precio de venta:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 320, 150, 40));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Descuento:");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 370, 100, 40));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("ISV:");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 420, 40, 40));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Fecha de venta:");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 270, 140, 40));

        lblRegresar.setFont(new java.awt.Font("Times New Roman", 2, 26)); // NOI18N
        lblRegresar.setForeground(new java.awt.Color(255, 255, 255));
        lblRegresar.setText("Regresar");
        lblRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRegresarMouseClicked(evt);
            }
        });
        getContentPane().add(lblRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 100, -1));

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Editar.png"))); // NOI18N
        btnEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditarMouseClicked(evt);
            }
        });
        getContentPane().add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 570, -1, 90));

        lblFondoV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Ventas1.png"))); // NOI18N
        lblFondoV.setText("jLabel1");
        getContentPane().add(lblFondoV, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1370, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmbEmpleadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbEmpleadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbEmpleadoActionPerformed

    private void lblRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseClicked
        FrmMenu menu = new FrmMenu();   
        menu.setVisible(true);          
        dispose();
    }//GEN-LAST:event_lblRegresarMouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        registrar();
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        seleccionarVenta();
    }//GEN-LAST:event_jTable1MouseClicked

    private void btnFactura11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFactura11MouseClicked
       
       FrmFacturaVenta menu = new FrmFacturaVenta();   
        menu.setVisible(true);          
        dispose();
    }//GEN-LAST:event_btnFactura11MouseClicked

    private void btnEditarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarMouseClicked
        editar();
    }//GEN-LAST:event_btnEditarMouseClicked

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
            java.util.logging.Logger.getLogger(FrmVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmVentas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmVentas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnEditar;
    private javax.swing.JLabel btnFactura11;
    private javax.swing.JComboBox<String> cmbCliente;
    private javax.swing.JComboBox<String> cmbDescuento;
    private javax.swing.JComboBox<String> cmbEmpleado;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbVehiculo;
    private com.toedter.calendar.JDateChooser jDateVenta;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblFondoV;
    private javax.swing.JLabel lblISV;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JLabel lblRegresar;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
