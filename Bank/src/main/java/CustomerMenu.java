import java.util.Scanner;

public class CustomerMenu {
    public static void c_main() {

        Scanner scan = new Scanner(System.in);

        System.out.println("""
                고객의 은행고유번호 입력하시오.
                """);
        int num1 = scan.nextInt();

        System.out.println("""
                원하는 메뉴를 선택해주세요.
                1. 내 정보 조회
                2. 고객의 정보 조회
                3. 내 정보 수정
                4. 고객 정보 수정
                """);
        int num2 = scan.nextInt();

        CustomerDBRepository customerDBRepository = new CustomerDBRepository();

        switch (num2){
            case 1:
                customerDBRepository.myPage(num1);
                break;

            case 2:
                customerDBRepository.custInfo();
                break;

            case 3:
                customerDBRepository.myPage(num1);
                customerDBRepository.myPageEdit(num1);
                customerDBRepository.myPage(num1);
                break;

            case 4:
                customerDBRepository.custInfoEdit();
                customerDBRepository.custInfo();
        }
    }
}