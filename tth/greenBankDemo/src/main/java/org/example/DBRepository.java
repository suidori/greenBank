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
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/Bank", "root", "1234");
            PreparedStatement pstmt = conn.prepareStatement("select * from owners where u_idx = ?");
            pstmt.setInt(1, u_idx);
                int accountNumbers;
                ResultSet rs = pstmt.executeQuery();
                try {
                    System.out.print("고객님의 모든 계좌 목록을 확인 중입니다.\n=====================================\n");
                    int count = 1;
                    while (rs.next()) {
                        System.out.println(count+". 계좌번호: " + rs.getInt("a_number"));
                        count++;
                    }
                    System.out.println("=====================================\n입출금을 원하시는 계좌번호를 입력해 주세요(0번 입력시 메인매뉴로 돌아갑니다.)");

                    accountNumbers = scan.nextInt();
                    if (accountNumbers == 0) {
                        break;
                    }
                    //계좌검증하는곳
                    pstmt = conn.prepareStatement("select * from owners where a_number = ?");
                    pstmt.setInt(1, accountNumbers);
                    rs = pstmt.executeQuery();

                    if (rs.next()){
                    int verification = rs.getInt("u_idx");
                    if (verification != u_idx){
                        System.out.println("본인 소유의 계좌번호만 입력 가능합니다. 다시 시도해 주세요.");
                        continue;
                      }
                    }

                    pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
                    pstmt.setInt(1, accountNumbers);
                    rs = pstmt.executeQuery();

                    if (!rs.next()) {
                        System.out.println("존재하지 않는 계좌번호입니다. 다시 한 번 확인해 주세요.");
                        continue;
                    }

                    System.out.println("현재 고객님의 계좌 잔액은 " + rs.getLong("a_balance") + "원입니다.");
                } catch (InputMismatchException e) {
                    System.out.println("죄송합니다, 숫자만 입력해 주세요.");
                    scan.nextLine();
                    continue;
                }
                //여기로 돌아가게 하고싶음
                    System.out.println("""
                            입금은 1번, 출금은 2번, 계좌 다시 선택은 3번,
                            서비스를 종료하시려면 4번을 입력해 주세요.
                            """);

                    if (!scan.hasNextInt()) {
                        System.out.println("죄송합니다, 숫자만 입력해 주세요.");
                        System.out.println("계좌 선택화면으로 돌아갑니다.");
                        scan.next();
                        continue;
                    }
                    int choice = scan.nextInt();
                    if(choice < 1 || choice > 4 ){
                        scan.nextLine();
                        System.out.println("유효하지 않은 번호입니다. 1번부터 4번 사이의 번호를 입력해 주세요.");
                        System.out.println("계좌 선택화면으로 돌아갑니다.");
                        continue;
                    }
                while (true) {
                    if (choice == 1) {
                            System.out.println("입금하실 금액을 입력해 주세요.");
                        try {
                            long balances = scan.nextLong();
                            if (balances < 0){
                                System.out.println("입금 금액은 음수일 수 없습니다. 다시 올바른 금액을 입력해 주세요.");
                                continue;
                            }
                            pstmt = conn.prepareStatement("update accounts set a_balance = a_balance + ? where a_number = ?");
                            pstmt.setLong(1, balances);
                            pstmt.setInt(2, accountNumbers);
                            pstmt.executeUpdate();

                            pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
                            pstmt.setInt(1, accountNumbers);
                            rs = pstmt.executeQuery();

                            if (rs.next()) {
                                System.out.println("입금 후 잔액은 " + rs.getLong("a_balance") + "원입니다.");
                                System.out.println("이용해주셔서 감사합니다. 서비스를 종료합니다.");
                                pstmt = conn.prepareStatement("insert into history(a_number,u_idx,h_calc,h_balance) values(?,?,?,?)");
                                pstmt.setInt(1,accountNumbers);
                                pstmt.setInt(2,u_idx);
                                pstmt.setLong(3,balances);
                                pstmt.setLong(4,rs.getLong("a_balance"));
                                pstmt.executeUpdate();
                                flag = 1;
                                break;

                            }
                        } catch (InputMismatchException e) {
                            System.out.println("죄송합니다, 숫자만 입력해 주세요.");
                            scan.nextLine();
                        }
                    }
                    else if (choice == 2) {
                        try {
                            pstmt = conn.prepareStatement("select a_balance from accounts where a_number = ?");
                            pstmt.setInt(1, accountNumbers);
                            rs = pstmt.executeQuery();

                            if (rs.next() && rs.getLong("a_balance") == 0) {
                                System.out.println("잔액이 0원이므로 출금 진행이 불가능합니다. 계좌 선택 화면으로 돌아갑니다.");
                                break;
                            }

                            System.out.println("출금하실 금액을 입력해 주세요.");
                            long balances = scan.nextLong();
                            if (balances < 0){
                                System.out.println("출금 금액은 음수일 수 없습니다. 다시 올바른 금액을 입력해 주세요.");
                                continue;
                            }
                            if (balances > rs.getLong("a_balance")) {
                                System.out.println("잔액이 부족하여 요청하신 금액을 출금할 수 없습니다. 다시 금액을 입력해 주세요.");
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
                                System.out.println("출금 후 잔액은 " + rs.getLong("a_balance") + "원입니다.");
                                System.out.println("이용해주셔서 감사합니다. 서비스를 종료합니다.");
                                pstmt = conn.prepareStatement("insert into history(a_number,u_idx,h_calc,h_balance) values(?,?,?,?)");
                                pstmt.setInt(1,accountNumbers);
                                pstmt.setInt(2,u_idx);
                                pstmt.setLong(3,-balances);
                                pstmt.setLong(4,rs.getLong("a_balance"));
                                pstmt.executeUpdate();
                                flag = 1;
                            break;
                            }
                        } catch (InputMismatchException e) {
                            System.out.println("죄송합니다, 숫자만 입력해 주세요.");
                            scan.nextLine();
                        }
                    }

                    else if (choice == 3) {
                        break;
                    }

                    else if (choice == 4) {
                        System.out.println("이용해주셔서 감사합니다. 서비스를 종료합니다.");
                        flag = 1;
                        break;
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
