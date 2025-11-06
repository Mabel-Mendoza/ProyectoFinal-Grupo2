package stradaproyectofinal;

import clases.Estilos;
import clases.clsCarga;
import clases.clsCliente;
import clases.clsConexion;
import clases.clsUtilidades;
import clases.User;                     // <-- SESIÓN
import java.awt.Image;
import javax.swing.ImageIcon;
import java.sql.Connection;
import javax.swing.JOptionPane;

/**
 * @author mabel
 */
public class FrmClientes extends javax.swing.JFrame {

    // ===== Sesión del usuario =====
    private final User currentUser;

    clsConexion con = new clsConexion();
    Connection cn = con.Sql_Conexion();
    clsUtilidades ut = new clsUtilidades();
    clsCarga car = new clsCarga();
    clsCliente cli = new clsCliente();

    // --- Constructor SIN usuario (evita usarlo en flujo real) ---
    public FrmClientes() {
        this(null);
    }

    // --- Constructor CON usuario (usar siempre desde el menú) ---
    public FrmClientes(User user) {
        this.currentUser = user;
        initComponents();
        
        // ======= VALIDACIONES EN TIEMPO REAL =======

// Nombre: solo letras
txtNombre.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
            evt.consume();
        }
    }
});

// Apellido: solo letras
TxtApellido.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isLetter(c) && !Character.isWhitespace(c)) {
            evt.consume();
        }
    }
});

// No Identidad: solo números, máximo 13
txtIdentidad.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
            return;
        }
        if (txtIdentidad.getText().length() >= 13) {
            evt.consume();
        }
    }
});

// Teléfono: solo números, máximo 8
txtTelefono.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isDigit(c)) {
            evt.consume();
            return;
        }
        if (txtTelefono.getText().length() >= 8) {
            evt.consume();
        }
    }
});

