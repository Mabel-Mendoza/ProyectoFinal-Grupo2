package stradaproyectofinal;

import clases.User;
import clases.UsersRepo;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

public class FrmLogin extends javax.swing.JFrame {

    // ===== Placeholders =====
    private final String USER_HINT = "Introduzca el Usuario";
    private final String PASS_HINT = "Ingrese la contraseña";
    private boolean showingUserHint = true;
    private boolean showingPassHint = true;
    private char echoOriginal = '•'; // o '*'

    private final Color PLACEHOLDER_COLOR = new Color(255, 255, 255, 140); // blanco translúcido
    private final Color TEXT_COLOR        = Color.WHITE;
    private final Color CARET_HIDDEN      = new Color(255, 255, 255, 0);   // cursor invisible
    private final Color CARET_VISIBLE     = Color.WHITE;                   // cursor visible

    public FrmLogin() {
        initComponents(); // *** NO TOCAR EL CONTENIDO DE ESTE MÉTODO ***

        // ----- Ajustes visuales -----
        this.setSize(1366, 768);
        this.setLocationRelativeTo(null);

        ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Login.png"));
        Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
                this.getWidth(), this.getHeight(), Image.SCALE_SMOOTH
        );
        lblFondo.setIcon(new ImageIcon(imagenEscalada));

        txtUsuario.setOpaque(false);
        txtUsuario.setBackground(new Color(0, 0, 0, 0));

        txtcontra.setOpaque(false);
        txtcontra.setBackground(new Color(0, 0, 0, 0));
        txtcontra.setForeground(TEXT_COLOR);
        txtcontra.setFont(new java.awt.Font("Times New Roman", java.awt.Font.ITALIC, 18));

        // ----- Config base de contraseña -----
        echoOriginal = '•';
        txtcontra.setEchoChar(echoOriginal);

        // ===== Inicialización como pista =====
        // Usuario
        if (USER_HINT.equals(txtUsuario.getText()) || txtUsuario.getText().trim().isEmpty()) {
            aplicarHintUsuario();
        } else {
            showingUserHint = false;
            txtUsuario.setForeground(TEXT_COLOR);
            txtUsuario.setCaretColor(CARET_VISIBLE);
        }
        // Contraseña
        String passActual = new String(txtcontra.getPassword());
        if (PASS_HINT.equals(passActual) || passActual.trim().isEmpty()) {
            aplicarHintContra();
        } else {
            showingPassHint = false;
            txtcontra.setForeground(TEXT_COLOR);
            txtcontra.setEchoChar(echoOriginal);
            txtcontra.setCaretColor(CARET_VISIBLE);
        }

        // ===== DocumentFilter: limpia pista al escribir/pegar y re-aplica si queda vacío =====
        ((AbstractDocument) txtUsuario.getDocument()).setDocumentFilter(new PlaceholderFilter(
                () -> showingUserHint,
                // clearHint: limpiar pista justo antes del primer insert/pegar
                () -> {
                    txtUsuario.setText("");
                    txtUsuario.setForeground(TEXT_COLOR);
                    txtUsuario.setCaretColor(CARET_VISIBLE);
                    showingUserHint = false;
                },
                // applyHintWhenEmpty: si se quedó vacío tras la edición, reponer pista de inmediato
                () -> {
                    if (txtUsuario.getText().trim().isEmpty()) {
                        aplicarHintUsuario();
                    }
                }
        ));

        ((AbstractDocument) txtcontra.getDocument()).setDocumentFilter(new PlaceholderFilter(
                () -> showingPassHint,
                () -> {
                    txtcontra.setText("");
                    txtcontra.setForeground(TEXT_COLOR);
                    txtcontra.setEchoChar(echoOriginal);
                    txtcontra.setCaretColor(CARET_VISIBLE);
                    showingPassHint = false;
                },
                () -> {
                    if (new String(txtcontra.getPassword()).trim().isEmpty()) {
                        aplicarHintContra();
                    }
                }
        ));

