import java.util.Scanner;

public class Test01 {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        try {
            int a = sc.nextInt();
            if (a>10){
                throw new RuntimeException();
            }
            System.out.println(a);
        }catch(Exception e){
            System.out.println("숫자만 입력하세요!");
        }

    }
}
