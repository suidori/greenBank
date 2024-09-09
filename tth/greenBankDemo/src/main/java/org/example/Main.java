package org.example;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    Scanner scan = new Scanner(System.in);
    DBRepository dbRepository = new DBRepository(1);

    public static void main(String[] args) {
        Main main = new Main();
        main.start();
    }
        public void start(){
            try {
                System.out.println("""
                        안녕하세요, 고객님! 
                        신뢰받는 금융 파트너, **그린은행**에 오신 것을 환영합니다.
                        필요한 서비스를 선택해 주세요.
                        ================================================
                        1.마이페이지 
                        2.개인정보 수정 
                        3.계좌 조회 및 거래내역 확인 
                        4.입금/출금 서비스
                        5.앱 종료
                        """);
                    int input = scan.nextInt();
                    if (input < 1 || input > 5 ) {
                        System.out.print("""
                                유효한 입력은 1에서 5까지의 번호입니다. 재시작합니다.
                                ================================================
                                """);
                        start();
                    }
                    switch (input) {
//                    case 1 -> dbRepository.myPage();
//                    case 2 -> dbRepository.editInformation();
//                    case 3 -> dbRepository.checkMyAccount();
                        case 4 -> dbRepository.depositWithdraw();
                        case 5 -> {
                            System.out.println("이용해주셔서 감사합니다. 서비스를 종료합니다.");
                        }
                    }
                }catch(InputMismatchException e){
                    System.out.println("죄송합니다, 숫자만 입력해 주세요");
                    scan.nextLine();
                    start();
                }
        }

}