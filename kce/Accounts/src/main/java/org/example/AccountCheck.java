package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AccountCheck {

    public static Accounts findByAccount(int a_number, int a_password) {
        try (Connection conn
                     = DriverManager.getConnection(
                "jdbc:mysql://192.168.0.53:8888/Bank", "root", "1234")) {

            PreparedStatement pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
            pstmt.setInt(1, a_number);
            pstmt.setInt(2, a_password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return Accounts.builder()
                        .a_number(rs.getInt("a_number"))
//  ????????????????                      .a_password(rs.getInt("a_password"))           비밀번호도 뜨게해야하나?
                        .a_balance(rs.getInt("a_balance"))
                        .build();
            } else {
                return null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public static void aInsert(){

    }


    public static void aUpdate(){

    }


    public static void aDelete(){

    }







}




