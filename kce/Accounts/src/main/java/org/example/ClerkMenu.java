package org.example;
/*
직원메뉴-4번
 [계좌 정보 조회] (최근 거래 내역 5개)(select)
 ->계좌번호 입력 + 비밀번호 입력(int)
    -다시입력

직원메뉴-6번
[계좌 개설 및 수정] (insert+update+delete)
(개설)
1. 신규개설
u_idx,a_password
2. 계좌삭제
삭제할 a_number -> a_password
(수정)
1.계좌 소유주 수정
수정할 a_number -> 1.추가 / 2.삭제 u_idx
2.계좌 비밀번호 수정
수정할 a_number -> 수정 a_password
 */

import java.util.Scanner;

public class ClerkMenu {
    private AccountCheck accountCheck =
            new AccountCheck();

    private String menu = """
            초록은행 직원 메뉴입니다
                                    
                    1. 마이페이지
                    2. 고객 정보 조회
                    3. 정보 수정
                    4. 계좌 정보 조회
                    5. 입금/출금
                    6. 계좌 관리
                    7. 종료
                    """;

    public static Accounts ACCOUNT;


    ClerkMenu() {
        Scanner scnn = new Scanner(System.in);
        while (true) {
            System.out.println(menu);
            int sc = scnn.nextInt();

            if (sc == 1) {

            } else if (sc == 2) {

            } else if (sc == 3) {

            } else if (sc == 4) {
                System.out.println("[계좌 정보 조회]");
                System.out.print("계좌번호 입력: ");
                int aNumber = scnn.nextInt();
                System.out.println();
                System.out.print("비밀번호 입력: ");
                int aPassword = scnn.nextInt();
                System.out.println();
                ClerkMenu.ACCOUNT = AccountCheck.findByAccount(aNumber, aPassword);

                if (ACCOUNT == null) {
                    System.out.println("계좌번호 또는 비밀번호를 정확하게 입력해주세요.");
                } else {
                    System.out.println("계좌정보를 가져옵니다.");
                }
                System.out.println(ClerkMenu.ACCOUNT);

            } else if (sc == 5) {

            } else if (sc == 6) {
                while (true) {
                    System.out.println("[계좌 관리]");
                    System.out.println("""
                            1. 신규 계좌 개설
                            2. 예금주 수정
                            3. 계좌 해지
                            4. 뒤로
                            """);
                    int ac = scnn.nextInt();

                    if (ac == 1) {
                        AccountCheck.aInsert();
                    } else if (ac == 2) {
                        AccountCheck.aUpdate();
                    } else if (ac == 3) {
                        AccountCheck.aDelete();
//??????메뉴선택으로 쓰이는 애들을 ClerkMenu로 다 빼버릴까...?
                    } else {
//                        new ClerkMenu();
                    }

                }


            } else {
                System.out.println("종료됩니다.");
                break;
            }
        }

    }






}

