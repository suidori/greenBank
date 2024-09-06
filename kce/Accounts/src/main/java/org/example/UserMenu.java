package org.example;
/*
고객메뉴-3번
[본인 계좌 조회](select)
1.전체조회
->로그인 된 사용자의 u_idx에 맞는 계좌번호,잔액 출력해줌
 */


import java.util.Scanner;

public class UserMenu {

    private AccountCheck accountCheck =
            new AccountCheck();

    private String menu = """
            초록은행 고객 메뉴입니다
                                    
                    1. 마이페이지
                    2. 정보 수정
                    3. 계좌 조회
                    4. 입금/출금
                    5. 계좌 개설/수정
                    6. 종료
                    """;

    public static Accounts ACCOUNT;

    UserMenu() {
        Scanner scnn = new Scanner(System.in);
        while (true) {
            System.out.println(menu);
            int sc = scnn.nextInt();

            if (sc == 1) {

            } else if (sc == 2) {

            } else if (sc == 3) {
                System.out.println("[계좌 정보 조회]");
                System.out.print("계좌번호 입력: ");
                int aNumber = scnn.nextInt();
                System.out.println();
                System.out.println("비밀번호 입력: ");
                int aPassword = scnn.nextInt();
                ClerkMenu.ACCOUNT = AccountCheck.findByAccount(aNumber, aPassword);

                if (ACCOUNT == null) {
                    System.out.println("계좌번호 또는 비밀번호를 정확하게 입력해주세요.");
                } else {
                    System.out.println("계좌정보를 가져옵니다.");
                }
                System.out.println(ClerkMenu.ACCOUNT);


            } else if (sc == 4) {

            } else if (sc == 5) {


            } else {
                System.out.println("종료됩니다.");
                break;
            }

        }
    }
}
