import DB.*;
import Menu.*;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("초록은행에 오신 것을 환영합니다.");
        while (true) {
            System.out.println("""
                            
                    메뉴를 선택 해 주세요.
                    1. 로그인
                    2. 회원가입
                    3. 종료
                    """);

            Scanner sc = new Scanner(System.in);
            try {
                switch (sc.nextInt()) {
                    case 1:
                        DBSign signIn = new DBSign();
                        sc.nextLine();
                        if(signIn.isConnection()) {
                            loginState LS = signIn.login(sc);
                            if (LS == loginState.CLERK) {
                                ClerkMenu.c_main(signIn.getU_idx(), sc);
                            } else if (LS == loginState.CUSTOMER) {
//                            CustomerMenu.c_main(signIn.getU_idx());
                            } else {
                                throw new RuntimeException();
                            }
                        }else {
                            throw new RuntimeException();
                        }
                        break;
                    case 2:
                        DBSign signup = new DBSign();
                        sc.nextLine();
                        if(signup.isConnection()) {
                            signup.register(sc);
                        }
                        throw new RuntimeException();
                    case 3:
                        System.out.println("프로그램을 종료합니다");
                        break;
                    default:
                        throw new InputMismatchException();
                }
                break;
            } catch (Exception e) {
                if (e instanceof InputMismatchException) {
                    System.out.println("1이나 2를 입력 해 주세요");
                }
            }
        }
    }
}
