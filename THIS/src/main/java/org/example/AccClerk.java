package org.example;

import org.example.sang.Accounts;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class AccClerk {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Connection conn = null;
    Accounts ACCOUNT = null;

    public AccClerk() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.0.53:8888/Bank",
                    "root",
                    "1234"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static void select() {
        System.out.println("4번->계좌정보");

    }

    public void insert() {
        Scanner sc = new Scanner(System.in);
        System.out.println("신규계좌개설");
        System.out.print("회원번호: ");

        try {
            int uIdx = sc.nextInt();
            pstmt = conn.prepareStatement("SELECT * FROM users WHERE u_idx = ?");
            pstmt.setInt(1, uIdx);
            rs = pstmt.executeQuery();
            if (rs.next()) {

                System.out.print("비밀번호: ");
                int aPassword = sc.nextInt();


                pstmt = conn.prepareStatement(
                        "INSERT INTO accounts (a_balance,a_password) VALUES (0,?);");
                pstmt.setInt(1, aPassword);
                pstmt.executeUpdate();

                //@last_a_number = LAST_INSERT_ID(); 어케야하는지.....

                pstmt = conn.prepareStatement(
                        "INSERT INTO owners (u_idx, a_number) VALUES (?,?);");
                pstmt.setInt(1, uIdx);
                pstmt.setInt(2,???????????????? );
                pstmt.executeUpdate();

                System.out.print("계좌가 개설되었습니다. 계좌번호는 [");
//                pstmt.executeQuery("SELECT LAST_INSERT_ID()");
                pstmt = conn.prepareStatement("SELECT * FROM accounts WHERE a_number = @last_a_number;");
                rs = pstmt.executeQuery();
                System.out.println(rs.getInt("a_number") + "] 입니다");

            } else {
                System.out.println("존재하지 않는 사용자입니다.");

            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


//    public static void insert() {
//        Scanner sc = new Scanner(System.in);
//
//        System.out.println("신규계좌개설");
//
//        System.out.print("회원번호: ");
//        int uIdx = sc.nextInt();
//        System.out.print("비밀번호: ");
//        int aPassword = sc.nextInt();
//
//
//        try (Connection conn
//                     = DriverManager.getConnection(
//                "jdbc:mysql://192.168.0.53:8888/Bank", "root", "1234")) {
//
//            PreparedStatement pstmt = conn.prepareStatement("select * from user where idx = ?");
//            pstmt.setInt(1, uIdx);
//            ResultSet rs = pstmt.executeQuery();
//
//            if (rs.next()) {
//                //사용자 존재
//                try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO accounts (a_balance, a_password) VALUES (0, ?);" +
//                        "SET @last_a_number = LAST_INSERT_ID();" +
//                        "INSERT INTO owners (u_idx, a_number) VALUES (?, @last_a_number);")) {
//                    pstmt.setString(1, aPassword);
//                    pstmt.setInt(2, uIdx);
//                    pstmt.executeUpdate();
//
//
//                    System.out.print("계좌가 개설되었습니다. 계좌번호는 [");
//                    pstmt = conn.prepareStatement("SELECT * FROM accounts WHERE a_number = @last_a_number;");
//                    rs = pstmt.executeQuery();
//                    System.out.println(rs.getInt("a_number") + "] 입니다");
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            } else {
//                System.out.println("존재하지 않는 사용자입니다.");
//
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public void updateOwners() {
        System.out.println("소유주 추가 및 삭제");


    }


    public void delete() {
        System.out.println("계좌해지");


    }


    public void updateApassword() {
        System.out.println("비밀번호 변경");


    }


}
