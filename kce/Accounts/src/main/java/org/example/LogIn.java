package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class LogIn {


    private int u_idx;

    public int getU_idx() {
        return u_idx;
    }

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet userInfo = null;
    ResultSet userId = null;

    public loginState login(Scanner sc) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.0.53:8888/Bank",
                    "root",
                    "1234"
            );

            pstmt = conn.prepareStatement("SELECT * FROM users;");
            userInfo = pstmt.executeQuery();
            System.out.println("""
                                        
                    1. 로그인
                    아이디와 비밀번호를 입력 해 주세요.
                    """);
            System.out.print("아이디: ");
            String idInput = sc.next();
            System.out.print("비밀번호: ");
            String passwordInput = sc.next();

            while (userInfo.next()) {
                if (idInput.equals(userInfo.getString("u_id"))) {
                    if (passwordInput.equals(userInfo.getString("u_password"))) {
                        System.out.println("로그인 성공");
                        u_idx = userInfo.getInt("u_idx");
                        if (userInfo.getString("u_level").equals("clerk")) {
                            System.out.println(userInfo.getString("u_name") + " 직원 계정으로 로그인했습니다.");
                            conn.close();
                            return loginState.CLERK;
                        } else {
                            System.out.println(userInfo.getString("u_name") + " 고객님, 반갑습니다.");
                            conn.close();
                            return loginState.CUSTOMER;
                        }
                    } else {
                        System.out.println("비밀번호가 올바르지 않습니다.");
                        conn.close();
                        return loginState.FAILED;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("존재하지 않는 아이디입니다.");
        try {
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginState.FAILED;
    }


}
