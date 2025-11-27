/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package stradaproyectofinal;

import clases.Estilos;
import clases.User;
import clases.clsCarga;
import clases.clsConexion;
import clases.clsEstadoV;
import clases.clsProc;
import clases.clsUtilidades;
import java.sql.Connection;
import clases.clsUtilidades;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.sql.Connection;
import javax.swing.JOptionPane;
import com.toedter.calendar.JDateChooser;
import java.text.DecimalFormat;
import java.sql.*;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;
import javax.swing.*;


/**
 *
 * @author mabel
 */
public class FrmDevolucion extends javax.swing.JFrame {
    
    private final User currentUser;
    
    clsConexion con = new clsConexion();
    Connection cn = con.Sql_Conexion();
    clsUtilidades ut = new clsUtilidades();
    clsCarga car = new clsCarga();
    clsProc pro = new clsProc();
    
    
    
    ButtonGroup grupoDanio = new ButtonGroup(); 
    java.sql.Date fechaMinimaPermitida = null;
    
    int idve;
    
    public FrmDevolucion(User user, int idev) {
        
        this.currentUser = user;

        if (currentUser != null) {
            setTitle("Clientes - Sesión: " + currentUser.getDisplayName() + " (" + currentUser.getRole() + ")");
        }
        
        initComponents();
        
        this.idve = idev;
        
        btnCompro.setEnabled(false);
        
        setResizable(false); 
        
        Estilos.aplicarEstiloDateChooser(jDateFinal);
        
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
    public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();
        if (!Character.isDigit(c) || txtBuscar.getText().length() >= 13) evt.consume();
    }
}); 
        
        
        Estilos.aplicarPlaceholder(txtBuscar, "Buscar por ID de devolución");
        
        
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

    clsEstadoV est = new clsEstadoV();
    int estado = est.obtenerEstadoVehiculo(cn, idve);
    
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
    
    public int obtenerUltimoIdVenta() {
        int idAlqui = 0;
        String sql = "SELECT MAX(iddevolucion) AS id FROM devolucionalquiler";

        try (Connection cn = new clsConexion().Sql_Conexion();
                
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            if (rs.next()) {
                idAlqui = rs.getInt("id");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al obtener último ID de devolucion: " + e.getMessage());
        }

        return idAlqui;
    }
    
    private int idAlqui;

    
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
    
    
    private void actualizarEstadoVehiculo(String idAlquiler, int nuevoEstado) {
    String sql = "UPDATE vehiculos " +
                 "SET idestadovehiculo=? " +
                 "WHERE idvehiculo = (SELECT idvehiculo FROM alquiler WHERE idalquiler=?)";

    try (PreparedStatement ps = cn.prepareStatement(sql)) {
        ps.setInt(1, nuevoEstado);
        ps.setString(2, idAlquiler);
        ps.executeUpdate();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Error al actualizar estado del vehículo: " + e.getMessage());
    }
}

    
    private void registrarDevolucion() {
        if (!validarDevolucion()) return; 

        String idAlquiler = cmbId.getSelectedItem().toString();
        java.sql.Date fechaFinal = new java.sql.Date(jDateFinal.getDate().getTime());
        int kilometrajeFinal = Integer.parseInt(txtKiloF.getText());
        boolean hayDano = rbtSi.isSelected();
        String descripcion = hayDano ? txtAreaDano.getText().trim() : null;
        Double cargoExtra = null;

        if (hayDano) {
            try (PreparedStatement ps = cn.prepareStatement(
                "SELECT a.idvehiculo, v.precio "
                        + "FROM alquiler a "
                        + "JOIN vehiculos v ON a.idvehiculo = v.idvehiculo "
                        + "WHERE a.idalquiler = ?")) {
                ps.setString(1, idAlquiler);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double precio = rs.getDouble("precio");
                        cargoExtra = precio * 0.05;
                        lblcargo.setText(String.format("%.2f", cargoExtra));
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al calcular cargo extra: " + e.getMessage());
                return;
            }
        }

        String sqlInsert = "INSERT INTO devolucionalquiler (idalquiler, fechafinal, kilometrajefinal, dano, cargoextra) VALUES (?, ?, ?, ?, ?)";
        Object[] parametros = { idAlquiler, fechaFinal, kilometrajeFinal, descripcion, cargoExtra };

        if (ut.ejecutarActualizacion(sqlInsert, parametros)) {
            JOptionPane.showMessageDialog(null, "Devolución registrada correctamente.");
            actualizarKilometrajeVehiculo(idAlquiler, kilometrajeFinal);
            actualizarEstadoVehiculo(idAlquiler, 1);
            
            
            ut.mostrarDatos(sqlMostrar, jTable1, new String[]{"ID", "ID Alquiler", "Fecha Final", "Kilometraje Final", "Daño", "Cargo Extra"});
            idAlqui = obtenerUltimoIdVenta();
                btnCompro.setEnabled(true);
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
                
                txtAreaDano.setText(danoObj.toString());
                jScrollPane2.setVisible(true); // <--- ESTA ES LA CLAVE
                lblDes.setVisible(true);
                lblCargoEx.setVisible(true);
                lblcargo.setVisible(true);

            } else {
                rbtNo.setSelected(true);

                jScrollPane2.setVisible(false); 
                lblDes.setVisible(false);
                lblCargoEx.setVisible(false);
                lblcargo.setVisible(false);

                txtAreaDano.setText("");
            }

            // Refrescar el layout
            jScrollPane1.revalidate();
            jScrollPane1.repaint();

            // Cargo extra
            Object cargoObj = jTable1.getValueAt(fila, 5);
            if (cargoObj != null) {
                lblcargo.setText("Cargo extra: L. " + cargoObj.toString());
            } else {
                lblcargo.setText("");
            }
            
            btnCompro.setEnabled(true);

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

        // Validación antes de editar
        if (!validarEdicionDevolucion()) return;

        // Obtener el id de la devolución
        int idDevolucion = Integer.parseInt(jTable1.getValueAt(filaSeleccionada, 0).toString());

        String idAlquiler = cmbId.getSelectedItem().toString();
        java.sql.Date fechaFinal = new java.sql.Date(jDateFinal.getDate().getTime());
        int kilometrajeFinal = Integer.parseInt(txtKiloF.getText());
        boolean hayDano = rbtSi.isSelected();
        String descripcion = hayDano ? txtAreaDano.getText().trim() : null;
        Double cargoExtra = null;

        // Calcular cargo extra si hay daño
        if (hayDano) {
            try (PreparedStatement ps = cn.prepareStatement(
                    "SELECT v.precio FROM alquiler a JOIN vehiculos v ON a.idvehiculo = v.idvehiculo WHERE a.idalquiler = ?")) {
                ps.setString(1, idAlquiler);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        double precio = rs.getDouble("precio");
                        cargoExtra = precio * 0.05;
                    }
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error al calcular cargo extra: " + e.getMessage());
                return;
            }
        }

        // Actualizar en la base de datos
        String sql = "UPDATE devolucionalquiler SET idalquiler=?, fechafinal=?, kilometrajefinal=?, dano=?, cargoextra=? WHERE iddevolucion=?";
        Object[] parametros = { idAlquiler, fechaFinal, kilometrajeFinal, descripcion, cargoExtra, idDevolucion };

        if (ut.ejecutarActualizacion(sql, parametros)) {
            JOptionPane.showMessageDialog(null, "Devolución actualizada correctamente.");
            ut.mostrarDatos(sqlMostrar, jTable1, new String[]{"ID", "ID Alquiler", "Fecha Final", "Kilometraje Final", "Daño", "Cargo Extra"});
        }
    }
   
   private boolean validarEdicionDevolucion() {
       
        String kiloFinal = txtKiloF.getText().trim();
        if (kiloFinal.isEmpty() || !kiloFinal.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Ingrese un kilometraje final válido (solo números).");
            txtKiloF.requestFocus();
            return false;
        }

        int kilo = Integer.parseInt(kiloFinal);
        int kiloInicial = Integer.parseInt(txtKiloF.getText().trim()); 
        if (kilo < kiloInicial) {
            JOptionPane.showMessageDialog(null, "El kilometraje final no puede ser menor que el inicial.");
            txtKiloF.requestFocus();
            return false;
        }

        if (rbtSi.isSelected()) {
            String descripcion = txtAreaDano.getText().trim();
            if (descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe describir el daño.");
                txtAreaDano.requestFocus();
                return false;
            }
        }

        return true; 
    }



    private boolean validarDevolucion() {
        
        if (cmbId.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un alquiler.");
            cmbId.requestFocus();
            return false;
        }

        if (jDateFinal.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Debe seleccionar una fecha de devolución.");
            jDateFinal.requestFocus();
            return false;
        }

        java.util.Date fechaSeleccionada = jDateFinal.getDate();

        if (fechaMinimaPermitida != null && fechaSeleccionada.before(fechaMinimaPermitida)) {
            JOptionPane.showMessageDialog(null, 
                "La fecha de devolución no puede ser antes del " + fechaMinimaPermitida.toString());
            jDateFinal.requestFocus();
            return false;
        }

        String kiloFinal = txtKiloF.getText().trim();
        if (kiloFinal.isEmpty() || !kiloFinal.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Ingrese un kilometraje final válido (solo números).");
            txtKiloF.requestFocus();
            return false;
        }

        if (rbtSi.isSelected()) {
            String descripcion = txtAreaDano.getText().trim();
            if (descripcion.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Debe describir el daño.");
                txtAreaDano.requestFocus();
                return false;
            }
        }

        return true; 
    }
    
    
    private void buscarDevolucionPorId() {
    String textoBusqueda = txtBuscar.getText().trim();
    String sqlBuscar;

    if (textoBusqueda.isEmpty()) {
        sqlBuscar = sqlMostrar; // Muestra todas las devoluciones si no hay texto
    } else {
        sqlBuscar = sqlMostrar + " WHERE iddevolucion LIKE '%" + textoBusqueda + "%'";
    }

    ut.mostrarDatos(sqlBuscar, jTable1, new String[]{
        "ID", "ID Alquiler", "Fecha Final", "Kilometraje Final", "Daño", "Cargo Extra"
    });
}

   
    private void generarComprobante() {
    if (cmbId.getSelectedItem() == null) {
        JOptionPane.showMessageDialog(this, "Seleccione un alquiler primero.");
        return;
    }

    String idAlquiler = cmbId.getSelectedItem().toString();
    String fechaEntrega = jDateFinal.getDate() != null ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(jDateFinal.getDate()) : "";
    String kilometrajeFinal = txtKiloF.getText();
    String descripcionDano = rbtSi.isSelected() ? txtAreaDano.getText().trim() : "N/A";
    String cargoExtra = lblcargo.getText().replace("Cargo extra: L. ", "").trim();

    String rutaPDF = "Comprobante_Devolucion_" + idAlquiler + ".pdf";

    try {
        com.itextpdf.text.Document documento = new com.itextpdf.text.Document();
        com.itextpdf.text.pdf.PdfWriter.getInstance(documento, new java.io.FileOutputStream(rutaPDF));
        documento.open();

        // Fuentes
        com.itextpdf.text.Font titulo = com.itextpdf.text.FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.BLACK);
        com.itextpdf.text.Font subTitulo = com.itextpdf.text.FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
        com.itextpdf.text.Font normal = com.itextpdf.text.FontFactory.getFont(FontFactory.COURIER, 12);

        // Encabezado
        Paragraph linea = new Paragraph("========================================", 
                FontFactory.getFont(FontFactory.COURIER_BOLD, 12));
        linea.setAlignment(Element.ALIGN_CENTER);
        documento.add(linea);

        Paragraph encabezado = new Paragraph("STRADA - Alquiler de Vehículos", titulo);
        encabezado.setAlignment(Element.ALIGN_CENTER);
        documento.add(encabezado);

        Paragraph comprobante = new Paragraph("*** COMPROBANTE DE DEVOLUCIÓN ***", subTitulo);
        comprobante.setAlignment(Element.ALIGN_CENTER);
        documento.add(comprobante);
        documento.add(linea);
        documento.add(new Paragraph(" "));

        // Datos principales
        documento.add(new Paragraph(String.format("ID Alquiler       : %s", idAlquiler), normal));
        documento.add(new Paragraph(String.format("Fecha de entrega  : %s", fechaEntrega), normal));
        documento.add(new Paragraph(String.format("Kilometraje final : %s km", kilometrajeFinal), normal));
        documento.add(new Paragraph(String.format("Descripción daño  : %s", descripcionDano), normal));
        
        
        
        double cargoDouble = 0;
try {
    cargoDouble = Double.parseDouble(cargoExtra);
} catch (NumberFormatException e) {
    cargoDouble = 0; // en caso de que esté vacío o inválido
}

NumberFormat formato = NumberFormat.getNumberInstance(Locale.US);
formato.setMinimumFractionDigits(2);
formato.setMaximumFractionDigits(2);

String cargoFormateado = formato.format(cargoDouble);

// Añadir al PDF
documento.add(new Paragraph(String.format("Cargo extra       : L. %s", cargoFormateado),
        FontFactory.getFont(FontFactory.COURIER, 12)));
        
        documento.add(new Paragraph(" "));

        // Línea separadora
        documento.add(linea);
        documento.add(new Paragraph(" "));
        
        // Mensaje de cierre
        Paragraph mensaje = new Paragraph("¡Gracias por su confianza en Strada!", subTitulo);
        mensaje.setAlignment(Element.ALIGN_CENTER);
        documento.add(mensaje);

        documento.close();

        // Abrir PDF automáticamente
        if (java.awt.Desktop.isDesktopSupported()) {
            java.awt.Desktop.getDesktop().browse(new java.io.File(rutaPDF).toURI());
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al generar comprobante: " + e.getMessage());
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
        buttonGroup2 = new javax.swing.ButtonGroup();
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
        btnCompro = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaDano = new javax.swing.JTextArea();
        lblCargoEx = new javax.swing.JLabel();
        lblFondo = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Devolución");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 30, 230, 70));

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });
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

        btnCompro.setBackground(new java.awt.Color(0, 0, 0));
        btnCompro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Compro.png"))); // NOI18N
        btnCompro.setBorder(null);
        btnCompro.setContentAreaFilled(false);
        btnCompro.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnCompro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnComproMouseClicked(evt);
            }
        });
        btnCompro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnComproActionPerformed(evt);
            }
        });
        getContentPane().add(btnCompro, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 630, 190, 110));

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
        getContentPane().add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 280, 320, 270));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseClicked
        FrmAlquiler alq = new FrmAlquiler(currentUser);
        alq.setVisible(true);
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

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        buscarDevolucionPorId();
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void btnComproActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnComproActionPerformed
      generarComprobante();
    }//GEN-LAST:event_btnComproActionPerformed

    private void btnComproMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnComproMouseClicked
        generarComprobante();
    }//GEN-LAST:event_btnComproMouseClicked

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
    private javax.swing.JButton btnCompro;
    private javax.swing.JLabel btnEditar;
    private javax.swing.JLabel btnRegistrar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
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
    private javax.swing.JScrollPane jScrollPane3;
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
