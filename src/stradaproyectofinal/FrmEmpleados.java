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
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author mabel
 */
public class FrmEmpleados extends javax.swing.JFrame {
    
    
    
    clsConexion con = new clsConexion();
    Connection cn = con.Sql_Conexion();
    clsUtilidades ut = new clsUtilidades();
    clsCarga car = new clsCarga();

    /**
     * Creates new form FrmEmpleados
     */
    public FrmEmpleados() {
        initComponents();
        
        
        Estilos.aplicarEstiloTabla(jTable1);
    
        Estilos.aplicarEstiloTextField(txtnombre);
        Estilos.aplicarEstiloTextField(txtapellido1);
        Estilos.aplicarEstiloTextField(txtsalario);
        Estilos.aplicarEstiloTextField(txtCorreo);
        Estilos.aplicarEstiloTextField(txtIdentidad);
        Estilos.aplicarEstiloTextField(txtDireccion);
        Estilos.aplicarEstiloTextField(txtTelefono);
        Estilos.aplicarEstiloTextField(txtBuscar);


        Estilos.aplicarEstiloComboBox(cmbJornada);
        Estilos.aplicarEstiloComboBox(cmbPuesto);
        Estilos.aplicarEstiloComboBox(cmbestado);
        
        this.setSize(1366, 768); 
         this.setLocationRelativeTo(null); // Centrar pantalla

    // Escalar la imagen al tamaño del JFrame
    ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Empleados1.png"));
    Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
            this.getWidth(),   
            this.getHeight(),  
            Image.SCALE_SMOOTH
    );
    lblFondoE.setIcon(new ImageIcon(imagenEscalada));
    
    
    car.cargarDatos(cmbPuesto,"puesto","idpuesto","descripcion");
    car.cargarDatos(cmbJornada,"jornada","idjornada","descripcion");
    car.cargarDatos(cmbestado,"estadoempleado","idestadoempleado","descripcion");
        
