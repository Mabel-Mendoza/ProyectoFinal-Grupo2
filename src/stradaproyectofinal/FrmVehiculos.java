package stradaproyectofinal;

import clases.Estilos;
import clases.clsCarga;
import clases.clsConexion;
import clases.clsUtilidades;
import clases.User;                  // <-- IMPORTANTE
import com.mysql.jdbc.PreparedStatement;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class FrmVehiculos extends javax.swing.JFrame {
    
    // ==== NUEVO: guardamos quién está logueado ====
    private final User currentUser;

    clsConexion con = new clsConexion();
    Connection cn = con.Sql_Conexion();
    clsUtilidades ut = new clsUtilidades();
    clsCarga car = new clsCarga();

    // ==== Constructor SIN usuario (si alguien lo usa por error, no hay sesión) ====
    public FrmVehiculos() {
        this(null);
    }

    // ==== Constructor CON usuario (el recomendado desde el menú) ====
    public FrmVehiculos(User user) {
        this.currentUser = user;           // guardamos la sesión
        initComponents();
        
        setResizable(false); 
       Estilos.aplicarPlaceholder(txtBuscar, "Buscar el vehículo por placa");


        
        
      // ======= Validación para campo Precio =======
      
      txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();

        // Limitar a 7 caracteres
        if (txtBuscar.getText().length() >= 7) {
            evt.consume(); // No deja escribir más
            return;
        }

        // Convertir a mayúsculas
        if (Character.isLetter(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }

        // Permitir solo letras, números y guiones (opcional)
        if (!Character.isLetterOrDigit(c) && c != '-' && c != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }
    }
});
      
      
      
      
      
      
      
TxtPrecio.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();

        // Solo permitir números y un punto decimal
        if (!Character.isDigit(c) && c != '.' && c != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
            return;
        }

        // Evitar más de un punto decimal
        if (c == '.' && TxtPrecio.getText().contains(".")) {
            evt.consume();
        }

        // Evitar valores negativos o signos
        if (c == '-' || c == '+') {
            evt.consume();
        }

        // Limitar la longitud (por ejemplo 10 dígitos)
        if (TxtPrecio.getText().length() >= 10) {
            evt.consume();
        }
    }
});

// ======= Validación para campo Kilometraje =======
TxtKilometraje.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();

        // Solo permitir números
        if (!Character.isDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }

        // Evitar valores negativos
        if (c == '-' || c == '+') {
            evt.consume();
        }

        // Limitar la longitud (por ejemplo 7 dígitos)
        if (TxtKilometraje.getText().length() >= 7) {
            evt.consume();
        }
    }
});
  
        
        
        
        // ======= Validación para campo Placa =======
TxtPlaca.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();

        // Limitar a 7 caracteres
        if (TxtPlaca.getText().length() >= 7) {
            evt.consume(); // No deja escribir más
            return;
        }

        // Convertir a mayúsculas
        if (Character.isLetter(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }

        // Permitir solo letras, números y guiones (opcional)
        if (!Character.isLetterOrDigit(c) && c != '-' && c != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }
    }
});

