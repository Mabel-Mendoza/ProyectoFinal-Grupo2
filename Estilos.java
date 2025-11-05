/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 *
 * @author mabel
 */
public class Estilos {
    
    public static void aplicarEstiloTextField(JTextField txt) {
        txt.setOpaque(false);
        txt.setBackground(new Color(0, 0, 0, 0));
        txt.setForeground(Color.WHITE);
        txt.setFont(new java.awt.Font("PMingLiU-ExtB", java.awt.Font.ITALIC, 16));
        txt.setBorder(new MatteBorder(0, 0, 2, 0, Color.WHITE));
    }

    // Estilo para JComboBox
    public static void aplicarEstiloComboBox(JComboBox combo) {
        combo.setOpaque(false);
        combo.setBackground(new Color(0, 0, 0, 0));
        combo.setFont(new java.awt.Font("PMingLiU-ExtB", java.awt.Font.ITALIC, 16));
        
        // Línea blanca inferior
        combo.setBorder(new MatteBorder(0, 0, 2, 0, Color.WHITE));

        // Renderer para ítems
        combo.setRenderer(new javax.swing.plaf.basic.BasicComboBoxRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    javax.swing.JList list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                setFont(new Font("PMingLiU-ExtB", Font.BOLD, 16));
                setForeground(Color.WHITE);
                setOpaque(false);

                if (isSelected) {
                    setOpaque(true);
                    setBackground(new Color(50, 50, 50)); // gris oscuro
                    setForeground(Color.WHITE);
                }

                return this;
            }
        });
    }
    
    
    public static void aplicarEstiloTabla(JTable tabla) {
        // Encabezado
        JTableHeader header = tabla.getTableHeader();
        header.setFont(new Font("Times New Roman", Font.BOLD, 14));

        // Forzar renderer personalizado para el encabezado
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(153, 0, 0)); // rojo oscuro
                c.setForeground(Color.WHITE);          // texto blanco
                setHorizontalAlignment(CENTER);        // centrado
                return c;
            }
        };

        for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Celdas
        tabla.setBackground(Color.BLACK);
        tabla.setForeground(Color.WHITE);
        tabla.setFont(new Font("Times New Roman", Font.PLAIN, 13));
        tabla.setGridColor(Color.DARK_GRAY);
        tabla.setRowHeight(25);
    }
    
}
