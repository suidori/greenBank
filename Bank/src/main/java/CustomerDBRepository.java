package org.example;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CustomerDBRepository {

    // 본인 정보 조회
    public void c_select(int u_idx) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.0.53:8888/Bank",
                    "root", "1234");
            pstmt = conn.prepareStatement("select * from users where u_idx = ?");
            pstmt.setInt(1, u_idx);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("""
                                고유번호 = %d
                                권한 = %s
                                아이디 = %s
                                비밀번호 = %s
                                이름 = %s
                                전화번호 = %s
                                """.formatted(
                                rs.getInt("idx"),
                                rs.getString("u_level"),
                                rs.getString("u_id"),
                                rs.getString("u_password"),
                                rs.getString("u_name"),
                                rs.getString("u_phone")
                        )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 본인 정보 수정
    public void c_update(int u_idx) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.0.53:8888/Bank",
                    "root", "1234");

            pstmt = conn.prepareStatement("UPDATE users SET u_id = ?, u_password = ?, u_name = ?, u_phone = ? WHERE idx = ?");

            pstmt.setInt(5, u_idx);

            String id = JOptionPane.showInputDialog("수정할 아이디");
            pstmt.setString(1, id);

            int pw = Integer.parseInt(JOptionPane.showInputDialog("수정할 비밀번호"));
            pstmt.setInt(2, pw);

            String name = JOptionPane.showInputDialog("수정할 이름");
            pstmt.setString(3, name);

            String pn = JOptionPane.showInputDialog("수정할 핸드폰번호(-까지 입력해주세요)");
            pstmt.setString(4, pn);


            pstmt.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 본인 계좌목록 조회
    public void a_select(int u_idx) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.0.53:8888/Bank",
                    "root", "1234");
            pstmt = conn.prepareStatement("select a_number from owners where = ?");
            pstmt.setInt(1, u_idx);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(
                        """
                                계좌번호 = %d
                                """.formatted(
                                rs.getInt("a_number"))
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 원하는 계좌 정보 조회
    public void ah_select(int a_number) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.0.53:8888/Bank",
                    "root", "1234");
            pstmt = conn.prepareStatement("select u_name, h_timestamp, h_calc, h_balance from history h inner join users u on (h.u_idx == u.u_idx) where a_number = ? order by h_timestamp desc limit 5");
            pstmt.setInt(1, a_number);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println(
                        """
                                날짜 = %s
                                거래자명 = %s
                                거래액 = %d
                                잔액 = %d
                                """.formatted(
                                rs.getString("h_timestamp"),
                                rs.getString("u_name"),
                                rs.getInt("h_calc"),
                                rs.getInt("h_balance"))

                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 입출금 기능
    public void h_insert(int a_number, int u_idx) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.0.53:8888/Bank",
                    "root", "1234");
            pstmt = conn.prepareStatement("INSERT INTO history (a_number,u_idx,h_calc,h_balance) VALUES (?,?,?,?)");
            pstmt.setInt(1, a_number);
            pstmt.setInt(2, u_idx);

            int h_calc = Integer.parseInt(JOptionPane.showInputDialog("거래액을 넣으세요"));
            pstmt.setInt(3, h_calc);

            pstmt.setInt(4, h_balance);

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}