// ======= Validación para campo Número de Serie =======
TxtSerie.addKeyListener(new java.awt.event.KeyAdapter() {
    @Override
    public void keyTyped(java.awt.event.KeyEvent evt) {
        char c = evt.getKeyChar();

        // Limitar a 17 caracteres
        if (TxtSerie.getText().length() >= 17) {
            evt.consume();
            return;
        }

        // Convertir a mayúsculas
        if (Character.isLetter(c)) {
            evt.setKeyChar(Character.toUpperCase(c));
        }

        // Permitir solo letras y números
        if (!Character.isLetterOrDigit(c) && c != KeyEvent.VK_BACK_SPACE) {
            evt.consume();
        }
    }
});


        this.setSize(1366, 768); 
        this.setLocationRelativeTo(null); // Centrar pantalla

        Estilos.aplicarEstiloTextField(TxtPlaca);
        Estilos.aplicarEstiloTextField(TxtMarca);
        Estilos.aplicarEstiloTextField(TxtColor);
        Estilos.aplicarEstiloTextField(TxtPrecio);
        Estilos.aplicarEstiloTextField(TxtModelo);
        Estilos.aplicarEstiloTextField(TxtSerie);
        Estilos.aplicarEstiloTextField(TxtKilometraje);
        Estilos.aplicarEstiloTextField(txtBuscar);
        


        Estilos.aplicarEstiloComboBox(CmbVehiculo);
        Estilos.aplicarEstiloComboBox(CmbEstado);
        Estilos.aplicarEstiloComboBox(CmbProveedor);
    
        Estilos.aplicarEstiloTabla(jTable1);
        
        

        // Escalar la imagen al tamaño del JFrame
        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Vehi1.png"));
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
                this.getWidth(),   
                this.getHeight(),  
                Image.SCALE_SMOOTH
        );
        lblFondoV.setIcon(new ImageIcon(imagenEscalada));

        car.cargarDatos(CmbVehiculo,"tipovehiculo","idtipovehiculo","descripcion");
        car.cargarDatos(CmbEstado,"estadovehiculo","idestadovehiculo","descripcion");
        car.cargarDatos(CmbProveedor,"proveedores","idproveedor","nombreproveedor");

        ut.mostrarDatos(sqlse, jTable1, new String[]{
            "ID", "Placa", "Año", "Marca", "Color", "Precio", "Modelo", "No. Serie", "Kilometraje", "Proveedor", "Tipo", "Estado"
        });

        // Opcional: muestra quién es en el título
        if (currentUser != null) {
            setTitle("Vehículos - Sesión: " + currentUser.getDisplayName() + " (" + currentUser.getRole() + ")");
        }
    }
    
    private void limpiar(){
        TxtPlaca.setText("");
        spinyear.setYear(java.util.Calendar.getInstance().get(java.util.Calendar.YEAR));
        TxtMarca.setText("");
        TxtColor.setText("");
        TxtPrecio.setText("");
        TxtModelo.setText("");
        TxtSerie.setText("");
        TxtKilometraje.setText("");
        if (CmbProveedor.getItemCount() > 0) CmbProveedor.setSelectedIndex(0);
        if (CmbVehiculo.getItemCount() > 0) CmbVehiculo.setSelectedIndex(0);
        if (CmbEstado.getItemCount() > 0) CmbEstado.setSelectedIndex(0);
        jTable1.clearSelection();
    }
    
    String sqlse = "SELECT v.idvehiculo, v.placa, v.anio, v.marca, "
           + "v.color, v.precio, v.modelo, v.noserie, v.kilometraje, "
           + "p.nombreproveedor AS proveedores, t.descripcion AS tipovehiculo, "
           + "e.descripcion AS estadovehiculo "
           + "FROM vehiculos v "
           + "JOIN proveedores p ON v.idproveedor = p.idproveedor "
           + "JOIN tipovehiculo t ON v.idtipovehiculo = t.idtipovehiculo "
           + "JOIN estadovehiculo e ON v.idestadovehiculo = e.idestadovehiculo";
    
    
   




    
    private void registrar() {
        if (!validarCampos()) return;
        String item = CmbProveedor.getSelectedItem().toString(); 
        int idP = Integer.parseInt(item.split(" - ")[0]); 
        String ite = CmbVehiculo.getSelectedItem().toString();
        int idT = Integer.parseInt(ite.split(" - ")[0]);
        String it = CmbEstado.getSelectedItem().toString();
        int idE = Integer.parseInt(it.split(" - ")[0]);
                
        String sql = "INSERT INTO vehiculos "
                   + "(placa, anio, marca, color, precio, modelo, noserie, kilometraje, idproveedor, idtipovehiculo, idestadovehiculo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Object[] parametros = {
            TxtPlaca.getText(), spinyear.getYear(), TxtMarca.getText(), TxtColor.getText(),
            TxtPrecio.getText(), TxtModelo.getText(), TxtSerie.getText(), TxtKilometraje.getText(),
            idP, idT, idE
        };

        if(ut.ejecutarActualizacion(sql, parametros)) {
            JOptionPane.showMessageDialog(null, "Vehiculo registrado correctamente.");
            ut.mostrarDatos(sqlse, jTable1, 
                new String[]{"ID", "Placa", "Anio", "Marca", "Color", "Precio", "Modelo", "No Serie", "Kilometraje", "Proveedor", "Tipo", "Estado"});
        }
    }
    
    private void editar() {
        if (!validarCampos()) return;
        int filaSeleccionada = jTable1.getSelectedRow();
        if(filaSeleccionada == -1){
            JOptionPane.showMessageDialog(null, "Seleccione un vehiculo para editar.");
            return;
        }
        int idvehiculo = Integer.parseInt(jTable1.getValueAt(filaSeleccionada, 0).toString());

        String item = CmbProveedor.getSelectedItem().toString(); 
        int idP = Integer.parseInt(item.split(" - ")[0]);
        String ite = CmbVehiculo.getSelectedItem().toString();
        int idT = Integer.parseInt(ite.split(" - ")[0]);
        String it = CmbEstado.getSelectedItem().toString();
        int idE = Integer.parseInt(it.split(" - ")[0]);

        String sql = "UPDATE vehiculos SET placa=?, anio=?, marca=?, color=?, precio=?, modelo=?, noserie=?, kilometraje=?, idproveedor=?, idtipovehiculo=?, idestadovehiculo=? "
                   + "WHERE idvehiculo=?";

        Object[] parametros = {
            TxtPlaca.getText(), spinyear.getYear(), TxtMarca.getText(), TxtColor.getText(),
            TxtPrecio.getText(), TxtModelo.getText(), TxtSerie.getText(), TxtKilometraje.getText(),
            idP, idT, idE, idvehiculo  
        };

        if(ut.ejecutarActualizacion(sql, parametros)){
            JOptionPane.showMessageDialog(null, "Datos del vehiculo actualizado correctamente.");
            ut.mostrarDatos(sqlse, jTable1, new String[]{
                "ID", "Placa", "Anio", "Marca", "Color", "Precio", "Modelo", "No Serie", "Kilometraje", "Proveedor", "Tipo", "Estado"
            });
        }
    }
    
    private void seleccion() {
       int fila = jTable1.getSelectedRow();
       if(fila != -1){
            TxtPlaca.setText(jTable1.getValueAt(fila, 1).toString());
            spinyear.setYear(Integer.parseInt(jTable1.getValueAt(fila, 2).toString().substring(0, 4)));
            TxtMarca.setText(jTable1.getValueAt(fila, 3).toString());
            TxtColor.setText(jTable1.getValueAt(fila, 4).toString());
            TxtPrecio.setText(jTable1.getValueAt(fila, 5).toString());
            TxtModelo.setText(jTable1.getValueAt(fila, 6).toString());
            TxtSerie.setText(jTable1.getValueAt(fila, 7).toString());
            TxtKilometraje.setText(jTable1.getValueAt(fila, 8).toString());
            String descP = jTable1.getValueAt(fila, 9).toString();
            for(int i=0;i<CmbProveedor.getItemCount();i++) if(CmbProveedor.getItemAt(i).contains(descP)) { CmbProveedor.setSelectedIndex(i); break; }
            String descT = jTable1.getValueAt(fila, 10).toString();
            for(int i=0;i<CmbVehiculo.getItemCount();i++) if(CmbVehiculo.getItemAt(i).contains(descT)) { CmbVehiculo.setSelectedIndex(i); break; }
            String descE = jTable1.getValueAt(fila, 11).toString();
            for(int i=0;i<CmbEstado.getItemCount();i++) if(CmbEstado.getItemAt(i).contains(descE)) { CmbEstado.setSelectedIndex(i); break; }
       }
       
       
       
       
       
    }
    
    // ==== VALIDACIONES ====
