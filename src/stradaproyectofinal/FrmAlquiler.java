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

import clases.Estilos;
import java.awt.event.KeyEvent;
import java.text.DecimalFormat;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;



/**
 *
 * @author mabel
 */
public class FrmAlquiler extends javax.swing.JFrame {
    
    
    clsConexion con = new clsConexion();
    Connection cn = con.Sql_Conexion();
    clsUtilidades ut = new clsUtilidades();
    clsCarga car = new clsCarga();

    /**
     * Creates new form FrmAlquiler
     */
    
    
    
    
    private DecimalFormat df = new DecimalFormat("#,##0.00");
    
    double precioPorDia = 0;
    double subtotal = 0;
    double garantia = 0;
    double isv = 0;
    double descuento = 0;
    double total = 0;
    double precioVehiculo = 0;
    
    
    
    public FrmAlquiler() {
        initComponents();
        
        txtdias.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calcularTotales();
            }
        });

        txtGara.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                calcularTotales();
            }
        });
      
        
        
        this.setSize(1366, 768); 
         this.setLocationRelativeTo(null); // Centrar pantalla

         
         
        Estilos.aplicarEstiloComboBox(cmbCliente);
        Estilos.aplicarEstiloComboBox(cmbEmpleado);
        Estilos.aplicarEstiloComboBox(cmbVehiculo);
        Estilos.aplicarEstiloComboBox(cmbEstado);
        Estilos.aplicarEstiloComboBox(cmbDescuento);
        Estilos.aplicarEstiloTextField(txtBuscar);


        
        Estilos.aplicarEstiloTextField(txtGara);
        Estilos.aplicarEstiloTextField(txtdias);
        
        

        Estilos.aplicarEstiloTabla(jTable1);
         
         
         
    // Escalar la imagen al tamaño del JFrame
    ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Alquiler1.png"));
    Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
            this.getWidth(),   
            this.getHeight(),  
            Image.SCALE_SMOOTH
    );
    lblFondoA.setIcon(new ImageIcon(imagenEscalada));
    
    
    
    // Cargar combos desde BD
        car.cargarDatos(cmbCliente, "clientes", "idcliente", "CONCAT(nombrecliente,' ',apellidocliente)");
        car.cargarDatos(cmbEmpleado, "empleados", "idempleado", "CONCAT(nombreempleado,' ',apellidoempleado)");
        car.cargarDatos(cmbVehiculo, "vehiculos", "idvehiculo", "modelo");
        // Ejemplo de cargar el combo estado
car.cargarDatos(cmbEstado, "estadoalquiler", "idestadoalquiler", "CONCAT(idestadoalquiler,' - ',descripcion)");


        
        
     // Cargar descuentos manuales
        cmbDescuento.removeAllItems();
        cmbDescuento.addItem("0 - No aplica");
        cmbDescuento.addItem("1- Promoción");
        cmbDescuento.addItem("2- Tercera edad");    
        
        
        

        // Mostrar registros
        ut.mostrarDatos(sqlse, jTable1, new String[]{
            "ID", "Cliente", "Empleado", "Vehículo", "Fecha Inicio", "Días", "Precio/Día", "Subtotal", "Garantía", "ISV", "Descuento", "Total", "Estado"
        });

        // Eventos
        cmbVehiculo.addActionListener(e -> cargarPrecioVehiculo());
        cmbDescuento.addActionListener(e -> calcularTotales());
        // Recalcular cuando se editen los TextFields (días, garantía)
    addDocumentListenerTo(txtdias);
    addDocumentListenerTo(txtGara);
    addRealtimeUpdate(txtdias);
    addRealtimeUpdate(txtGara);

    // Llamada inicial por si ya hay selección
    calcularTotales();
}