        // ===== Bloquear Backspace/Delete cuando hay pista =====
        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyPressed(java.awt.event.KeyEvent e) {
                if (showingUserHint && (e.getKeyCode()==java.awt.event.KeyEvent.VK_BACK_SPACE
                        || e.getKeyCode()==java.awt.event.KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });
        txtcontra.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override public void keyPressed(java.awt.event.KeyEvent e) {
                if (showingPassHint && (e.getKeyCode()==java.awt.event.KeyEvent.VK_BACK_SPACE
                        || e.getKeyCode()==java.awt.event.KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });

        // ===== Reaplicar pista si queda vacío al perder foco (fallback) =====
        txtUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (txtUsuario.getText().trim().isEmpty()) {
                    aplicarHintUsuario();
                }
            }
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if (showingUserHint) {
                    txtUsuario.setCaretPosition(0);
                    txtUsuario.setCaretColor(CARET_HIDDEN);
                }
            }
        });
        txtcontra.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override public void focusLost(java.awt.event.FocusEvent e) {
                if (new String(txtcontra.getPassword()).trim().isEmpty()) {
                    aplicarHintContra();
                }
            }
            @Override public void focusGained(java.awt.event.FocusEvent e) {
                if (showingPassHint) {
                    txtcontra.setCaretPosition(0);
                    txtcontra.setCaretColor(CARET_HIDDEN);
                }
            }
        });

        // ===== Evitar que el cursor entre en la pista con el mouse =====
        MouseAdapter lockCaretOnPlaceholder = new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (showingUserHint && e.getSource()==txtUsuario) {
                    txtUsuario.setCaretPosition(0);
                    e.consume();
                } else if (showingPassHint && e.getSource()==txtcontra) {
                    txtcontra.setCaretPosition(0);
                    e.consume();
                }
            }
            @Override public void mouseReleased(MouseEvent e) {
                if (showingUserHint && e.getSource()==txtUsuario) {
                    txtUsuario.setCaretPosition(0);
                    e.consume();
                } else if (showingPassHint && e.getSource()==txtcontra) {
                    txtcontra.setCaretPosition(0);
                    e.consume();
                }
            }
            @Override public void mouseClicked(MouseEvent e) {
                if (showingUserHint && e.getSource()==txtUsuario) {
                    txtUsuario.setCaretPosition(0);
                    e.consume();
                } else if (showingPassHint && e.getSource()==txtcontra) {
                    txtcontra.setCaretPosition(0);
                    e.consume();
                }
            }
        };
        txtUsuario.addMouseListener(lockCaretOnPlaceholder);
        txtcontra.addMouseListener(lockCaretOnPlaceholder);

        // ===== Ojo mostrar/ocultar =====
        imgcontra.setVisible(false);
        imgnocontra.setVisible(true);

        imgnocontra.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (!showingPassHint) {
                    txtcontra.setEchoChar((char)0);
                    imgnocontra.setVisible(false);
                    imgcontra.setVisible(true);
                }
            }
        });
        imgcontra.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (!showingPassHint) {
                    txtcontra.setEchoChar(echoOriginal);
                    imgcontra.setVisible(false);
                    imgnocontra.setVisible(true);
                }
            }
        });

        // ---- ENTRAR ----
        imgEntrar1.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                String usuario = showingUserHint ? "" : txtUsuario.getText().trim();
                String clave   = showingPassHint ? "" : new String(txtcontra.getPassword());
