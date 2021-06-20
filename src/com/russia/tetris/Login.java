package com.russia.tetris;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.*;

public class Login extends JFrame {
    static JFrame jf = new JFrame("账号登陆");
    JPanel jp = new JPanel();
    JLabel l_name = new JLabel("用户：");
    JLabel l_password = new JLabel("密码：");
    static JTextField t_name = new JTextField(10);
    static JPasswordField t_password = new JPasswordField(10);
    JButton ok = new JButton("登陆");
    JButton you = new JButton("游客");
    JButton register = new JButton("注册");

    public Login() {
        jp.add(l_name);
        jp.add(t_name);
        jp.add(l_password);
        jp.add(t_password);
        jp.add(ok);
        jp.add(you);
        jf.add(jp);
        //事件OK按钮监听
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                check(true);
            }

        });

        //事件Cancel按钮监听
        you.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                check(false);
            }

        });
        jf.setVisible(true);

        jf.setSize(200, 150);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jf.setLocation(500, 270);
    }

    public static void check(boolean flag) {
        String userName = null;
        int score=0;
        if (!flag) {
            char[] a = "aaaaaa".toCharArray();
            Object[] user = new DbConnection().queryByName("游客", a);
            userName = (String) user[0];
            score = (int) user[1];
        }else{
            if (t_name.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "请输入用户名!", "error", JOptionPane.ERROR_MESSAGE);
            } else if (t_password.getPassword().equals("")) {
                JOptionPane.showMessageDialog(null, "请输入密码!", "error", JOptionPane.ERROR_MESSAGE);
            } else {
                Object[] user = new DbConnection().queryByName(t_name.getText(), t_password.getPassword());
                userName = (String) user[0];
                score = (int) user[1];
            }
        }
        if (userName != null) {
            jf.setVisible(false);
            TetrisFrame frame = new TetrisFrame(userName,score);
        }
    }


    public static void main(String[] args) {
        Login a = new Login();
//        TetrisFrame frame = new TetrisFrame();
    }

}
