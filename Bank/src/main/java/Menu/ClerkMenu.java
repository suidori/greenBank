package Menu;

import DB.DBClerk;

import java.util.Scanner;

public class ClerkMenu {
    public static void c_main(int u_idx, Scanner sc){
        DBClerk dbClerk = new DBClerk(u_idx);
        while(true) {
            System.out.println("""
                    초록은행 직원 메뉴입니다
                                    
                    1. 마이페이지
                    2. 고객 정보 조회
                    3. 정보 수정
                    4. 계좌 정보 조회
                    5. 입금/출금
                    6. 계좌 개설
                    7. 회원 탈퇴
                    8. 종료
                    """);

            try {
                switch (sc.nextInt()) {
                    case 1:
                        dbClerk.myInfo();
                        break;
                    case 2:
                        dbClerk.customerInfo(sc);
                        break;
                    case 3:
                        dbClerk.edit(sc);
                        break;
                    case 4:
                        dbClerk.accountInfo(sc);
                        break;
                    case 5:
                        dbClerk.IOMoney(sc);
                        break;
                    case 6:
                        dbClerk.accountEdit(sc);
                        break;
                    case 7:

                    case 8:

                    default:
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
