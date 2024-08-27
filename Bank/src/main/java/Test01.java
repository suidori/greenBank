import java.sql.*;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test01 {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Scanner sc = new Scanner(System.in);

        System.out.println("이름을 입력 해 주세요.");
        String nameInput = sc.nextLine();
        System.out.printf("당신의 이름은 %s 입니다.\n", nameInput);

        System.out.println("전화번호를 입력 해 주세요.");
        String phoneInput = sc.nextLine();

        String regex = "\\d{3}-\\d{4}-\\d{4}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneInput);

        if (matcher.matches()) {
            System.out.printf("당신의 전화번호는 %s 입니다.\n", phoneInput);
        } else {
            phoneInput = phoneInput.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
            System.out.printf("당신의 전화번호는 %s 입니다.\n", phoneInput);
        }
    }
}
