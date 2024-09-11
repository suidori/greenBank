package Menu;

import java.sql.Connection;
import java.util.Scanner;

public class ClerkMenu {
    private Connection conn;

    public ClerkMenu(Connection conn) {
        this.conn = conn;
    }

    public void c_main(int u_idx, Scanner sc){
        DBClerk dbClerk = new DBClerk();
        ClerkDetailedMenu clerkDetailedMenu = new ClerkDetailedMenu();      //<<<<<<<<<<<<<<<<<<<<<<<<<<
        while(true) {
            System.out.println("""
                    초록은행 직원 메뉴입니다
                                    
                    1. 마이페이지
                    2. 내 정보 수정 
                    3. 고객 정보 조회 
                    4. 고객 정보 수정
                    5. 계좌 정보 조회
                    6. 입금/출금
                    7. 계좌 관리
                    8. 종료
                    """);

            try {
                switch (sc.nextInt()) {
                    case 1:
                        dbClerk.myPage(u_idx);
                        throw new Exception("메인 메뉴로");
                    case 2:
                        dbClerk.myPageEdit();
                        dbClerk.myPage(u_idx);
                        throw new Exception("메인 메뉴로");
                    case 3:
                        dbClerk.customerInfo();
                        throw new Exception("메인 메뉴로");
                    case 4:
                        int temp = dbClerk.custInfoEdit();
                        dbClerk.myPage(temp);
                        throw new Exception("메인 메뉴로");
                    case 5:
                        dbClerk.selectMyAcc(u_idx);
                    case 6:
                        dbClerk.depositWithdraw();
                        throw new Exception("메인 메뉴로");
                    case 7:
                        clerkDetailedMenu.cdm();                     //<<<<<<<<<<<<<<<<<<<<<<<<<<
                        throw new Exception("메인 메뉴로");
                    case 8:
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
