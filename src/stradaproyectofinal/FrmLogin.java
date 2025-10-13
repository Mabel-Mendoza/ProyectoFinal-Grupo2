/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package stradaproyectofinal;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

/**
 *
 * @author mabel
 */

public class FrmLogin extends javax.swing.JFrame {

    boolean mostrar = false;
    
    
    
    public FrmLogin() {
        initComponents();
        
        
        txtcontra.setEchoChar('*'); 
        imgcontra.setVisible(false); 
        imgnocontra.setVisible(true);
        
         this.setSize(1366, 768); 
         this.setLocationRelativeTo(null); 

   
    ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/stradaproyectofinal/Img-Login.png"));
    Image imagenEscalada = iconoOriginal.getImage().getScaledInstance(
            this.getWidth(),   
            this.getHeight(),  
            Image.SCALE_SMOOTH
    );
    lblFondo.setIcon(new ImageIcon(imagenEscalada));
    
    
    
    txtUsuario.setOpaque(false);        // quita el fondo
    txtUsuario.setBackground(new Color(0,0,0,0));
    
        txtcontra.setOpaque(false);
        txtcontra.setBackground(new Color(0, 0, 0, 0));
        txtcontra.setForeground(Color.WHITE);
        txtcontra.setFont(new java.awt.Font("Times New Roman", java.awt.Font.ITALIC, 18));
        txtcontra.setBorder(new MatteBorder(0, 0, 5, 0, Color.WHITE));

        
    
            // dentro del constructor (después de initComponents())
        txtcontra.setEchoChar('•'); // empieza ocultando (puedes usar '*' si prefieres)
        imgcontra.setVisible(false);   // ojo abierto oculto al inicio
        imgnocontra.setVisible(true);  // ojo cerrado visible al inicio

        // click ojo cerrado -> mostrar contraseña
        imgnocontra.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtcontra.setEchoChar((char)0); // mostrar texto
                imgnocontra.setVisible(false);
                imgcontra.setVisible(true);
            }
        });

        // click ojo abierto -> ocultar contraseña
        imgcontra.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                txtcontra.setEchoChar('•'); // ocultar con puntitos
                imgcontra.setVisible(false);
                imgnocontra.setVisible(true);
            }
});

// para leer la contraseña (por ejemplo en login):
char[] clave = txtcontra.getPassword();
String claveString = new String(clave); // si necesitas String (evita si es posible)
    
    
    

// ---- EFECTO ENTRAR ----
        imgEntrar1.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                FrmMenu menu = new FrmMenu();
                menu.setVisible(true);
                dispose();
            }

        });

        // ---- EFECTO SALIR ----
        ImgSalir1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                ImgSalir1.setIcon(ImgSalir.getIcon()); // cambia al claro
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ImgSalir1.setIcon(ImgSalir1.getIcon()); // vuelve al oscuro
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                int confirm = JOptionPane.showConfirmDialog(null, "¿Seguro que deseas salir?", "Salir", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
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
        txtUsuario.setText("Introduzca el Usuario");
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

        txtcontra.setText("Ingrese la contraseña");
        txtcontra.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 10, 0, new java.awt.Color(255, 255, 255)));
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
        // TODO add your handling code here:
    }//GEN-LAST:event_ImgSalirMouseClicked

    private void imgEntrarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imgEntrarMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_imgEntrarMouseClicked

    private void imgEntrar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imgEntrar1MouseClicked
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
            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
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
