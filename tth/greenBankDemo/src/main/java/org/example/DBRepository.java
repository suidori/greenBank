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

    public void depositWithdraw() {
        int flag = 0;
        try {
            while (true) {
            Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.0.53:8888/Bank", "root", "1234");
            PreparedStatement pstmt = conn.prepareStatement("select * from owners where u_idx = ?");
            pstmt.setInt(1, u_idx);
                int accountNumbers;
                ResultSet rs = pstmt.executeQuery();
                try {
                    System.out.print("현재 접속한 본인의 전체 계좌번호 목록을 조회합니다.\n=====================================\n");
                    while (rs.next()) {
                        System.out.println("계좌번호: " + rs.getInt("a_number"));
                    }
                    System.out.println("=====================================\n입출금을 진행할 계좌번호를 입력하세요:");
                    accountNumbers = scan.nextInt();

                    pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
                    pstmt.setInt(1, accountNumbers);
                    rs = pstmt.executeQuery();

                    if (!rs.next()) {
                        System.out.println("없는 계좌번호입니다. 다시 적어주세요.");
                        continue;
                    }

                    System.out.println("현재 귀하의 계좌 잔액은: " + rs.getLong("a_balance") + "원입니다.");
                } catch (InputMismatchException e) {
                    System.out.println("숫자만 입력해주세요.");
                    scan.nextLine();  // 버퍼를 비워줌
                    continue;
                }

                while (true) {
                    System.out.println("입금: 1번, 출금: 2번, 돌아가기: 3번 입력해 주세요.");
                    if (!scan.hasNextInt()) {
                        System.out.println("숫자만 입력하세요.");
                        scan.next();
                        continue;
                    }

                    int choice = scan.nextInt();
                    if (choice == 1) {
                        try {
                            System.out.println("입금할 금액을 입력하세요:");
                            long balances = scan.nextLong();
                            pstmt = conn.prepareStatement("update accounts set a_balance = a_balance + ? where a_number = ?");
                            pstmt.setLong(1, balances);
                            pstmt.setInt(2, accountNumbers);
                            pstmt.executeUpdate();

                            pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
                            pstmt.setInt(1, accountNumbers);
                            rs = pstmt.executeQuery();

                            if (rs.next()) {
                                System.out.println("입금 후 잔액은: " + rs.getLong("a_balance") + "원입니다.");
                                flag = 1;
                                break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("숫자만 입력해주세요.");
                            scan.nextLine();
                        }
                    } else if (choice == 2) {
                        try {
                            pstmt = conn.prepareStatement("select a_balance from accounts where a_number = ?");
                            pstmt.setInt(1, accountNumbers);
                            rs = pstmt.executeQuery();

                            if (rs.next() && rs.getLong("a_balance") == 0) {
                                System.out.println("잔액이 0원입니다.");
                                break;
                            }

                            System.out.println("출금할 금액을 입력하세요:");
                            long balances = scan.nextLong();

                            if (balances > rs.getLong("a_balance")) {
                                System.out.println("잔액이 부족합니다.");
                                continue;
                            }

                            pstmt = conn.prepareStatement("update accounts set a_balance = a_balance - ? where a_number = ?");
                            pstmt.setLong(1, balances);
                            pstmt.setInt(2, accountNumbers);
                            pstmt.executeUpdate();

                            pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
                            pstmt.setInt(1, accountNumbers);
                            rs = pstmt.executeQuery();

                            if (rs.next()) {
                                System.out.println("출금 후 잔액은: " + rs.getLong("a_balance") + "원입니다.");
                                flag = 1;
                            break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("숫자만 입력해주세요.");
                            scan.nextLine();
                        }
                    } else if (choice == 3) {
                        System.out.println("종료합니다.");
                        flag = 1;
                        break;
                    } else {
                        System.out.println("1, 2, 3만 입력하세요.");
                    }


                }
                if (flag == 1){
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