private void addDocumentListenerTo(final javax.swing.JTextField tf) {
    tf.getDocument().addDocumentListener(new DocumentListener() {
        public void insertUpdate(DocumentEvent e) { calcularTotales(); }
        public void removeUpdate(DocumentEvent e) { calcularTotales(); }
        public void changedUpdate(DocumentEvent e) { calcularTotales(); }
        
    });
}








    // Consulta principal
    String sqlse = "SELECT a.idalquiler, " +
            "CONCAT(c.nombrecliente,' ',c.apellidocliente) AS cliente, " +
            "CONCAT(e.nombreempleado,' ',e.apellidoempleado) AS empleado, " +
            "ve.modelo AS vehiculo, a.fechainicio, a.totaldias, a.preciopordia, a.subtotal, " +
            "a.garantia, a.isv, a.descuentoalquiler, a.totalpagar, a.kilometrajeinicial, ea.descripcion " +
            "FROM alquiler a " +
            "JOIN clientes c ON a.idcliente=c.idcliente " +
            "JOIN empleados e ON a.idempleado=e.idempleado " +
            "JOIN vehiculos ve ON a.idvehiculo=ve.idvehiculo " +
            "JOIN estadoalquiler ea ON a.idestadoalquiler=ea.idestadoalquiler";

    // Cargar precio diario según vehículo
    private void cargarPrecioVehiculo() {
    try {
        Object sel = cmbVehiculo.getSelectedItem();
        if (sel == null) return;

        
        String item = sel.toString();
        int idVehiculo = Integer.parseInt(item.split(" - ")[0].trim());

        // Consulta SQL
        String sql = "SELECT precio, kilometraje FROM vehiculos WHERE idvehiculo=" + idVehiculo;
        Statement st = cn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        if (rs.next()) {
            // Leer precio y limpiar comas o símbolos
            String sPrecio = rs.getString("precio").replace(",", "").trim();

            // Convertir correctamente a número
            precioVehiculo = Double.parseDouble(sPrecio);

           
            precioPorDia = precioVehiculo * 0.05;

            // Mostrar formateado
            lblPrecio.setText(String.format("%.2f", precioPorDia));

            
            
            int km = rs.getInt("kilometraje");
            lblkilo.setText(String.valueOf(km));
            
            // Recalcular totales
            calcularTotales();
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error al cargar precio: " + ex.getMessage());
    }
}

    
    
    
    
    
    
    
    
    
    private double parsePercentageFromCombo(Object item) {
    if (item == null) return 0;
    String s = item.toString();
    
    try {
        String[] parts = s.split("-");
        String numPart = parts[0].trim().replace("%", "").replaceAll("[^0-9.]", "");
        if (!numPart.isEmpty()) return Double.parseDouble(numPart);
    } catch (Exception ex) {
        
    }
    s = s.toLowerCase();
    if (s.contains("promoc")) return 10;
    if (s.contains("tercera")) return 15;
    return 0;
}

    
    
    
    private void addRealtimeUpdate(final javax.swing.JTextField textField) {
        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { calcularTotales(); }
            @Override
            public void removeUpdate(DocumentEvent e) { calcularTotales(); }
            @Override
            public void changedUpdate(DocumentEvent e) { calcularTotales(); }
        });
    }



       
    
    
    // Calcular subtotal, ISV, descuento y total
    private void calcularTotales() {
        try {
            int dias = 0;
            if (!txtdias.getText().isEmpty()) {
                dias = Integer.parseInt(txtdias.getText());
            }

            subtotal = precioPorDia * dias;
            garantia = txtGara.getText().isEmpty() ? 0 : Double.parseDouble(txtGara.getText());

            
            
            
String itemD = cmbDescuento.getSelectedItem() != null 
        ? cmbDescuento.getSelectedItem().toString() 
        : "0 - No aplica";

double porcentajeDesc = 0.0;

// Mapear la opción a su porcentaje real
if (itemD.contains("No aplica")) {
    porcentajeDesc = 0;
} else if (itemD.toLowerCase().contains("promoción")) {
    porcentajeDesc = 10;  // 10% para promoción
} else if (itemD.toLowerCase().contains("tercera")) {
    porcentajeDesc = 15;  // 15% para tercera edad
}

// Calcular descuento real
descuento = (subtotal * porcentajeDesc) / 100;


            isv = subtotal * 0.15; // 15% de ISV
            total = subtotal + isv - garantia - descuento;

            // Mostrar valores
            lblSubtotal.setText(String.format("%.2f", subtotal));
            lblISV.setText(String.format("%.2f", isv));
            lblTotal.setText(String.format("%.2f", total));
        } catch (Exception e) {
            
        }
    }

    
    

    
    
    
    
    

    private void registrar() {
      try {  
        String itemC = cmbCliente.getSelectedItem().toString();
        int idC = Integer.parseInt(itemC.split(" - ")[0]);

        String itemE = cmbEmpleado.getSelectedItem().toString();
        int idE = Integer.parseInt(itemE.split(" - ")[0]);

        String itemV = cmbVehiculo.getSelectedItem().toString();
        int idV = Integer.parseInt(itemV.split(" - ")[0]);

        // Registrar alquiler
String itemEA = cmbEstado.getSelectedItem().toString();
int idEA = Integer.parseInt(itemEA.split(" - ")[0].trim());


        String sql = "INSERT INTO alquiler (idcliente, idempleado, idvehiculo, fechainicio, totaldias, preciopordia, subtotal, garantia, isv, descuentoalquiler, totalpagar, kilometrajeinicial, idestadoalquiler) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] parametros = {
            idC,
            idE,
            idV,
            new java.sql.Date(jDateInicio.getDate().getTime()),
            Integer.parseInt(txtdias.getText()),
            precioPorDia,
            subtotal,
            Double.parseDouble(txtGara.getText()),
            isv,
            descuento,
            total,
            Integer.parseInt(lblkilo.getText()),
            idEA
        };

        if (ut.ejecutarActualizacion(sql, parametros)) {
            JOptionPane.showMessageDialog(null, "Alquiler registrado correctamente.");
            ut.mostrarDatos(sqlse, jTable1, new String[]{
                "ID", "Cliente", "Empleado", "Vehículo", "Fecha Inicio", "Días", "Precio/Día", "Subtotal", "Garantía", "ISV", "Descuento", "Total", "Estado"
            });
        }
        }catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error al registrar alquiler: " + ex.getMessage());
        }
    }
    
    
    

    
    
    private void seleccionarAlquiler() {
    int fila = jTable1.getSelectedRow();

    if (fila != -1) {
        // Cliente
        String clienteTabla = jTable1.getValueAt(fila, 1).toString();
        for (int i = 0; i < cmbCliente.getItemCount(); i++) {
            if (cmbCliente.getItemAt(i).contains(clienteTabla)) {
                cmbCliente.setSelectedIndex(i);
                break;
            }
        }

        // Empleado
        String empleadoTabla = jTable1.getValueAt(fila, 2).toString();
        for (int i = 0; i < cmbEmpleado.getItemCount(); i++) {
            if (cmbEmpleado.getItemAt(i).contains(empleadoTabla)) {
                cmbEmpleado.setSelectedIndex(i);
                break;
            }
        }

        // Vehículo
        String vehiculoTabla = jTable1.getValueAt(fila, 3).toString();
        for (int i = 0; i < cmbVehiculo.getItemCount(); i++) {
            if (cmbVehiculo.getItemAt(i).contains(vehiculoTabla)) {
                cmbVehiculo.setSelectedIndex(i);
                break;
            }
        }

        // Fecha inicio
        try {
            java.util.Date fecha = java.sql.Date.valueOf(jTable1.getValueAt(fila, 4).toString());
            jDateInicio.setDate(fecha);
        } catch (Exception e) {
            jDateInicio.setDate(null);
        }

        // Días
        txtdias.setText(jTable1.getValueAt(fila, 5).toString());

        // Precio por día
        lblPrecio.setText(jTable1.getValueAt(fila, 6).toString());

        // Subtotal
        lblSubtotal.setText(jTable1.getValueAt(fila, 7).toString());

        // Garantía
        txtGara.setText(jTable1.getValueAt(fila, 8).toString());

        // ISV
        lblISV.setText(jTable1.getValueAt(fila, 9).toString());

        // Descuento
        double valorDesc = 0;
        try {
            valorDesc = Double.parseDouble(jTable1.getValueAt(fila, 10).toString());
        } catch (Exception e) { }
        switch ((int) valorDesc) {
            case 0:
                cmbDescuento.setSelectedIndex(0); // "0 - No aplica"
                break;
            case 10:
                cmbDescuento.setSelectedIndex(1); // "1- Promoción"
                break;
            case 15:
                cmbDescuento.setSelectedIndex(2); // "2- Tercera edad"
                break;
            default:
                cmbDescuento.setSelectedIndex(0);
                break;
        }

        // Total
        lblTotal.setText(jTable1.getValueAt(fila, 11).toString());

        // Kilometraje inicial
        lblkilo.setText(jTable1.getValueAt(fila, 12).toString());

        // Estado del alquiler
        String estado = jTable1.getValueAt(fila, 13).toString();
        cmbEstado.setSelectedItem(estado);

        // Recalcular totales por seguridad
        calcularTotales();
    }
}




    
    
    
    
   


 
    
    
    private void editarAlquiler() {
    int filaSeleccionada = jTable1.getSelectedRow();

    if (filaSeleccionada == -1) {
        JOptionPane.showMessageDialog(null, "Seleccione un alquiler para editar.");
        return;
    }

    int idAlquiler = Integer.parseInt(jTable1.getValueAt(filaSeleccionada, 0).toString());

    try {
        // Obtener los IDs reales desde los ComboBox
        String itemC = cmbCliente.getSelectedItem().toString();
        int idC = Integer.parseInt(itemC.split(" - ")[0].trim());

        String itemE = cmbEmpleado.getSelectedItem().toString();
        int idE = Integer.parseInt(itemE.split(" - ")[0].trim());

        String itemV = cmbVehiculo.getSelectedItem().toString();
        int idV = Integer.parseInt(itemV.split(" - ")[0].trim());

        // ✅ Obtener id del estado directamente del combo
        String itemEA = cmbEstado.getSelectedItem().toString();
        int idEA = Integer.parseInt(itemEA.split(" - ")[0].trim());

        // --- Consulta para actualizar alquiler ---
        String sql = "UPDATE alquiler SET idcliente=?, idempleado=?, idvehiculo=?, fechainicio=?, totaldias=?, "
                + "preciopordia=?, subtotal=?, garantia=?, isv=?, descuentoalquiler=?, totalpagar=?, kilometrajeinicial=?, idestadoalquiler=? "
                + "WHERE idalquiler=?";

        Object[] parametros = {
            idC,
            idE,
            idV,
            new java.sql.Date(jDateInicio.getDate().getTime()),
            Integer.parseInt(txtdias.getText()),
            precioPorDia,
            subtotal,
            txtGara.getText().isEmpty() ? 0 : Double.parseDouble(txtGara.getText()),
            isv,
            descuento,
            total,
            Integer.parseInt(lblkilo.getText()),
            idEA,
            idAlquiler
        };

        if (ut.ejecutarActualizacion(sql, parametros)) {
            JOptionPane.showMessageDialog(null, "Alquiler actualizado correctamente.");
            ut.mostrarDatos(sqlse, jTable1, new String[]{
                "ID", "Cliente", "Empleado", "Vehículo", "Fecha Inicio", "Días", "Precio/Día", "Subtotal",
                "Garantía", "ISV", "Descuento", "Total", "Estado"
            });
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error al editar alquiler: " + ex.getMessage());
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
        jDateInicio = new com.toedter.calendar.JDateChooser();
        txtdias = new javax.swing.JTextField();
        lblPrecio = new javax.swing.JLabel();
        lblSubtotal = new javax.swing.JLabel();
        txtGara = new javax.swing.JTextField();
        lblISV = new javax.swing.JLabel();
        cmbDescuento = new javax.swing.JComboBox<>();
        cmbEstado = new javax.swing.JComboBox<>();
        lblTotal = new javax.swing.JLabel();
        lblkilo = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        btnRegistrar = new javax.swing.JLabel();
        btnEditar = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        lblRegresar = new javax.swing.JLabel();
        lblFondoA = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        cmbCliente.setFont(new java.awt.Font("PMingLiU-ExtB", 2, 24)); // NOI18N
        cmbCliente.setForeground(new java.awt.Color(255, 255, 255));
        cmbCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbCliente.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));
        getContentPane().add(cmbCliente, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 110, 260, -1));

        cmbEmpleado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbEmpleado, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 150, 260, -1));

        cmbVehiculo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbVehiculo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 190, 260, -1));
        getContentPane().add(jDateInicio, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 270, 210, -1));

        txtdias.setFont(new java.awt.Font("PMingLiU-ExtB", 2, 18)); // NOI18N
        txtdias.setForeground(new java.awt.Color(255, 255, 255));
        txtdias.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(255, 255, 255)));
        txtdias.setSelectionColor(new java.awt.Color(58, 0, 12));
        txtdias.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtdiasFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtdiasFocusLost(evt);
            }
        });
        txtdias.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtdiasActionPerformed(evt);
            }
        });
        getContentPane().add(txtdias, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 310, 220, 30));

        lblPrecio.setFont(new java.awt.Font("PMingLiU-ExtB", 2, 18)); // NOI18N
        lblPrecio.setForeground(new java.awt.Color(242, 242, 242));
        lblPrecio.setText("Precio");
        getContentPane().add(lblPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 230, 90, -1));

        lblSubtotal.setFont(new java.awt.Font("PMingLiU-ExtB", 2, 18)); // NOI18N
        lblSubtotal.setForeground(new java.awt.Color(242, 242, 242));
        lblSubtotal.setText("Subtotal");
        getContentPane().add(lblSubtotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 370, 90, -1));

        txtGara.setFont(new java.awt.Font("PMingLiU-ExtB", 2, 18)); // NOI18N
        txtGara.setForeground(new java.awt.Color(255, 255, 255));
        txtGara.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(255, 255, 255)));
        txtGara.setSelectionColor(new java.awt.Color(58, 0, 12));
        txtGara.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtGaraFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtGaraFocusLost(evt);
            }
        });
        txtGara.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtGaraInputMethodTextChanged(evt);
            }
        });
        txtGara.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGaraActionPerformed(evt);
            }
        });
        getContentPane().add(txtGara, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 470, 240, 30));

        lblISV.setFont(new java.awt.Font("PMingLiU-ExtB", 2, 18)); // NOI18N
        lblISV.setForeground(new java.awt.Color(242, 242, 242));
        lblISV.setText("ISV");
        getContentPane().add(lblISV, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 440, 90, -1));

        cmbDescuento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbDescuento, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 400, 180, -1));

        cmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(cmbEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 520, 180, -1));

        lblTotal.setFont(new java.awt.Font("PMingLiU-ExtB", 2, 18)); // NOI18N
        lblTotal.setForeground(new java.awt.Color(242, 242, 242));
        lblTotal.setText("Total a pagar");
        getContentPane().add(lblTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 610, 120, -1));

        lblkilo.setFont(new java.awt.Font("PMingLiU-ExtB", 2, 18)); // NOI18N
        lblkilo.setForeground(new java.awt.Color(255, 255, 255));
        lblkilo.setText("Kilometraje");
        getContentPane().add(lblkilo, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 570, 150, -1));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 160, 570, 470));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Alquiler");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 190, 70));

        txtBuscar.setText("Buscar");
        getContentPane().add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 120, 440, -1));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-buscar.png"))); // NOI18N
        jLabel17.setText("jLabel17");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1120, 80, 140, 90));

        btnRegistrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Regi.png"))); // NOI18N
        btnRegistrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnRegistrarMouseClicked(evt);
            }
        });
        getContentPane().add(btnRegistrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 590, -1, -1));

        btnEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Editar.png"))); // NOI18N
        btnEditar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditarMouseClicked(evt);
            }
        });
        getContentPane().add(btnEditar, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 590, -1, -1));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Cliente:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 110, 70, 40));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Empleado:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 140, 100, 40));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Vehículo:");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 180, 90, 40));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Fecha de inicio:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 260, 140, 40));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Precio por día:");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 220, 130, 40));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Total de días:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 310, 130, 40));

        jLabel16.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("Sub total:");
        getContentPane().add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 360, 90, 40));

        jLabel18.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Garantía:");
        getContentPane().add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 470, 90, 40));

        jLabel19.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Estado de alquiler:");
        getContentPane().add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 510, 170, 40));

        jLabel20.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Total:");
        getContentPane().add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 600, 60, 40));

        jLabel21.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("ISV:");
        getContentPane().add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 430, 50, 40));

        jLabel22.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Kilometraje incial:");
        getContentPane().add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 560, 170, 40));

        jLabel23.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setText("Descuento:");
        getContentPane().add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 390, 100, 40));

        lblRegresar.setFont(new java.awt.Font("Times New Roman", 2, 26)); // NOI18N
        lblRegresar.setForeground(new java.awt.Color(255, 255, 255));
        lblRegresar.setText("Regresar");
        lblRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRegresarMouseClicked(evt);
            }
        });
        getContentPane().add(lblRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 100, -1));

        lblFondoA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Alquiler1.png"))); // NOI18N
        lblFondoA.setText("jLabel1");
        getContentPane().add(lblFondoA, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1366, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseClicked
        FrmMenu menu = new FrmMenu();   
        menu.setVisible(true);          
        dispose(); 
    }//GEN-LAST:event_lblRegresarMouseClicked

    private void txtdiasFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtdiasFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdiasFocusGained

    private void txtdiasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtdiasFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdiasFocusLost

    private void txtdiasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtdiasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtdiasActionPerformed

    private void btnRegistrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnRegistrarMouseClicked
        registrar();
    }//GEN-LAST:event_btnRegistrarMouseClicked

    private void btnEditarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditarMouseClicked
        editarAlquiler();
    }//GEN-LAST:event_btnEditarMouseClicked

    private void txtGaraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGaraActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGaraActionPerformed

    private void txtGaraInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtGaraInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGaraInputMethodTextChanged

    private void txtGaraFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGaraFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGaraFocusLost

    private void txtGaraFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtGaraFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGaraFocusGained

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        seleccionarAlquiler();
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
            java.util.logging.Logger.getLogger(FrmAlquiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmAlquiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmAlquiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmAlquiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmAlquiler().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel btnEditar;
    private javax.swing.JLabel btnRegistrar;
    private javax.swing.JComboBox<String> cmbCliente;
    private javax.swing.JComboBox<String> cmbDescuento;
    private javax.swing.JComboBox<String> cmbEmpleado;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbVehiculo;
    private com.toedter.calendar.JDateChooser jDateInicio;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblFondoA;
    private javax.swing.JLabel lblISV;
    private javax.swing.JLabel lblPrecio;
    private javax.swing.JLabel lblRegresar;
    private javax.swing.JLabel lblSubtotal;
    private javax.swing.JLabel lblTotal;
    private javax.swing.JLabel lblkilo;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtGara;
    private javax.swing.JTextField txtdias;
    // End of variables declaration//GEN-END:variables
}