// Correo: siempre en minúsculas
txtCorreo.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        evt.setKeyChar(Character.toLowerCase(c));
    }
});

        
        
        
        

        if (currentUser != null) {
            setTitle("Clientes - Sesión: " + currentUser.getDisplayName() + " (" + currentUser.getRole() + ")");
        }

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

        // Fondo
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Clientes1.png"));
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
                this.getWidth(),
                this.getHeight(),
                Image.SCALE_SMOOTH
        );
        lblFondoC.setIcon(new ImageIcon(imagenEscalada));

        // Cargar combos y tabla
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
            if (!validarCampos()) {
        return;
    }
        
        try {
            String sexo = rbtfem.isSelected() ? "F" : "M";

            String item = cmbsseg.getSelectedItem().toString();
            int idSeg = Integer.parseInt(item.split(" - ")[0].trim());

            String sql = "INSERT INTO clientes "
                       + "(nombrecliente, apellidocliente, noidentidad, fechanacimiento, sexo, correo, direccion, telefono, idsegmentacion) "
                       + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

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

            if (ut.ejecutarActualizacion(sql, parametros)) {
                JOptionPane.showMessageDialog(this, "Cliente registrado correctamente.");
                ut.mostrarDatos(sqlse, jTable1,
                    new String[]{"ID", "Nombre", "Apellido", "No Identidad", "Fecha de nacimiento", "Sexo", "Correo", "Dirección", "Teléfono", "Segmentación"});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editar() {
        if (!validarCampos()) {
            return;
        }

        int filaSeleccionada = jTable1.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para editar.");
            return;
        }

        int idcliente = Integer.parseInt(jTable1.getValueAt(filaSeleccionada, 0).toString());
        try {
            String sexo = rbtfem.isSelected() ? "F" : "M";
            String item = cmbsseg.getSelectedItem().toString();
            int idSeg = Integer.parseInt(item.split(" - ")[0].trim());

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

            if (ut.ejecutarActualizacion(sql, parametros)) {
                JOptionPane.showMessageDialog(this, "Cliente actualizado correctamente.");
                ut.mostrarDatos(sqlse, jTable1, new String[]{
                    "ID", "Nombre", "Apellido", "No Identidad", "Edad", "Sexo", "Correo", "Dirección", "Teléfono", "Segmentación"
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al actualizar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void seleccion() {
        int fila = jTable1.getSelectedRow();
        if (fila == -1) return;

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
        if ("F".equalsIgnoreCase(sexo)) {
            rbtfem.setSelected(true);
            rbtnmas.setSelected(false);
        } else {
            rbtnmas.setSelected(true);
            rbtfem.setSelected(false);
        }

        txtCorreo.setText(jTable1.getValueAt(fila, 6).toString());
        txtDireccion.setText(jTable1.getValueAt(fila, 7).toString());
        txtTelefono.setText(jTable1.getValueAt(fila, 8).toString());

        String descSeg = jTable1.getValueAt(fila, 9).toString();
        for (int i = 0; i < cmbsseg.getItemCount(); i++) {
            if (cmbsseg.getItemAt(i).contains(descSeg)) {
                cmbsseg.setSelectedIndex(i);
                break;
            }
        }
    }
    
    
    
    // ======= VALIDACIONES GENERALES =======
private boolean validarCampos() {
    // --- Campos vacíos ---
    if (txtNombre.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese el nombre del cliente.");
        txtNombre.requestFocus();
        return false;
    }

    if (TxtApellido.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese el apellido del cliente.");
        TxtApellido.requestFocus();
        return false;
    }

    if (txtIdentidad.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese el número de identidad.");
        txtIdentidad.requestFocus();
        return false;
    }

    if (txtIdentidad.getText().length() != 13) {
        JOptionPane.showMessageDialog(this, "El número de identidad debe tener exactamente 13 dígitos.");
        txtIdentidad.requestFocus();
        return false;
    }

    if (jDatenacimiento.getDate() == null) {
        JOptionPane.showMessageDialog(this, "Seleccione una fecha de nacimiento.");
        jDatenacimiento.requestFocus();
        return false;
    }

    // Validar edad mínima 18 años
    java.util.Calendar hoy = java.util.Calendar.getInstance();
    java.util.Calendar fechaNac = java.util.Calendar.getInstance();
    fechaNac.setTime(jDatenacimiento.getDate());
    int edad = hoy.get(java.util.Calendar.YEAR) - fechaNac.get(java.util.Calendar.YEAR);
    if (hoy.get(java.util.Calendar.DAY_OF_YEAR) < fechaNac.get(java.util.Calendar.DAY_OF_YEAR)) {
        edad--;
    }
    if (edad < 18) {
        JOptionPane.showMessageDialog(this, "El cliente debe ser mayor de 18 años.");
        jDatenacimiento.requestFocus();
        return false;
    }

    // Validar sexo
    if (!rbtfem.isSelected() && !rbtnmas.isSelected()) {
        JOptionPane.showMessageDialog(this, "Seleccione el sexo del cliente.");
        return false;
    }

    if (txtCorreo.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese el correo electrónico del cliente.");
        txtCorreo.requestFocus();
        return false;
    }

    // Validar formato de correo
    String correo = txtCorreo.getText().trim();
    if (!correo.matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$")) {
        JOptionPane.showMessageDialog(this, "Ingrese un correo electrónico válido (ejemplo: usuario@gmail.com).");
        txtCorreo.requestFocus();
        return false;
    }

    if (txtDireccion.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese la dirección del cliente.");
        txtDireccion.requestFocus();
        return false;
    }

    if (txtTelefono.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese el número de teléfono del cliente.");
        txtTelefono.requestFocus();
        return false;
    }

    if (txtTelefono.getText().length() != 8) {
        JOptionPane.showMessageDialog(this, "El número de teléfono debe tener exactamente 8 dígitos.");
        txtTelefono.requestFocus();
        return false;
    }

    // Validar ComboBox Segmentación
    if (cmbsseg.getSelectedIndex() == 0 ||
        cmbsseg.getSelectedItem().toString().toLowerCase().contains("seleccione")) {
        JOptionPane.showMessageDialog(this, "Seleccione una segmentación para el cliente.");
        cmbsseg.requestFocus();
        return false;
    }

    return true; // ✅ Todo bien
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
        jScrollPane2 = new javax.swing.JScrollPane();

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 190, 660, 440));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Clientes");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 190, 70));

        txtBuscar.setText("Buscar");
        getContentPane().add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 140, 550, -1));

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
        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 190, 660, 430));

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
        FrmMenu menu = new FrmMenu(currentUser);   
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
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FrmClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() { new FrmClientes().setVisible(true); }
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
    private javax.swing.JScrollPane jScrollPane2;
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
