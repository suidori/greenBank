package Menu;

import DB.DBClerk;
import DB.DBCustomer;

import java.sql.Connection;
import java.util.Scanner;

public class CustomerMenu {
    private Connection conn;

    public CustomerMenu(Connection conn) {
        this.conn = conn;
    }

    public void c_main(int u_idx, Scanner sc){
        DBCustomer dbCustomer = new DBCustomer(u_idx, sc, conn);
        ClerkDetailedMenu clerkDetailedMenu = new ClerkDetailedMenu();      //<<<<<<<<<<<<<<<<<<<<<<<<<<
        while(true) {
            System.out.println("""
                    초록은행 고객 메뉴입니다
                                    
                    1. 마이페이지
                    2. 내 정보 수정 
                    3. 내 계좌 조회
                    4. 계좌 정보 조회
                    5. 입금/출금
                    6. 종료
                    """);

            try {
                switch (sc.nextInt()) {
                    case 1:
                        dbCustomer.myPage(u_idx);
                        throw new Exception("메인 메뉴로");
                    case 2:
                        dbCustomer.myPageEdit();
                        dbCustomer.myPage(u_idx);
                        throw new Exception("메인 메뉴로");
                    case 3:
                        dbCustomer.selectMyAcc();
                        throw new Exception("메인 메뉴로");
                    case 4:
                        dbCustomer.select();
                        throw new Exception("메인 메뉴로");
                    case 5:
                        dbCustomer.depositWithdraw();
                        throw new Exception("메인 메뉴로");
                    case 6:
                        break;
                    default:
                        throw new RuntimeException("잘못된 입력");
                }
                break;
            } catch (Exception e) {
                if(e instanceof RuntimeException){
                    e.printStackTrace();
                }else {
                    System.out.println();
                }
            }
        }
    }
}
