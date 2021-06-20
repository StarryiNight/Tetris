package com.russia.tetris;

import javax.swing.*;
import java.sql.*;

public class DbConnection {
    private static Connection connDB() throws SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //访问本机的mysql数据库,格式 jdbc:数据库://本地端口3306/数据库名?跨时区参数
        String url = "jdbc:mysql://localhost:3306/user?serverTimezone=Asia/Shanghai";
        String username = "root";
        String password = "123456";
        //获取到数据库链接
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }

    public Object[] queryByName(String name, char[] password) {
        String pwd = String.valueOf(password);
        String sql = "select * from user.user where user = '" + name + "' and password = '" + pwd + "' ";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            conn = connDB();
            st = conn.createStatement();
            rs = st.executeQuery(sql);
            if (!rs.next()) {
                JOptionPane.showMessageDialog(null, "此用户不存在或密码错误!", "error", JOptionPane.ERROR_MESSAGE);
                return null;

            } else {
                String userName=rs.getString(1);
                int score = rs.getInt(3);
                JOptionPane.showMessageDialog(null, "欢迎您,"+userName, "登陆成功", JOptionPane.INFORMATION_MESSAGE);
                Object[] user = new Object[2];
                user[0] = userName;
                user[1] = score;
                return user;
            }
        } catch (SQLException e) {
            System.out.print("Exception" + e.getMessage());
        }
        return null;
    }

    public Object[][] getRank() {
        String sql = "select * from `user`.user order by score desc";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        Object[][] rankData = new Object[10][3];
            try {
                conn = connDB();
                st = conn.createStatement();
                rs = st.executeQuery(sql);
                int count = 0;
                while (rs.next()) {
                    rankData[count][0] = count + 1;
                    rankData[count][1] = rs.getString(1);
                    rankData[count][2] = rs.getInt(3);
                    count++;
                    if (count >= 10) {
                        break;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        return rankData;
    }

    public boolean saveScore(String username,int score) {
        String sql = "update  `user`.user set score=? where user=? ";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        int n = 0;
        try {
            conn = connDB();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, score);
            ps.setString(2, username);
            n = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n == 1;
    }

    public boolean register(String username,char[] password) {
        String sql = "insert into  `user`.user values(?,?,0) ";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        int n = 0;
        try {
            conn = connDB();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, String.valueOf(password));
            n = ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n == 1;
    }

    public int maxScore(String username) {
        String sql = "select score from user.user where user=?";
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        int n = 0;
        try {
            conn = connDB();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();
            while (rs.next()) {
                n = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return n;
    }
}