        ut.mostrarDatos(sqlse, jTable1, new String[]{
    "ID", "Nombre", "Apellido", "No Identidad", "Fecha Nacimiento", "Sexo", "Correo", "Dirección", "Teléfono", "Salario base", "Puesto", "Jornada", "Estado"
}
);
    }
    
    String sqlse = "SELECT e.idempleado, e.nombreempleado, e.apellidoempleado, e.noidentidad, "
       + "e.fechanacimiento, "
       + "CASE WHEN e.sexo = 'f' THEN 'Femenino' ELSE 'Masculino' END AS sexo, "
       + "e.correo, e.direccion, e.telefono, e.salariobase, "
       + "p.descripcion AS puesto, j.descripcion AS jornada, "
       + "s.descripcion AS estadoempleado "
       + "FROM empleados e "
       + "JOIN puesto p ON e.idpuesto = p.idpuesto "
       + "JOIN jornada j ON e.idjornada = j.idjornada "
       + "JOIN estadoempleado s ON e.idestadoempleado = s.idestadoempleado";


    
    private void registrar() {
        
        String sexo = rbtnfem.isSelected() ? "F" : "M";

        String item = cmbPuesto.getSelectedItem().toString(); 
        int idP = Integer.parseInt(item.split(" - ")[0]); 
        
        String ite = cmbJornada.getSelectedItem().toString();
        int idJ = Integer.parseInt(ite.split(" - ")[0]);
        
        String it = cmbestado.getSelectedItem().toString();
        int idE = Integer.parseInt(it.split(" - ")[0]);
                
        String sql = "INSERT INTO empleados "
   + "(nombreempleado, apellidoempleado, noidentidad, fechanacimiento, sexo, correo, direccion, telefono, salariobase, idpuesto, idjornada, idestadoempleado) "
   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";



            Object[] parametros = {
            txtnombre.getText(),
            txtapellido1.getText(),
            txtIdentidad.getText(),
            new java.sql.Date(jDateNacimiento.getDate().getTime()), // fecha de nacimiento
            sexo,
            txtCorreo.getText(),
            txtDireccion.getText(),
            txtTelefono.getText(),
            txtsalario.getText(),
            idP,
            idJ,
            idE
        };


        // 4. Ejecutar la inserción
            if(ut.ejecutarActualizacion(sql, parametros)) {
                JOptionPane.showMessageDialog(null, "Empleado registrado correctamente.");
                // 5. Limpiar campos
               // limpiarCampos();
                // 6. Actualizar tabla

                ut.mostrarDatos(sqlse, jTable1, 
                    new String[]{"ID", "Nombre", "Apellido", "No Identidad", "Fecha Nacimiento", "Sexo", "Correo", "Dirección", "Teléfono", "Salario base", "Puesto", "Jornada", "Estado"});
            }
    }
    
    private void editar() {
        int filaSeleccionada = jTable1.getSelectedRow();

        if(filaSeleccionada == -1){
            JOptionPane.showMessageDialog(null, "Seleccione un empleado para editar.");
            return;
        }

        int idempleado = Integer.parseInt(jTable1.getValueAt(filaSeleccionada, 0).toString());

        String sexo = rbtnfem.isSelected() ? "F" : "M";

        String item = cmbPuesto.getSelectedItem().toString(); 
        int idP = Integer.parseInt(item.split(" - ")[0]);
        
        String ite = cmbJornada.getSelectedItem().toString();
        int idJ = Integer.parseInt(ite.split(" - ")[0]);
        
        String it = cmbestado.getSelectedItem().toString();
        int idE = Integer.parseInt(it.split(" - ")[0]);

        String sql = "UPDATE empleados SET nombreempleado=?, apellidoempleado=?, noidentidad=?, fechanacimiento=?, sexo=?, correo=?, direccion=?, telefono=?, salariobase=?, idpuesto=?, idjornada=?, idestadoempleado=? "
           + "WHERE idempleado=?";


        Object[] parametros = {
        txtnombre.getText(),
        txtapellido1.getText(),
        txtIdentidad.getText(),
        new java.sql.Date(jDateNacimiento.getDate().getTime()),
        sexo,
        txtCorreo.getText(),
        txtDireccion.getText(),
        txtTelefono.getText(),
        txtsalario.getText(),
        idP,
        idJ,
        idE,
        idempleado
    };


        if(ut.ejecutarActualizacion(sql, parametros)){
            JOptionPane.showMessageDialog(null, "Empleado actualizado correctamente.");
            
            ut.mostrarDatos(sqlse, jTable1, new String[]{
                "ID", "Nombre", "Apellido", "No Identidad", "Fecha Nacimiento", "Sexo", "Correo", "Dirección", "Teléfono", "Salario base", "Puesto", "Jornada", "Estado"
            });
        }
    }
    
    private void seleccion() {
       int fila = jTable1.getSelectedRow();

        if(fila != -1){
            txtnombre.setText(jTable1.getValueAt(fila, 1).toString());
            txtapellido1.setText(jTable1.getValueAt(fila, 2).toString());
            txtIdentidad.setText(jTable1.getValueAt(fila, 3).toString());
            try {
                java.util.Date fecha = java.sql.Date.valueOf(jTable1.getValueAt(fila, 4).toString());
                jDateNacimiento.setDate(fecha);
            } catch (Exception e) {
                jDateNacimiento.setDate(null);
            }


            String sexo = jTable1.getValueAt(fila, 5).toString();
            if(sexo.equals("f")){
                rbtnfem.setSelected(true);
            } else {
                rbtnmas.setSelected(true);
            }

            txtCorreo.setText(jTable1.getValueAt(fila, 6).toString());
            txtDireccion.setText(jTable1.getValueAt(fila, 7).toString());
            txtTelefono.setText(jTable1.getValueAt(fila, 8).toString());
            txtsalario.setText(jTable1.getValueAt(fila, 9).toString());
            

            String descP = jTable1.getValueAt(fila, 10).toString();
            for(int i = 0; i < cmbPuesto.getItemCount(); i++){
                if(cmbPuesto.getItemAt(i).contains(descP)){
                    cmbPuesto.setSelectedIndex(i);
                    break;
                }
            }
            
            String descJ = jTable1.getValueAt(fila, 11).toString();
            for(int i = 0; i < cmbJornada.getItemCount(); i++){
                if(cmbJornada.getItemAt(i).contains(descJ)){
                    cmbJornada.setSelectedIndex(i);
                    break;
                }
            }
            
            String descE = jTable1.getValueAt(fila, 12).toString();
            for(int i = 0; i < cmbestado.getItemCount(); i++){
                if(cmbestado.getItemAt(i).contains(descE)){
                    cmbestado.setSelectedIndex(i);
                    break;
                }
            }
        }
    }
    
    public void generarCredencial(int idEmpleado, String nombre, String apellido, String noidentidad) {
     String usuario = nombre.substring(0,1).toLowerCase() + apellido.toLowerCase();

        String contrasenia = nombre.substring(0,1).toUpperCase() 
                            + apellido.substring(0,1).toUpperCase() 
                            + noidentidad.substring(0, 6);

        String sql = "INSERT INTO credenciales (idempleado, usuario, contrasenia) VALUES (?, ?, ?)";

        Object[] parametros = {idEmpleado, usuario, contrasenia};

        // Ejecutar la inserción usando tu clase de utilidades
        if (ut.ejecutarActualizacion(sql, parametros)) {
            JOptionPane.showMessageDialog(null, "Credencial generada para el empleado:\nUsuario: " 
                                                + usuario + "\nContraseña: " + contrasenia);
        } else {
            JOptionPane.showMessageDialog(null, "Error al generar credencial.");
        }
    }
    
    
    public int obtenerUltimoIdEmpleado() {
    int id = -1;
    String sql = "SELECT idempleado FROM empleados ORDER BY idempleado DESC LIMIT 1";
    try (Connection cn = new clsConexion().Sql_Conexion();
         Statement st = cn.createStatement();
         ResultSet rs = st.executeQuery(sql)) {
        
        if (rs.next()) {
            id = rs.getInt("idempleado");
        }
        
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al obtener último ID: " + e.getMessage());
    }
    return id;
}

    
    
    
    
    
    
    
    
    
    
    
    
    
 

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rbtnfem = new javax.swing.JRadioButton();
        rbtnmas = new javax.swing.JRadioButton();
        txtnombre = new javax.swing.JTextField();
        txtsalario = new javax.swing.JTextField();
        txtIdentidad = new javax.swing.JTextField();
        txtCorreo = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        txtTelefono = new javax.swing.JTextField();
        cmbJornada = new javax.swing.JComboBox<>();
        cmbPuesto = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JLabel();
        btnEditar = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jDateNacimiento = new com.toedter.calendar.JDateChooser();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        lblRegresar = new javax.swing.JLabel();
        cmbestado = new javax.swing.JComboBox<>();
        chkcred = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txtapellido1 = new javax.swing.JTextField();
        lblFondoE = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rbtnfem.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 24)); // NOI18N
        rbtnfem.setForeground(new java.awt.Color(242, 242, 242));
        rbtnfem.setText("Femenino");
        getContentPane().add(rbtnfem, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 280, -1, -1));

        rbtnmas.setFont(new java.awt.Font("PMingLiU-ExtB", 1, 24)); // NOI18N
        rbtnmas.setForeground(new java.awt.Color(242, 242, 242));
        rbtnmas.setText("Masculino");
        getContentPane().add(rbtnmas, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 280, -1, -1));
        getContentPane().add(txtnombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 90, 200, -1));
        getContentPane().add(txtsalario, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 460, 200, -1));

        txtIdentidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdentidadActionPerformed(evt);
            }
        });
        getContentPane().add(txtIdentidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 190, 290, -1));
        getContentPane().add(txtCorreo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 320, 250, -1));

        txtDireccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionActionPerformed(evt);
            }
        });
        getContentPane().add(txtDireccion, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 370, 290, -1));
        getContentPane().add(txtTelefono, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 420, 200, -1));

        cmbJornada.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbJornada, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 560, 230, -1));

        cmbPuesto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbPuesto, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 510, 230, -1));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(792, 200, 480, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Empleados");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 20, 240, 70));

        txtBuscar.setText("Buscar");
        getContentPane().add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(790, 140, 430, -1));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-buscar.png"))); // NOI18N
        jLabel17.setText("jLabel17");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 110, 130, 90));

        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Regi.png"))); // NOI18N
        btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseClicked(evt);
            }
        });
        getContentPane().add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 640, -1, 90));

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Editar.png"))); // NOI18N
        btnEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditarMouseClicked(evt);
            }
        });
        getContentPane().add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 640, -1, 90));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Nombre:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 80, 80, 40));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Apellido:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 130, 90, 40));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("No. de identidad:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 180, 160, 40));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Fecha de nacimiento:");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 230, 190, 40));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Género:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 270, 80, 40));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Correo:");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 310, 70, 40));

        jLabel15.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Dirección:");
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 360, 100, 40));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Puesto:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 500, 70, 40));
        getContentPane().add(jDateNacimiento, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 240, 210, -1));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Estado:");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 600, 70, 40));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Salario base:");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 450, 120, 40));

        lblRegresar.setFont(new java.awt.Font("Times New Roman", 2, 26)); // NOI18N
        lblRegresar.setForeground(new java.awt.Color(255, 255, 255));
        lblRegresar.setText("Regresar");
        lblRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRegresarMouseClicked(evt);
            }
        });
        getContentPane().add(lblRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 100, -1));

        getContentPane().add(cmbestado, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 600, 230, 30));

        chkcred.setForeground(new java.awt.Color(255, 255, 255));
        chkcred.setText("Crear credencial");
        chkcred.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkcredActionPerformed(evt);
            }
        });
        getContentPane().add(chkcred, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 650, -1, -1));

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Crear credencial:");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(910, 640, 120, 40));

        jLabel20.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Jornada:");
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 550, 80, 40));

        jLabel21.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Teléfono:");
        getContentPane().add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 410, 90, 40));
        getContentPane().add(txtapellido1, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 140, 200, -1));

        lblFondoE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Empleados1.png"))); // NOI18N
        lblFondoE.setText("jLabel1");
        getContentPane().add(lblFondoE, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1365, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdentidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdentidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdentidadActionPerformed

    private void txtDireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionActionPerformed

    private void lblRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseClicked
        FrmMenu menu = new FrmMenu();   
        menu.setVisible(true);          
        dispose();
    }//GEN-LAST:event_lblRegresarMouseClicked

    private void chkcredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkcredActionPerformed
        // TODO add your handling code here:
        int idEmpleado = obtenerUltimoIdEmpleado(); // obtiene el último id insertado
        String nombre = txtnombre.getText();
        String apellido = txtsalario.getText();
        String noIdentidad = txtIdentidad.getText(); // aquí está el noidentidad

        generarCredencial(idEmpleado, nombre, apellido, noIdentidad);
    }//GEN-LAST:event_chkcredActionPerformed

    private void btnRegistrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseClicked
        registrar();
    }//GEN-LAST:event_btnRegistrarMouseClicked

    private void btnEditarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarMouseClicked
        editar();
    }//GEN-LAST:event_btnEditarMouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        seleccion();
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
            java.util.logging.Logger.getLogger(FrmEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmEmpleados.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmEmpleados().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnEditar;
    private javax.swing.JLabel btnRegistrar;
    private javax.swing.JCheckBox chkcred;
    private javax.swing.JComboBox<String> cmbJornada;
    private javax.swing.JComboBox<String> cmbPuesto;
    private javax.swing.JComboBox<String> cmbestado;
    private com.toedter.calendar.JDateChooser jDateNacimiento;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblFondoE;
    private javax.swing.JLabel lblRegresar;
    private javax.swing.JRadioButton rbtnfem;
    private javax.swing.JRadioButton rbtnmas;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCorreo;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtIdentidad;
    private javax.swing.JTextField txtTelefono;
    private javax.swing.JTextField txtapellido1;
    private javax.swing.JTextField txtnombre;
    private javax.swing.JTextField txtsalario;
    // End of variables declaration//GEN-END:variables
}
