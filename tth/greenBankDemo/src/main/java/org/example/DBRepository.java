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

    public void depositWithdraw () {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.0.53:8888/Bank",
                    "root", "1234");
            PreparedStatement pstmt = conn.prepareStatement("select * from owners where u_idx = ?");
            pstmt.setInt(1, u_idx);
            boolean done = false;
            while(!done) {
                int accountNumbers;
                ResultSet rs = null;
                rs = pstmt.executeQuery();
                try {
                System.out.print("""
                        현재 접속한 본인의 전체 계좌번호 목록을 조회합니다.
                        =====================================
                        """);
                while (rs.next()) {
                        System.out.print("""
                        계좌번호 : %d
                        """.formatted(
                                rs.getInt("a_number")));
                    }
                    System.out.println("""
                        =====================================
                        입출금을 진행 하실 계좌번호를 입력하세요
                        """);
                pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
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

