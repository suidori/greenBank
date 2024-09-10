package org.example;

//ClerkMenu에서 6번 눌렀을 때 여기로 옴(계좌관리)

import java.sql.SQLException;
import java.util.Scanner;


public class ClerkMenu6 {

    AccClerk accClerk = new AccClerk();


    Scanner scnn = new Scanner(System.in);
    String accMenu = """
            [계좌 관리]
            1. 신규 계좌 개설
            2. 예금주 관리
            3. 계좌 비밀번호 변경
            4. 뒤로
            """;

    ClerkMenu6() throws SQLException {
        while (true) {
            System.out.println(accMenu);
            int am = scnn.nextInt();

            if (am == 1) {
                accClerk.insert();
                break;
            } else if (am == 2) {
                ownersManage();
                break;
            } else if (am == 3) {
                accClerk.updateApassword();
                break;
            } else {
                ClerkMenu.c_main();

            }


        }

    }


    //

    public static void ownersManage() throws SQLException {
        AccClerk accClerk = new AccClerk();
        Scanner scnn = new Scanner(System.in);
        System.out.println("""
                예금주 관리
                1. 추가
                2. 삭제         
                3. 예금주 조회               
                4. 뒤로
                """);

        while (true) {

            switch (scnn.nextInt()) {
                case 1:
                    accClerk.updateOwners();
                    break;
                case 2:
                    accClerk.delOwners();
                    break;
                case 3:
                    accClerk.checkOwners();
                    break;
                case 4:
                    new ClerkMenu6();

                default:
                    throw new RuntimeException("잘못된 입력");
            }

        }

    }


    public static void main(String[] args) throws SQLException {
        new ClerkMenu6();
    }
}