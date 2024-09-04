package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class DBSign {

    private boolean connection;
    private int u_idx;

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    public DBSign() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.0.53:8888/Bank",
                    "root",
                    "1234"
            );
            connection = true;
        } catch (Exception e) {
            System.out.println("연결 실패");
            connection = false;
        }
    }

    public boolean isConnection() {
        return connection;
    }

    public loginState login(Scanner sc) {
        System.out.println("아이디: ");
        String inputId = sc.nextLine();
        System.out.println("비밀번호: ");
        String passwordInput = sc.nextLine();

        try {
            pstmt = conn.prepareStatement("SELECT * FROM users WHERE u_id = ? AND u_password = ?");
            pstmt.setString(1, inputId);
            pstmt.setString(2, passwordInput);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.printf("권한: %s\n", (rs.getString("u_level").equals("clerk")) ? "직원" : "고객");
                System.out.printf("%s님, 반갑습니다.");
                return (rs.getString("u_level").equals("clerk")) ? loginState.CLERK : loginState.CUSTOMER;
            } else {
                System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
                return loginState.FAILED;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginState.FAILED;
    }

    public boolean register(Scanner sc) {
        System.out.println("회원 가입을 진행합니다.");

        boolean repeat = true;
        String inputId;
        String inputPassword;
        try {
            while (repeat) {
                System.out.println("""
                                        
                        초기 화면으로 돌아가고 싶다면 '취소'를 입력 해 주세요.
                        아이디는 영어 대소문자, 숫자만 입력 가능합니다.
                        아이디:
                        """);
                inputId = sc.nextLine();
                cancle(inputId);
                repeat = !isValidString(inputId);
            }
            while (repeat) {
                System.out.println("""
                                        
                        초기 화면으로 돌아가고 싶다면 '취소'를 입력 해 주세요.
                        비밀번호는 영어 대소문자, 숫자만 입력 가능합니다.
                        비밀번호:
                        """);
                inputPassword= sc.nextLine();

                cancle(inputPassword);
                repeat = !isValidString(inputPassword);
            }
        }catch (Exception e){
            System.out.println("초기 화면으로 돌아갑니다.");
        }

    }

    private static boolean isValidString(String input) {
        String regex = "^[a-zA-Z0-9]+$";

        if (input == null) {
            return false;
        }else if(!input.matches(regex)){
            return false;
        }

    }

    private static void cancle(String input){
        if(input.equals("취소")){
            throw new RuntimeException("회원 가입 취소");
        }
    }

}