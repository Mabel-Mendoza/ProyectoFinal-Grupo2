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
import java.util.Date;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.sql.Statement;
import java.sql.ResultSet;



/**
 *
 * @author mabel
 */
public class FrmPagos extends javax.swing.JFrame {

    /**
     * Creates new form FrmPagos
     */
    
    
    clsConexion con = new clsConexion();
    Connection cn = con.Sql_Conexion();
    clsUtilidades ut = new clsUtilidades();
    clsCarga car = new clsCarga();

    // Variables para cálculos
    double salarioBase = 0;
    double ihss = 0;
    double rap = 0;
    double totalDeducciones = 0;
    double totalHorasExtras = 0;
    double salarioNeto = 0;
    
    
    
    public FrmPagos() {
        initComponents();
        
        this.setSize(1366, 768); 
         this.setLocationRelativeTo(null); // Centrar pantalla

    // Escalar la imagen al tamaño del JFrame
    ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Pagos1.png"));
    Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
            this.getWidth(),   
            this.getHeight(),  
            Image.SCALE_SMOOTH
    );
    lblFondop.setIcon(new ImageIcon(imagenEscalada));
    
    
    
    Estilos.aplicarEstiloTabla(jTable1);
    
    
    Estilos.aplicarEstiloTextField(txtBuscar);
    
    
    
    Estilos.aplicarEstiloComboBox(cmbEstado);
    Estilos.aplicarEstiloComboBox(cmbPago);
    Estilos.aplicarEstiloComboBox(cmbEmpleado);
    
    
    // Cargar datos en combos
        car.cargarDatos(cmbEmpleado, "empleados", "idempleado", "nombreempleado");
        car.cargarDatos(cmbPago, "metodopago", "idmetodopago", "descripcion");
        car.cargarDatos(cmbEstado, "estadopago", "idestadopago", "descripcion");
        

        // Configurar slider
        SliderHoras.setMinimum(1);
        SliderHoras.setMaximum(10);
        SliderHoras.setValue(1);

        // Evento al seleccionar empleado
        cmbEmpleado.addActionListener(e -> cargarSalarioBase());

        // Evento del slider
        SliderHoras.addChangeListener(e -> calcular());
        
