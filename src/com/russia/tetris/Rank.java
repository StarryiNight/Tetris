package com.russia.tetris;


import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class Rank extends JFrame {
    Rank(){
        JFrame jFrame = new JFrame("排行榜");
        jFrame.setBounds(600,300,280, 380);
        jFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        String[] name = {"排名", "用户", "分数"};
        JTable table = new JTable(new DbConnection().getRank(),name);
        JLabel title = new JLabel(new ImageIcon("image/rank.png"));

        jFrame.setLayout(null);
        JScrollPane jScrollPane = new JScrollPane(table);
        jFrame.add(title);
        title.setBounds(0,0,280, 100);
        setTableColumnCenter(table);
        table.setEnabled(false);

        jScrollPane.setBounds(0,90,280, 250);
        table.setSize(280,300);


        jFrame.add(jScrollPane);
        jFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        jFrame.setVisible(true);
        jFrame.setResizable(false);
    }

    public void setTableColumnCenter(JTable table){
        DefaultTableCellRenderer r = new DefaultTableCellRenderer();
        r.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, r);
    }

}
