package org.example;


import java.util.Scanner;

public class ClerkMenu {

    public static void c_main(){
        Scanner sc = new Scanner(System.in);
        AccClerk accClerk = new AccClerk();
        boolean replay = true;
        while(replay) {
            System.out.println("""
                    초록은행 직원 메뉴입니다
                                    
                    1. 마이페이지
                    2. 고객 정보 조회
                    3. 정보 수정
                    4. 계좌 정보 조회
                    5. 입금/출금
                    6. 계좌 관리
                    7. 종료
                    """);

            try {
                switch (sc.nextInt()) {
                    case 1:
//                        dbClerk.myInfo();
                        break;
                    case 2:
//                        dbClerk.customerInfo(sc);
                        break;
                    case 3:
//                        dbClerk.edit(sc);
                        break;
                    case 4:
                        accClerk.select();
                        break;
                    case 5:
//                        dbClerk.IOMoney(sc);
                        break;
                    case 6:
                        new ClerkMenu6();
                        break;
                    case 7:
                        replay = false;
                        break;
                    default:
                        throw new RuntimeException("잘못된 입력");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



}