;
            }
        });

        // ---- SALIR ----
        ImgSalir1.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { ImgSalir1.setIcon(ImgSalir.getIcon()); }
            @Override public void mouseExited (MouseEvent e) { ImgSalir1.setIcon(ImgSalir1.getIcon()); }
            @Override public void mouseClicked(MouseEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "¿Seguro que deseas salir?", "Salir", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) System.exit(0);
            }
        });
    }

    // ===== Aplicar pistas =====
    private void aplicarHintUsuario() {
        txtUsuario.setText(USER_HINT);
        txtUsuario.setForeground(PLACEHOLDER_COLOR);
        txtUsuario.setCaretColor(CARET_HIDDEN);  // cursor invisible con pista
        showingUserHint = true;
        txtUsuario.setCaretPosition(0);          // cursor al inicio
    }

    private void aplicarHintContra() {
        txtcontra.setText(PASS_HINT);
        txtcontra.setForeground(PLACEHOLDER_COLOR);
        txtcontra.setEchoChar((char)0);          // sin puntos cuando es pista
        txtcontra.setCaretColor(CARET_HIDDEN);   // cursor invisible con pista
        showingPassHint = true;
        imgcontra.setVisible(false);
        imgnocontra.setVisible(true);
        txtcontra.setCaretPosition(0);
    }

    // ===== DocumentFilter: limpia pista al primer insert/pegar y re-aplica si vacío =====
    private static class PlaceholderFilter extends DocumentFilter {
        private final java.util.function.BooleanSupplier isShowingHint;
        private final Runnable clearHint;
        private final Runnable applyHintWhenEmpty;

        PlaceholderFilter(java.util.function.BooleanSupplier isShowingHint,
                          Runnable clearHint,
                          Runnable applyHintWhenEmpty) {
            this.isShowingHint = isShowingHint;
            this.clearHint = clearHint;
            this.applyHintWhenEmpty = applyHintWhenEmpty;
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                throws BadLocationException {
            if (string != null && !string.isEmpty() && isShowingHint.getAsBoolean()) {
                clearHint.run();
                offset = 0; // insert desde el inicio tras limpiar
            }
            super.insertString(fb, offset, string, attr);
            // tras insertar, no hace falta reponer pista
        }

        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                throws BadLocationException {
            boolean hadHint = isShowingHint.getAsBoolean();
            if (text != null && !text.isEmpty() && hadHint) {
                clearHint.run();
                offset = 0;
                length = 0;
            }
            super.replace(fb, offset, length, text, attrs);
            // si reemplazaron por vacío y quedó todo vacío -> reponer pista
            if (!isShowingHint.getAsBoolean() && ((AbstractDocument) fb.getDocument()).getLength() == 0) {
                applyHintWhenEmpty.run();
            }
        }

        @Override
        public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            if (isShowingHint.getAsBoolean()) {
                // No borres la pista con Backspace/Delete
                return;
            }
            super.remove(fb, offset, length);
            // si quedó vacío, reponer pista inmediatamente
            if (((AbstractDocument) fb.getDocument()).getLength() == 0) {
                applyHintWhenEmpty.run();
            }
        }
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtUsuario = new javax.swing.JTextField();
        imgnocontra = new javax.swing.JLabel();
        imgcontra = new javax.swing.JLabel();
        txtcontra = new javax.swing.JPasswordField();
        imgEntrar1 = new javax.swing.JLabel();
        ImgSalir1 = new javax.swing.JLabel();
        imgEntrar = new javax.swing.JLabel();
        ImgSalir = new javax.swing.JLabel();
        lblFondo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        txtUsuario.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        txtUsuario.setForeground(new java.awt.Color(255, 255, 255));
        txtUsuario.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(255, 255, 255)));
        txtUsuario.setSelectionColor(new java.awt.Color(58, 0, 12));
        txtUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsuarioFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUsuarioFocusLost(evt);
            }
        });
        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });
        getContentPane().add(txtUsuario, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 410, 330, 50));

        imgnocontra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/img-nocontrasena.png"))); // NOI18N
        getContentPane().add(imgnocontra, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 530, 60, 60));

        imgcontra.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/img-contrasena.png"))); // NOI18N
        getContentPane().add(imgcontra, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 530, 60, 60));

        txtcontra.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 5, 0, new java.awt.Color(255, 255, 255)));
        getContentPane().add(txtcontra, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 520, 330, 50));

        imgEntrar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Log-in (1).png"))); // NOI18N
        imgEntrar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imgEntrar1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                imgEntrar1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                imgEntrar1MouseExited(evt);
            }
        });
        imgEntrar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                imgEntrar1KeyPressed(evt);
            }
        });
        getContentPane().add(imgEntrar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 550, -1, -1));

        ImgSalir1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Log-out (1).png"))); // NOI18N
        ImgSalir1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ImgSalir1MouseClicked(evt);
            }
        });
        getContentPane().add(ImgSalir1, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 550, -1, -1));

        imgEntrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Log-in (2).png"))); // NOI18N
        imgEntrar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imgEntrarMouseClicked(evt);
            }
        });
        getContentPane().add(imgEntrar, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 550, -1, -1));

        ImgSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Log-out (2)_1.png"))); // NOI18N
        ImgSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ImgSalirMouseClicked(evt);
            }
        });
        getContentPane().add(ImgSalir, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 550, -1, -1));

        lblFondo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Login.png"))); // NOI18N
        lblFondo.setText("Ingrese la contraseña");
        getContentPane().add(lblFondo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsuarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioFocusGained
          // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioFocusGained

    private void txtUsuarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioFocusLost
     // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioFocusLost

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void ImgSalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ImgSalirMouseClicked
        int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que deseas salir?", "Salir", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) System.exit(0);
    // TODO add your handling code here:
    }//GEN-LAST:event_ImgSalirMouseClicked

    private void imgEntrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imgEntrarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_imgEntrarMouseClicked

    private void imgEntrar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imgEntrar1MouseClicked
                                       
       String usuario = showingUserHint ? "" : txtUsuario.getText().trim();
        String clave   = showingPassHint ? "" : new String(txtcontra.getPassword());

        // Si el usuario dejó las pistas/están vacíos
        if (usuario.isEmpty() || usuario.equals(USER_HINT)
         || clave.isEmpty()   || clave.equals(PASS_HINT)) {
            JOptionPane.showMessageDialog(this, "Ingrese usuario y contraseña válidos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Optional<User> userOpt = UsersRepo.login(usuario, clave);
if (userOpt.isPresent()) {
    User logged = userOpt.get();
    FrmMenu menu = new FrmMenu(logged); // pasa el usuario con su Role
    menu.setVisible(true);
    dispose();
} 

    
       
   // TODO add your handling code here:
    }//GEN-LAST:event_imgEntrar1MouseClicked

    private void ImgSalir1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ImgSalir1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ImgSalir1MouseClicked

    private void imgEntrar1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imgEntrar1MouseEntered
        imgEntrar1.setIcon(imgEntrar.getIcon());
    }//GEN-LAST:event_imgEntrar1MouseEntered

    private void imgEntrar1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imgEntrar1MouseExited
        imgEntrar1.setIcon(imgEntrar1.getIcon());
    }//GEN-LAST:event_imgEntrar1MouseExited

    private void imgEntrar1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_imgEntrar1KeyPressed
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            imgEntrar1MouseClicked(null);
        }// TODO add your handling code here:
    }//GEN-LAST:event_imgEntrar1KeyPressed

    
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmLogin().setVisible(true);
            }
        });
    }


    
    
    
    
                                        
    
    
    
    
    
    
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ImgSalir;
    private javax.swing.JLabel ImgSalir1;
    private javax.swing.JLabel imgEntrar;
    private javax.swing.JLabel imgEntrar1;
    private javax.swing.JLabel imgcontra;
    private javax.swing.JLabel imgnocontra;
    private javax.swing.JLabel lblFondo;
    private javax.swing.JTextField txtUsuario;
    private javax.swing.JPasswordField txtcontra;
    // End of variables declaration//GEN-END:variables
}
