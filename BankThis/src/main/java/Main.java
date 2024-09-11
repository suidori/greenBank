import DB.*;
import Menu.ClerkMenu;
<<<<<<< HEAD
=======
import Menu.CustomerMenu;
>>>>>>> main

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("초록은행에 오신 것을 환영합니다.");
        while (true) {
            System.out.println("""

                    메뉴를 선택 해 주세요.
                    1. 로그인
                    2. 회원가입
                    3. 종료
                    """);
            try {
                switch (sc.nextInt()) {
                    case 1:
                        DBSign signIn = new DBSign();
                        sc.nextLine();
                        if (signIn.isConnection()) {
                            switch(signIn.login(sc)){
                                case CLERK:
                                    ClerkMenu clerkMenu = new ClerkMenu(signIn.conn);
                                    clerkMenu.c_main(signIn.getU_idx(), sc);
                                case CUSTOMER:
<<<<<<< HEAD
                                    //고객 메뉴 실행
=======
                                    CustomerMenu customerMenu = new CustomerMenu(signIn.conn);
                                    customerMenu.c_main(signIn.getU_idx(), sc);
>>>>>>> main
                                case FAILED:
                                    throw new RuntimeException();
                            }
                        }
                        break;
                    case 2:
                        DBSign signup = new DBSign();
                        sc.nextLine();
                        if (signup.isConnection()) {
                            signup.register(sc);
                        } else {
                            break;
                        }
                        throw new RuntimeException();
                    case 3:
                        System.out.println("프로그램을 종료합니다");
                        break;
                    default:
                        throw new InputMismatchException();
                }
            } catch (Exception e) {
                if (e instanceof InputMismatchException) {
                    System.out.println("올바른 숫자를 입력 해 주세요");
                    sc.nextLine();
                    continue;
                }
            }
            break;
        }
    }
}
