/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package clases;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.MatteBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

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
    combo.setOpaque(true);
    combo.setBackground(Color.BLACK);
    combo.setForeground(Color.WHITE);
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

            // Estilo base
            setFont(new Font("PMingLiU-ExtB", Font.BOLD, 16));
            setOpaque(true);
            setBackground(Color.BLACK);
            setForeground(Color.WHITE);

            // Estilo cuando está seleccionado o resaltado
             if (isSelected) {
                setBackground(Color.BLACK);
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

        // Permitir scroll horizontal
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Ajuste automático del ancho de columnas según contenido
        ajustarAnchoColumnas(tabla);
    }

    // Ajusta ancho de columnas según contenido
    private static void ajustarAnchoColumnas(JTable tabla) {
    SwingUtilities.invokeLater(() -> {
        TableColumnModel columnModel = tabla.getColumnModel();
        for (int col = 0; col < tabla.getColumnCount(); col++) {
            int anchoMax = 0;

            // Medir ancho del encabezado
            TableCellRenderer rendererHeader = tabla.getTableHeader().getDefaultRenderer();
            Component compHeader = rendererHeader.getTableCellRendererComponent(tabla,
                    tabla.getColumnName(col), false, false, 0, col);
            anchoMax = compHeader.getPreferredSize().width;

            // Medir ancho de cada celda
            for (int row = 0; row < tabla.getRowCount(); row++) {
                TableCellRenderer rendererCell = tabla.getCellRenderer(row, col);
                Component compCell = rendererCell.getTableCellRendererComponent(tabla,
                        tabla.getValueAt(row, col), false, false, row, col);
                anchoMax = Math.max(anchoMax, compCell.getPreferredSize().width);
            }

            // Ajustar la columna con margen generoso
            columnModel.getColumn(col).setPreferredWidth(anchoMax + 40); // +40 para evitar cortes
        }
    });
}


    // Método para obtener un JScrollPane con scroll horizontal y vertical
    public static JScrollPane crearTablaConScroll(JTable tabla) {
        aplicarEstiloTabla(tabla); // Aplica tus estilos y ajuste automático
        JScrollPane scrollPane = new JScrollPane(tabla,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        return scrollPane;
    }
    
    
    
    
}
