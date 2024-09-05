package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DBRepository {
    Scanner scan = new Scanner(System.in);
    public int u_idx;

    public DBRepository(int u_idx) {
        this.u_idx = u_idx;
    }

    public void myPage() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.0.53:8888/Bank",
                    "root", "1234");
            PreparedStatement pstmt = conn.prepareStatement("select * from users where u_id = (?)");
            System.out.print("조회할 아이디를 입력하세요 : ");
            String usersID = scan.nextLine();
            pstmt.setString(1, usersID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("""
                        아이디 = %s
                        비밀번호 = %s
                        이름 = %s
                        전화번호 = %s
                        """.formatted(
                        rs.getString("u_id"),
                        rs.getString("u_password"),
                        rs.getString("u_name"),
                        rs.getString("u_phone")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void editInformation() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.0.53:8888/Bank",
                    "root",
                    "1234");
            PreparedStatement pstmt = conn.prepareStatement("update users set u_password = ?,u_phone =? where u_id = ?");

            System.out.print("아이디를 입력하세요 : ");
            String usersID = scan.nextLine();
            pstmt.setString(3, usersID);
            System.out.print("변경 비밀번호: ");
            String password = scan.nextLine();
            pstmt.setString(1, password);
            System.out.print("변경 연락처: ");
            String phoneNum = scan.nextLine();
            pstmt.setString(2, phoneNum);
            System.out.println("변경 완료 되었습니다.");

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public void checkMyAccount() {
        try {
            Connection conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/GreenBank",
                    "root", "1234");
            PreparedStatement pstmt = conn.prepareStatement("select * from Users where u_id = (?)");
            System.out.print("조회할 아이디를 입력하세요 : ");
            String usersID = scan.nextLine();
            pstmt.setString(1, usersID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("""
                        아이디 = %s
                        비밀번호 = %s
                        이름 = %s
                        전화번호 = %s
                        """.formatted(
                        rs.getString("u_id"),
                        rs.getString("u_password"),
                        rs.getString("u_name"),
                        rs.getString("u_phone")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void depositWithdraw () {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.0.53:8888/Bank",
                    "root", "1234");
            PreparedStatement pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
            boolean done = false;
            while(!done) {
                int accountNumbers;
                ResultSet rs = null;
                try {
                System.out.println("입출금을 진행 하실 계좌번호를 입력하세요");
                accountNumbers = scan.nextInt();
                pstmt.setInt(1, accountNumbers);
                rs = pstmt.executeQuery();
                boolean row = true;
                while (rs.next()) {
                    System.out.println("""
                            현재 귀하의 %s 계좌의 현재 잔액은 : %d 원 입니다.
                            """.formatted(rs.getInt("a_number"),
                            rs.getLong("a_balance")
                    ));
                    row = false;
                }

                if (row) {
                    System.out.println("없는 계좌번호입니다. 다시 적어주시길 바랍니다.");
                    continue;
                }
                }catch (InputMismatchException e){
                    System.out.println("숫자만 입력해주세요");
                    scan.nextLine();
                    continue;
                }
                while (true) {
                    System.out.println("입금은 1번 출금은 2번을 입력해 주세요");
                    if (!scan.hasNextInt()) {
                        System.out.println("숫자만 입력하세요");
                        scan.next();
                        continue;
                    }
                    int choice = scan.nextInt();
                    if (choice == 1 ) {
                        System.out.println("입금할 금액을 입력하세요");
                        long balances = scan.nextLong();
                        pstmt = conn.prepareStatement("update accounts set a_balance = a_balance + ? where a_number = ?");
                        pstmt.setLong(1, balances);
                        pstmt.setInt(2, accountNumbers);
                        pstmt.executeUpdate();

                        pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
                        pstmt.setInt(1, accountNumbers);
                        rs = pstmt.executeQuery();
                        if (rs.next()) {
                            System.out.println("""
                                    입금 후 귀하의 %s 계좌의 현재 잔액은 : %d 원 입니다.
                                    """.formatted(accountNumbers, rs.getLong("a_balance")));

                            pstmt = conn.prepareStatement("insert into history(a_number, u_idx, h_calc , h_balance) values(?,?,?,?)");
                            pstmt.setInt(1, accountNumbers);
                            pstmt.setInt(2, u_idx);
                            pstmt.setLong(3, balances);
                            pstmt.setLong(4, rs.getLong("a_balance"));
                            pstmt.executeUpdate();

                            done = true;
                            break;
                        }
                    }
                        else if (choice == 2 ) {
                        pstmt = conn.prepareStatement("select a_balance from accounts where a_number = ?");
                        pstmt.setInt(1, accountNumbers);
                        rs = pstmt.executeQuery();
                            if (rs.next() && rs.getLong("a_balance") == 0){
                                System.out.println("잔액이 0원이므로 종료됩니다.");
                                break;
                            }
                        Long balances;
                        while (true) {
                            System.out.println("출금할 금액을 입력하세요");
                            balances = scan.nextLong();
                            pstmt.setInt(1, accountNumbers);
                            rs = pstmt.executeQuery();
                            if (rs.next()){
                                long balance = rs.getLong("a_balance");
                            if (balances > balance) {
                                System.out.println("잔액이 부족합니다.");
                                continue;
                            } else {
                                break;
                            }
                            }

                        }
                            pstmt = conn.prepareStatement("update accounts set a_balance = a_balance - ? where a_number = ?");
                            pstmt.setLong(1, balances);
                            pstmt.setInt(2, accountNumbers);
                            pstmt.executeUpdate();

                            pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
                            pstmt.setInt(1, accountNumbers);
                            rs = pstmt.executeQuery();
                            if (rs.next()) {
                                System.out.println("""
                                    출금 후 귀하의 %s 계좌의 현재 잔액은 : %d 원 입니다.
                                    """.formatted(accountNumbers, rs.getLong("a_balance")));

                                pstmt = conn.prepareStatement("insert into history(a_number, u_idx, h_calc , h_balance) values(?,?,?,?)");
                                pstmt.setInt(1, accountNumbers);
                                pstmt.setInt(2, u_idx);
                                pstmt.setLong(3, -balances);
                                pstmt.setLong(4, rs.getLong("a_balance"));
                                pstmt.executeUpdate();

                                done = true;

                                break;
                            }
                    } else {
                        System.out.println("1또는 2만 입력하세요");
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        }
    }