private boolean validarCampos() {
    // ====== Validaciones de texto ======
    if (TxtPlaca.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese la placa del vehículo.");
        TxtPlaca.requestFocus();
        return false;
    }

    if (TxtMarca.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese la marca del vehículo.");
        TxtMarca.requestFocus();
        return false;
    }

    if (TxtColor.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese el color del vehículo.");
        TxtColor.requestFocus();
        return false;
    }

    if (TxtPrecio.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese el precio del vehículo.");
        TxtPrecio.requestFocus();
        return false;
    }

    if (TxtModelo.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese el modelo del vehículo.");
        TxtModelo.requestFocus();
        return false;
    }

    if (TxtSerie.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese el número de serie.");
        TxtSerie.requestFocus();
        return false;
    }
    
    if (CmbVehiculo.getSelectedIndex() == 0 || 
        CmbVehiculo.getSelectedItem().toString().toLowerCase().contains("seleccione")) {
        JOptionPane.showMessageDialog(this, "Seleccione el tipo de vehículo.");
        CmbVehiculo.requestFocus();
        return false;
    }

    if (TxtKilometraje.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Por favor, ingrese el kilometraje del vehículo.");
        TxtKilometraje.requestFocus();
        return false;
    }
    
    if (CmbEstado.getSelectedIndex() == 0 || 
        CmbEstado.getSelectedItem().toString().toLowerCase().contains("seleccione")) {
        JOptionPane.showMessageDialog(this, "Seleccione el estado del vehículo.");
        CmbEstado.requestFocus();
        return false;
    }

    // ====== Validación del año ======
    int anio = spinyear.getYear();
    if (anio <= 1900 || anio > java.util.Calendar.getInstance().get(java.util.Calendar.YEAR) + 1) {
        JOptionPane.showMessageDialog(this, "Seleccione un año válido.");
        spinyear.requestFocus();
        return false;
    }

    // ====== Validaciones numéricas (solo números válidos) ======
    try {
        double precio = Double.parseDouble(TxtPrecio.getText());
        if (precio < 0) {
            JOptionPane.showMessageDialog(this, "El precio no puede ser negativo.");
            TxtPrecio.requestFocus();
            return false;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El precio debe ser un número válido (sin letras ni símbolos).");
        TxtPrecio.requestFocus();
        return false;
    }

    try {
        double km = Double.parseDouble(TxtKilometraje.getText());
        if (km < 0) {
            JOptionPane.showMessageDialog(this, "El kilometraje no puede ser negativo.");
            TxtKilometraje.requestFocus();
            return false;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "El kilometraje debe ser un número válido (sin letras ni símbolos).");
        TxtKilometraje.requestFocus();
        return false;
    }

    // ====== Validaciones de selección en ComboBox ======
    if (CmbProveedor.getSelectedIndex() == 0 || 
        CmbProveedor.getSelectedItem().toString().toLowerCase().contains("seleccione")) {
        JOptionPane.showMessageDialog(this, "Seleccione un proveedor del vehículo.");
        CmbProveedor.requestFocus();
        return false;
    }


    return true; 
}



private void buscarVehiculoPorPlaca() {
    String textoBusqueda = txtBuscar.getText().trim();
    String sqlBuscar;

    if (textoBusqueda.isEmpty()) {
        sqlBuscar = sqlse; // muestra todo
    } else {
        sqlBuscar = sqlse + " WHERE v.placa LIKE '%" + textoBusqueda + "%'";
    }

    ut.mostrarDatos(sqlBuscar, jTable1, new String[]{
        "ID", "Placa", "Año", "Marca", "Color", "Precio", "Modelo", "No. Serie", "Kilometraje", "Proveedor", "Tipo", "Estado"
    });
}





    
    
  
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        TxtMarca = new javax.swing.JTextField();
        TxtColor = new javax.swing.JTextField();
        TxtPrecio = new javax.swing.JTextField();
        TxtModelo = new javax.swing.JTextField();
        TxtPlaca = new javax.swing.JTextField();
        CmbVehiculo = new javax.swing.JComboBox<>();
        CmbEstado = new javax.swing.JComboBox<>();
        CmbProveedor = new javax.swing.JComboBox<>();
        TxtSerie = new javax.swing.JTextField();
        TxtKilometraje = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        spinyear = new com.toedter.calendar.JYearChooser();
        jLabel2 = new javax.swing.JLabel();
        lblRegresar = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        lblFondoV = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        TxtMarca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtMarcaActionPerformed(evt);
            }
        });
        getContentPane().add(TxtMarca, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 190, 220, -1));
        getContentPane().add(TxtColor, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 260, 150, -1));
        getContentPane().add(TxtPrecio, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 300, 120, -1));

        TxtModelo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TxtModeloActionPerformed(evt);
            }
        });
        getContentPane().add(TxtModelo, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 340, 220, -1));
        getContentPane().add(TxtPlaca, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 150, 220, -1));

        CmbVehiculo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(CmbVehiculo, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 420, 220, -1));

        CmbEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(CmbEstado, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 500, 220, -1));

        CmbProveedor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        getContentPane().add(CmbProveedor, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 540, 220, -1));
        getContentPane().add(TxtSerie, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 380, 160, -1));
        getContentPane().add(TxtKilometraje, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 460, 160, 20));

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

        getContentPane().add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 150, 640, 390));

        spinyear.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 1, new java.awt.Color(255, 255, 255)));
        getContentPane().add(spinyear, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 220, -1, -1));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Marca:");
        getContentPane().add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 180, 70, 40));

        lblRegresar.setFont(new java.awt.Font("Times New Roman", 2, 26)); // NOI18N
        lblRegresar.setForeground(new java.awt.Color(255, 255, 255));
        lblRegresar.setText("Regresar");
        lblRegresar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblRegresarMouseClicked(evt);
            }
        });
        getContentPane().add(lblRegresar, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 100, -1));

        jLabel5.setFont(new java.awt.Font("Times New Roman", 0, 48)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Vehículos");
        getContentPane().add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 20, 190, 70));

        jLabel3.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Año:");
        getContentPane().add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 210, 50, 40));

        jLabel6.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Color:");
        getContentPane().add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 250, 60, 40));

        jLabel7.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Precio:");
        getContentPane().add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 290, 70, 40));

        jLabel8.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Placa:");
        getContentPane().add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 140, 60, 40));

        jLabel9.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("No. de serie:");
        getContentPane().add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 370, 120, 40));

        jLabel10.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Tipo de vehículo:");
        getContentPane().add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 410, 160, 40));

        jLabel11.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Proveedor:");
        getContentPane().add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 530, 100, 40));

        jLabel12.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Modelo:");
        getContentPane().add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 330, 80, 40));

        jLabel13.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Kilometraje:");
        getContentPane().add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 450, 110, 40));

        jLabel14.setFont(new java.awt.Font("Times New Roman", 0, 22)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("Estado del vehículo:");
        getContentPane().add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 490, 180, 40));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Regi.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 490, -1, -1));

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Editar.png"))); // NOI18N
        jLabel15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel15MouseClicked(evt);
            }
        });
        getContentPane().add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 490, -1, -1));

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });
        getContentPane().add(txtBuscar, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 110, 580, -1));

        jLabel17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-buscar.png"))); // NOI18N
        jLabel17.setText("jLabel17");
        getContentPane().add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 70, 140, 90));

        lblFondoV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Vehi1.png"))); // NOI18N
        lblFondoV.setText("Buscar");
        getContentPane().add(lblFondoV, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 0, 1360, -1));
        getContentPane().add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(572, 150, 640, 400));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TxtModeloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtModeloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtModeloActionPerformed

    private void TxtMarcaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TxtMarcaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_TxtMarcaActionPerformed

    private void lblRegresarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblRegresarMouseClicked
       FrmMenu menu = new FrmMenu(currentUser);
        menu.setVisible(true);
        dispose();
    }//GEN-LAST:event_lblRegresarMouseClicked

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        if (!validarCampos()) return;
        registrar();
         
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel15MouseClicked
        if (!validarCampos()) return;
        editar();
    }//GEN-LAST:event_jLabel15MouseClicked

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        seleccion();
    }//GEN-LAST:event_jTable1MouseClicked

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        buscarVehiculoPorPlaca();
    }//GEN-LAST:event_txtBuscarKeyReleased

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
            java.util.logging.Logger.getLogger(FrmVehiculos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() { new FrmVehiculos().setVisible(true); }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CmbEstado;
    private javax.swing.JComboBox<String> CmbProveedor;
    private javax.swing.JComboBox<String> CmbVehiculo;
    private javax.swing.JTextField TxtColor;
    private javax.swing.JTextField TxtKilometraje;
    private javax.swing.JTextField TxtMarca;
    private javax.swing.JTextField TxtModelo;
    private javax.swing.JTextField TxtPlaca;
    private javax.swing.JTextField TxtPrecio;
    private javax.swing.JTextField TxtSerie;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblFondoV;
    private javax.swing.JLabel lblRegresar;
    private com.toedter.calendar.JYearChooser spinyear;
    private javax.swing.JTextField txtBuscar;
    // End of variables declaration//GEN-END:variables
}
