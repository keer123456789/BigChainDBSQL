/*
 * Created by JFormDesigner on Mon Jan 27 22:44:48 CST 2020
 */

package com.keer.GUI;

import com.keer.Util.BigchainDBRunnerUtil;
import com.keer.Util.BigchainDBUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.*;

/**
 * @author keer
 */
public class BigchainDB extends JFrame {
    private static Logger logger = LoggerFactory.getLogger(BigchainDB.class);
    public BigchainDB() {
        initComponents();
    }

    /**
     * 连接按钮点击事件
     * @param e
     */
    private void connBtnActionPerformed(ActionEvent e) {
        // TODO add your code here
        String url = hostField.getText();
        String key = keyField.getText();
        if (url.equals("") || key.equals("")) {
            JOptionPane.showMessageDialog(null, "host和密钥不能为空", "错误", JOptionPane.WARNING_MESSAGE);
            logger.error("host和密钥不能为空");
        }
        BigchainDBRunnerUtil runnerUtil=new BigchainDBRunnerUtil();
        if(!runnerUtil.StartConn(url)){
            JOptionPane.showMessageDialog(null,"连接失败！！检查host是否正确！！","错误",JOptionPane.WARNING_MESSAGE);
            logger.error("连接失败！！检查host是否正确！！");
        }
    }

    /**
     * 链上信息按钮点击事件
     * @param e
     */
    private void chainInfoActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    /**
     * 运行按钮点击事件
     * @param e
     */
    private void runActionPerformed(ActionEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        panel3 = new JPanel();
        connInfo = new JLabel();
        panel4 = new JPanel();
        Host = new JLabel();
        hostField = new JTextField();
        keyField = new JTextField();
        Key = new JLabel();
        chainInfo = new JButton();
        connBtn = new JButton();
        panel1 = new JPanel();
        scrollPane1 = new JScrollPane();
        tree1 = new JTree();
        runField = new JTextField();
        run = new JButton();
        scrollPane2 = new JScrollPane();
        tabbedPane1 = new JTabbedPane();

        //======== this ========
        setForeground(Color.black);
        setBackground(new Color(51, 255, 255));
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        //======== panel3 ========
        {
            panel3.setBackground(new Color(204, 204, 204));
            panel3.setLayout(null);

            //---- connInfo ----
            connInfo.setText("\u4fe1\u606f");
            panel3.add(connInfo);
            connInfo.setBounds(15, 0, 50, 22);

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
        panel3.setBounds(-5, 465, 795, 25);

        //======== panel4 ========
        {
            panel4.setBackground(new Color(204, 204, 204));
            panel4.setLayout(null);

            //---- Host ----
            Host.setText("Host");
            Host.setBackground(Color.white);
            Host.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            panel4.add(Host);
            Host.setBounds(5, 10, 50, 30);

            //---- hostField ----
            hostField.setBackground(Color.white);
            panel4.add(hostField);
            hostField.setBounds(60, 15, 135, hostField.getPreferredSize().height);

            //---- keyField ----
            keyField.setBackground(Color.white);
            panel4.add(keyField);
            keyField.setBounds(265, 15, 165, keyField.getPreferredSize().height);

            //---- Key ----
            Key.setText("Key");
            Key.setFont(new Font("Microsoft YaHei UI", Font.PLAIN, 16));
            panel4.add(Key);
            Key.setBounds(215, 15, 50, 30);

            //---- chainInfo ----
            chainInfo.setText("\u94fe\u4e0a\u4fe1\u606f");
            chainInfo.setBackground(Color.white);
            chainInfo.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chainInfoActionPerformed(e);
                }
            });
            panel4.add(chainInfo);
            chainInfo.setBounds(new Rectangle(new Point(645, 15), chainInfo.getPreferredSize()));

            //---- connBtn ----
            connBtn.setText("\u8fde\u63a5");
            connBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    connBtnActionPerformed(e);
                }
            });
            panel4.add(connBtn);
            connBtn.setBounds(465, 15, 95, connBtn.getPreferredSize().height);

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
        panel4.setBounds(0, 0, 790, 55);

        //======== panel1 ========
        {
            panel1.setBackground(Color.white);
            panel1.setLayout(null);

            //======== scrollPane1 ========
            {

                //---- tree1 ----
                tree1.setBackground(Color.white);
                scrollPane1.setViewportView(tree1);
            }
            panel1.add(scrollPane1);
            scrollPane1.setBounds(0, 0, 120, 410);

            //---- runField ----
            runField.setBackground(Color.white);
            panel1.add(runField);
            runField.setBounds(120, 285, 670, 95);

            //---- run ----
            run.setText("\u8fd0\u884c");
            run.setBackground(Color.white);
            run.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    runActionPerformed(e);
                }
            });
            panel1.add(run);
            run.setBounds(675, 375, run.getPreferredSize().width, 35);

            //======== scrollPane2 ========
            {

                //======== tabbedPane1 ========
                {
                    tabbedPane1.setBackground(Color.white);
                    tabbedPane1.setForeground(new Color(204, 204, 204));
                }
                scrollPane2.setViewportView(tabbedPane1);
            }
            panel1.add(scrollPane2);
            scrollPane2.setBounds(120, 0, 670, 285);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel1.getComponentCount(); i++) {
                    Rectangle bounds = panel1.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel1.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel1.setMinimumSize(preferredSize);
                panel1.setPreferredSize(preferredSize);
            }
        }
        contentPane.add(panel1);
        panel1.setBounds(0, 55, 790, 410);

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
    private JLabel connInfo;
    private JPanel panel4;
    private JLabel Host;
    private JTextField hostField;
    private JTextField keyField;
    private JLabel Key;
    private JButton chainInfo;
    private JButton connBtn;
    private JPanel panel1;
    private JScrollPane scrollPane1;
    private JTree tree1;
    private JTextField runField;
    private JButton run;
    private JScrollPane scrollPane2;
    private JTabbedPane tabbedPane1;
    // JFormDesigner - End of variables declaration  //GEN-END:variables

    /**
     * 创建新的面板
     *
     * @param text
     * @return
     */
    private static JComponent createJTable(String text) {
        JPanel panel = new JPanel(new GridLayout(1, 1));

        JTable table = new JTable();
        panel.add(table);

        return panel;
    }

//    /**
//     * 点击连接按钮触发事件
//     *
//     * @param e
//     */
//    private void btConnActionPerformed(ActionEvent e) {
//        String url = hostField.getText();
//        String key = keyField.getText();
//        if (url.equals(null) || key.equals(null)) {
//            JOptionPane.showMessageDialog(null, "host和密钥不能为空", "", JOptionPane.WARNING_MESSAGE);
//            logger.error("host和密钥不能为空");
//        }
//    }
}