        ut.mostrarDatos(sqlse, jTable1, new String[]{
            "ID Pago", "Nombre", "Apellido", "Fecha Pago",
            "Horas Extras", "Pago por Hora Extra", "Total Horas Extras",
            "IHSS", "RAP", "Total Deducciones", "Salario Neto",
            "Método de Pago", "Estado de Pago"
        });

       
    }
    
    
    String sqlse = "SELECT p.idpago, "
        + "e.nombreempleado, e.apellidoempleado, "
        + "p.fechapago, "
        + "p.horasextras, "
        + "p.pagohorasextras, "
        + "p.totalhorasextras, "
        + "p.deduccionihss, "
        + "p.deduccionrap, "
        + "p.totaldeducciones, "
        + "p.salarioneto, "
        + "m.descripcion AS metodopago, "
        + "es.descripcion AS estadopago "
        + "FROM planillapagos p "
        + "JOIN empleados e ON p.idempleado = e.idempleado "
        + "JOIN metodopago m ON p.idmetodopago = m.idmetodopago "
        + "JOIN estadopago es ON p.idestadopago = es.idestadopago";

    
 

    private void cargarSalarioBase() {
        try {
            String item = cmbEmpleado.getSelectedItem().toString();
            int idEmpleado = Integer.parseInt(item.split(" - ")[0]);

            String sql = "SELECT salariobase FROM empleados WHERE idempleado=" + idEmpleado;
            Statement st = cn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            if (rs.next()) {
                salarioBase = rs.getDouble("salariobase");
                lblSalarioB.setText(String.valueOf(salarioBase));
                calcular();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al obtener salario base: " + e.getMessage());
        }
    }

    private void calcular() {
        int horasExtras = SliderHoras.getValue();
        totalHorasExtras = horasExtras * 150;

        ihss = salarioBase * 0.025;
        rap = salarioBase * 0.015;
        totalDeducciones = ihss + rap;

        salarioNeto = salarioBase - totalDeducciones + totalHorasExtras;

        lblIHSS.setText(String.valueOf(ihss));
        lblRAP.setText(String.valueOf(rap));
        lblTotal.setText(String.valueOf(totalDeducciones));
        lblSalarioN.setText(String.valueOf(salarioNeto));
    }

    private void registrarPago() {
        try {
            String itemE = cmbEmpleado.getSelectedItem().toString();
            int idEmpleado = Integer.parseInt(itemE.split(" - ")[0]);

            String itemM = cmbPago.getSelectedItem().toString();
            int idMetodo = Integer.parseInt(itemM.split(" - ")[0]);

            String itemEs = cmbEstado.getSelectedItem().toString();
            int idEstado = Integer.parseInt(itemEs.split(" - ")[0]);

            Date fecha = jDatePago.getDate();
            java.sql.Date fechaSQL = new java.sql.Date(fecha.getTime());

            String sql = "INSERT INTO planillapagos "
                    + "(idempleado, fechapago, horasextras, pagohorasextras, totalhorasextras, "
                    + "deduccionihss, deduccionrap, totaldeducciones, salarioneto, idmetodopago, idestadopago) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            Object[] parametros = {
                idEmpleado,
                fechaSQL,
                (double) SliderHoras.getValue(),
                150.0,
                totalHorasExtras,
                ihss,
                rap,
                totalDeducciones,
                salarioNeto,
                idMetodo,
                idEstado
            };

            if (ut.ejecutarActualizacion(sql, parametros)) {
                JOptionPane.showMessageDialog(null, "Pago registrado correctamente.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al registrar pago: " + e.getMessage());
        }
        
        
        ut.mostrarDatos(sqlse, jTable1, new String[]{
            "ID Pago", "Nombre", "Apellido", "Fecha Pago",
            "Horas Extras", "Pago por Hora Extra", "Total Horas Extras",
            "IHSS", "RAP", "Total Deducciones", "Salario Neto",
            "Método de Pago", "Estado de Pago"
        });

        
    }
    
    
    
    private void seleccionarPago() {
    int fila = jTable1.getSelectedRow();
    if (fila != -1) {
        try {
            // Empleado (comboBox)
            String nombreEmpleado = jTable1.getValueAt(fila, 1).toString();
            for (int i = 0; i < cmbEmpleado.getItemCount(); i++) {
                if (cmbEmpleado.getItemAt(i).contains(nombreEmpleado)) {
                    cmbEmpleado.setSelectedIndex(i);
                    break;
                }
            }

            // Fecha de pago
            try {
                Date fecha = java.sql.Date.valueOf(jTable1.getValueAt(fila, 3).toString());
                jDatePago.setDate(fecha);
            } catch (Exception e) {
                jDatePago.setDate(null);
            }

            // Horas Extras (slider)
            SliderHoras.setValue((int)(Double.parseDouble(jTable1.getValueAt(fila, 4).toString())));

            // Totales y deducciones
            totalHorasExtras = Double.parseDouble(jTable1.getValueAt(fila, 6).toString());
            ihss = Double.parseDouble(jTable1.getValueAt(fila, 7).toString());
            rap = Double.parseDouble(jTable1.getValueAt(fila, 8).toString());
            totalDeducciones = Double.parseDouble(jTable1.getValueAt(fila, 9).toString());
            salarioNeto = Double.parseDouble(jTable1.getValueAt(fila, 10).toString());

            lblSalarioB.setText(String.valueOf(salarioBase));
            lblIHSS.setText(String.valueOf(ihss));
            lblRAP.setText(String.valueOf(rap));
            lblTotal.setText(String.valueOf(totalDeducciones));
            lblSalarioN.setText(String.valueOf(salarioNeto));

            // Método de Pago (comboBox)
            String metodoPago = jTable1.getValueAt(fila, 11).toString();
            for (int i = 0; i < cmbPago.getItemCount(); i++) {
                if (cmbPago.getItemAt(i).contains(metodoPago)) {
                    cmbPago.setSelectedIndex(i);
                    break;
                }
            }

            // Estado de Pago (comboBox)
            String estadoPago = jTable1.getValueAt(fila, 12).toString();
            for (int i = 0; i < cmbEstado.getItemCount(); i++) {
                if (cmbEstado.getItemAt(i).contains(estadoPago)) {
                    cmbEstado.setSelectedIndex(i);
                    break;
                }
            }

            // Recalcular totales por seguridad
            calcular();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al seleccionar pago: " + e.getMessage());
        }
    }
}

    
    
    
    
    
    
    
    private void editarPago() {
    int filaSeleccionada = jTable1.getSelectedRow();

    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(null, "Seleccione un pago para editar.");
        return;
    }

    
    int idPago = Integer.parseInt(jTable1.getValueAt(filaSeleccionada, 0).toString());

    try {
       
        String itemE = cmbEmpleado.getSelectedItem().toString();
        int idEmpleado = Integer.parseInt(itemE.split(" - ")[0]);

        // Método de pago
        String itemM = cmbPago.getSelectedItem().toString();
        int idMetodo = Integer.parseInt(itemM.split(" - ")[0]);

        // Estado de pago
        String itemEs = cmbEstado.getSelectedItem().toString();
        int idEstado = Integer.parseInt(itemEs.split(" - ")[0]);

        // Fecha de pago
        Date fecha = jDatePago.getDate();
        java.sql.Date fechaSQL = new java.sql.Date(fecha.getTime());

        // Horas extras desde slider
        int horasExtras = SliderHoras.getValue();
        double pagoHorasExtras = 150.0; 
        double totalHorasExtras = horasExtras * pagoHorasExtras;

        // Deducciones (10% cada una)
        double salarioBase = Double.parseDouble(lblSalarioB.getText());
        double ihss = salarioBase * 0.10;
        double rap = salarioBase * 0.10;
        double totalDeducciones = ihss + rap;

        // Salario neto
        double salarioNeto = salarioBase - totalDeducciones + totalHorasExtras;

        
        String sql = "UPDATE planillapagos SET "
                   + "idempleado=?, fechapago=?, horasextras=?, pagohorasextras=?, "
                   + "totalhorasextras=?, deduccionihss=?, deduccionrap=?, "
                   + "totaldeducciones=?, salarioneto=?, idmetodopago=?, idestadopago=? "
                   + "WHERE idpago=?";

        Object[] parametros = {
            idEmpleado,
            fechaSQL,
            horasExtras,
            pagoHorasExtras,
            totalHorasExtras,
            ihss,
            rap,
            totalDeducciones,
            salarioNeto,
            idMetodo,
            idEstado,
            idPago
        };

        
        if (ut.ejecutarActualizacion(sql, parametros)) {
            JOptionPane.showMessageDialog(null, "Pago actualizado correctamente.");

            // Refrescar los datos en la tabla
            ut.mostrarDatos(sqlse, jTable1, new String[]{
                "ID Pago", "Nombre", "Apellido", "Fecha Pago",
                "Horas Extras", "Pago por Hora Extra", "Total Horas Extras",
                "IHSS", "RAP", "Total Deducciones", "Salario Neto",
                "Método de Pago", "Estado de Pago"
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error al actualizar pago: " + e.getMessage());
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

        cmbPago = new javax.swing.JComboBox<>();
        cmbEstado = new javax.swing.JComboBox<>();
        lblSalarioB = new javax.swing.JLabel();
        jDatePago = new com.toedter.calendar.JDateChooser();
        cmbEmpleado = new javax.swing.JComboBox<>();
        lblRAP = new javax.swing.JLabel();
        SliderHoras = new javax.swing.JSlider();
        lblSalarioN = new javax.swing.JLabel();
        lblIHSS = new javax.swing.JLabel();
        lblTotal = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnRegistrar = new javax.swing.JLabel();
        BtnEditar = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        lblLupa = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        lblRegresar = new javax.swing.JLabel();
        lblFondop = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbPago, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 460, 180, -1));

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 500, 180, -1));

        lblSalarioB.setFont(new java.awt.Font("PMingLiU-ExtB", 3, 18)); // NOI18N
        lblSalarioB.setForeground(new java.awt.Color(242, 242, 242));
        lblSalarioB.setText("Salario");
        getContentPane().add(lblSalarioB, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 200, 140, 30));
        getContentPane().add(jDatePago, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 160, 200, -1));

        cmbEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 120, 200, -1));

        lblRAP.setFont(new java.awt.Font("PMingLiU-ExtB", 3, 18)); // NOI18N
        lblRAP.setForeground(new java.awt.Color(242, 242, 242));
        lblRAP.setText("RAP");
        getContentPane().add(lblRAP, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 320, 140, 30));
        getContentPane().add(SliderHoras, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 250, -1, -1));

        lblSalarioN.setFont(new java.awt.Font("PMingLiU-ExtB", 3, 18)); // NOI18N
        lblSalarioN.setForeground(new java.awt.Color(242, 242, 242));
        lblSalarioN.setText("Salario");
        getContentPane().add(lblSalarioN, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 410, 140, 30));

        lblIHSS.setFont(new java.awt.Font("PMingLiU-ExtB", 3, 18)); // NOI18N
        lblIHSS.setForeground(new java.awt.Color(242, 242, 242));
        lblIHSS.setText("IHSS");
        getContentPane().add(lblIHSS, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 280, 140, 30));

        lblTotal.setFont(new java.awt.Font("PMingLiU-ExtB", 3, 18)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(242, 242, 242));
        lblTotal.setText("Total");
        getContentPane().add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 370, 140, 30));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 210, 540, -1));

        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Regi.png"))); // NOI18N
        btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseClicked(evt);
            }
        });
        getContentPane().add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 550, -1, 90));

        BtnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Editar.png"))); // NOI18N
        BtnEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BtnEditarMouseClicked(evt);
            }
        });
        getContentPane().add(BtnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 550, -1, 90));

        txtBuscar.setText("Buscar");
        getContentPane().add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 120, 480, -1));

        lblLupa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-buscar.png"))); // NOI18N
        lblLupa.setText("jLabel17");
        getContentPane().add(lblLupa, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 80, 130, 90));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Pagos");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 20, 120, 70));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Horas Extras:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 240, 120, 40));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Estado de pago:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 490, 150, 40));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Salario base:");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 190, 120, 40));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Fecha de pago:");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 150, 140, 40));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Deducción RAP:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 310, 160, 40));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Total de deducción:");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 360, 180, 40));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Deducción IHSS:");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 270, 160, 40));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Empleado:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 110, 100, 40));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Salario Neto:");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 400, 120, 40));

        jLabel20.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Método de pago:");
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 450, 160, 40));

        lblRegresar.setFont(new java.awt.Font("Times New Roman", 2, 26)); // NOI18N
        lblRegresar.setForeground(new java.awt.Color(255, 255, 255));
        lblRegresar.setText("Regresar");
        lblRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRegresarMouseClicked(evt);
            }
        });
        getContentPane().add(lblRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 100, -1));

        lblFondop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Pagos1.png"))); // NOI18N
        lblFondop.setText("jLabel1");
        getContentPane().add(lblFondop, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1366, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseClicked
        FrmMenu menu = new FrmMenu();   
        menu.setVisible(true);          
        dispose();
    }//GEN-LAST:event_lblRegresarMouseClicked

    private void btnRegistrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseClicked
        registrarPago();
    }//GEN-LAST:event_btnRegistrarMouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        seleccionarPago();
    }//GEN-LAST:event_jTable1MouseClicked

    private void BtnEditarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BtnEditarMouseClicked
        editarPago();
    }//GEN-LAST:event_BtnEditarMouseClicked

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
            java.util.logging.Logger.getLogger(FrmPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPagos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmPagos().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BtnEditar;
    private javax.swing.JSlider SliderHoras;
    private javax.swing.JLabel btnRegistrar;
    private javax.swing.JComboBox<String> cmbEmpleado;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbPago;
    private com.toedter.calendar.JDateChooser jDatePago;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblFondop;
    private javax.swing.JLabel lblIHSS;
    private javax.swing.JLabel lblLupa;
    private javax.swing.JLabel lblRAP;
    private javax.swing.JLabel lblRegresar;
    private javax.swing.JLabel lblSalarioB;
    private javax.swing.JLabel lblSalarioN;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
