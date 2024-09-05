package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    Scanner scan = new Scanner(System.in);
    DBRepository dbRepository = new DBRepository(7);

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }
        public void start(){
            try {
                System.out.println("""
                할 것을 입력하세요
                1.마이페이지 2.정보 수정 3.본인 계좌 조회 4.입금/출금 5.종료
                """);
                switch (scan.nextInt()){
                    case 1 -> dbRepository.myPage();
                    case 2 -> dbRepository.editInformation();
                    case 3 -> dbRepository.checkMyAccount();
                    case 4 -> dbRepository.depositWithdraw();
                    case 5 -> System.out.println("프로그램을 종료합니다");

                }

            }catch (InputMismatchException e){
                System.out.println("숫자만 입력하세요, 재시작 합니다");
                scan.nextLine();
                start();

            }
        }

}