import DB.*;
import Menu.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DBSign dbSign = new DBSign();
        System.out.println("초록은행에 오신 것을 환영합니다.");
        while(true) {
            System.out.println("""
                            
                    메뉴를 선택 해 주세요.
                    1. 로그인
                    2. 회원가입
                    """);

            Scanner sc = new Scanner(System.in);
            try {
                switch (sc.nextInt()) {
                    case 1:
                        loginState LS = dbSign.login(sc);
                        if(LS == loginState.CLERK){
//                            ClerkMenu.c_main(dbSign.getU_idx(), sc);
                        }else if (LS==loginState.CUSTOMER){
//                            CustomerMenu.c_main(dbSign.getU_idx());
                        }else{
                            throw new RuntimeException();
                        }
                        break;
                    case 2:
                        if(dbSign.register(sc)){
                            System.out.println("회원가입이 완료되었습니다.");
                        }else{
                            System.out.println("회원가입에 실패했습니다.");
                        }
                        throw new RuntimeException();
                    default:
                        throw new InputMismatchException();
                }
                break;
            } catch (Exception e) {
                if(e instanceof InputMismatchException) {
                    System.out.println("1이나 2를 입력 해 주세요");
                }
            }
        }
    }
}
