/*
 * Created by JFormDesigner on Mon Jan 27 22:44:48 CST 2020
 */

package com.keer.GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author keer
 */
public class BigchainDB extends JFrame {
    public BigchainDB() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel3 = new JPanel();
        panel4 = new JPanel();
        label1 = new JLabel();
        scrollPane2 = new JScrollPane();
        table1 = new JTable();
        scrollPane3 = new JScrollPane();
        tree1 = new JTree();
        panel5 = new JPanel();
        scrollPane4 = new JScrollPane();
        textArea1 = new JTextArea();
        button1 = new JButton();

        //======== this ========
        setForeground(Color.black);
        setBackground(new Color(51, 255, 255));
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel3 ========
        {
            panel3.setBackground(new Color(204, 204, 204));
            panel3.setLayout(null);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel3.getComponentCount(); i++) {
                    Rectangle bounds = panel3.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel3.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel3.setMinimumSize(preferredSize);
                panel3.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel3);
        panel3.setBounds(-5, 465, 785, 25);

        //======== panel4 ========
        {
            panel4.setBackground(new Color(204, 204, 204));
            panel4.setLayout(null);

            //---- label1 ----
            label1.setText("text");
            panel4.add(label1);
            label1.setBounds(0, 0, 95, 55);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel4.getComponentCount(); i++) {
                    Rectangle bounds = panel4.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel4.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel4.setMinimumSize(preferredSize);
                panel4.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel4);
        panel4.setBounds(0, 0, 785, 55);

        //======== scrollPane2 ========
        {

            //---- table1 ----
            table1.setBackground(Color.white);
            scrollPane2.setViewportView(table1);
        }
        contentPane.add(scrollPane2);
        scrollPane2.setBounds(160, 55, 625, 300);

        //======== scrollPane3 ========
        {

            //---- tree1 ----
            tree1.setBackground(Color.white);
            scrollPane3.setViewportView(tree1);
        }
        contentPane.add(scrollPane3);
        scrollPane3.setBounds(0, 55, 160, 410);

        //======== panel5 ========
        {
            panel5.setBackground(Color.white);
            panel5.setLayout(null);

            //======== scrollPane4 ========
            {

                //---- textArea1 ----
                textArea1.setBackground(Color.white);
                scrollPane4.setViewportView(textArea1);
            }
            panel5.add(scrollPane4);
            scrollPane4.setBounds(0, 0, 625, 70);

            //---- button1 ----
            button1.setText("\u8fd0\u884c");
            button1.setBackground(new Color(204, 204, 204));
            panel5.add(button1);
            button1.setBounds(480, 75, 125, button1.getPreferredSize().height);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel5.getComponentCount(); i++) {
                    Rectangle bounds = panel5.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel5.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel5.setMinimumSize(preferredSize);
                panel5.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel5);
        panel5.setBounds(160, 355, 625, 110);

        { // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JPanel panel3;
    private JPanel panel4;
    private JLabel label1;
    private JScrollPane scrollPane2;
    private JTable table1;
    private JScrollPane scrollPane3;
    private JTree tree1;
    private JPanel panel5;
    private JScrollPane scrollPane4;
    private JTextArea textArea1;
    private JButton button1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
