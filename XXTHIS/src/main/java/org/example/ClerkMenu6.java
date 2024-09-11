package org.example;

//ClerkMenu에서 6번 눌렀을 때 여기로 옴(계좌관리)

import org.example.sang.Accounts;

import java.util.Scanner;


public class ClerkMenu6 {

    private AccClerk accClerk = new AccClerk();


    private String accMenu = """
            [계좌 관리]
            1. 신규 계좌 개설
            2. 예금주 관리
            3. 계좌 비밀번호 변경
            4. 계좌 해지
            5. 뒤로
            """;



    ClerkMenu6() {
        Scanner scnn = new Scanner(System.in);
        while (true) {

            System.out.println(accMenu);
            int am = scnn.nextInt();

            if (am == 1) {
                accClerk.insert();
                break;
            } else if (am == 2) {
                accClerk.updateOwners();
                break;
            } else if (am == 3) {
                accClerk.updateApassword();
                break;
            } else if (am == 4) {
                accClerk.delete();
                break;
            } else {
                ClerkMenu.c_main();

            }

        }


    }

    public static void main(String[] args) {
        new ClerkMenu6();
    }
}