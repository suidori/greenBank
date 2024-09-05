package DB;

import java.sql.*;
import java.util.Scanner;

public class DBSign {

    private boolean connection;
    private int u_idx;

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    public int getU_idx() {
        return u_idx;
    }

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
        System.out.println("로그인 메뉴입니다.");
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
                Thread.sleep(1000);
                System.out.printf("%s님, 반갑습니다.\n", rs.getString("u_name"));
                Thread.sleep(1000);
                u_idx = rs.getInt("u_idx");
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

    public void register(Scanner sc) throws SQLException {
        System.out.println("회원 가입을 진행합니다.");

        String inputId;
        String inputPassword;
        String inputName;
        String inputPhone;
        try {
            while (true) {
                System.out.println("""
                                        
                        초기 화면으로 돌아가고 싶다면 '취소'를 입력 해 주세요.
                        아이디는 영어 대소문자, 숫자만 입력 가능합니다.
                        아이디:
                        """);
                inputId = sc.nextLine();
                cancel(inputId);
                if (!isValidString(inputId)) {
                    continue;
                }
                pstmt = conn.prepareStatement("SELECT * FROM users WHERE u_id = ?");
                pstmt.setString(1, inputId);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    System.out.println("중복된 아이디입니다.");
                } else {
                    break;
                }
            }
            while (true) {
                System.out.println("""
                                        
                        초기 화면으로 돌아가고 싶다면 '취소'를 입력 해 주세요.
                        비밀번호는 영어 대소문자, 숫자만 입력 가능합니다.
                        비밀번호:
                        """);
                inputPassword = sc.nextLine();
                cancel(inputPassword);
                if (isValidString(inputPassword)) {
                    break;
                }
            }
            while (true) {
                System.out.println("""
                                        
                        초기 화면으로 돌아가고 싶다면 '취소'를 입력 해 주세요.
                        이름은 영문이나 한글만 사용할 수 있습니다.
                        이름:
                        """);
                inputName = sc.nextLine();
                cancel(inputName);
                if (isValidName(inputName)) {
                    break;
                }
            }
            while (true) {
                System.out.println("""
                                        
                        초기 화면으로 돌아가고 싶다면 '취소'를 입력 해 주세요.
                        전화번호는 '010-1234-5678' 또는 '01012345678'의 형태로 입력 해 주세요.
                        전화번호:
                        """);
                inputPhone = sc.nextLine();
                cancel(inputPhone);
                if (isValidPhone(inputPhone)) {
                    String noHyphen = "^\\d{11}$";

                    if (inputPhone.matches(noHyphen)) {
                        inputPhone = inputPhone.substring(0, 3) + "-" + inputPhone.substring(3, 7) + "-" + inputPhone.substring(7);
                    }
                    break;
                }
            }

            pstmt = conn.prepareStatement("INSERT INTO users (u_id, u_password, u_name, u_phone) VALUES (?, ?, ?, ?);");
            pstmt.setString(1, inputId);
            pstmt.setString(2, inputPassword);
            pstmt.setString(3, inputName);
            pstmt.setString(4, inputPhone);
            pstmt.executeUpdate();

            System.out.printf("""
                    회원 가입이 완료되었습니다.
                    회원 정보
                    아이디: %s
                    비밀번호: %s
                    이름: %s
                    전화번호: %s
                    %n""", inputId, inputPassword, inputName, inputPhone);

        } catch (Exception e) {
            System.out.println("초기 화면으로 돌아갑니다.");
        }
        conn.close();
    }

    private boolean isValidString(String input) {
        String regex = "^[a-zA-Z0-9]+$";

        if (input.isEmpty() || input.isBlank()) {
            return false;
        }

        return input.matches(regex);
    }

    private static void cancel(String input) {
        if (input.equals("취소")) {
            throw new RuntimeException("회원 가입 취소");
        }
    }

    private boolean isValidName(String input) {
        String englishRegex = "^[a-zA-Z]+$";
        String koreanRegex = "^[가-힣]+$";

        if (input.contains(" ") || input.isEmpty()) {
            return false;
        }

        return input.matches(englishRegex) || input.matches(koreanRegex);
    }

    private boolean isValidPhone(String input) {
        String regex = "^\\d{3}-\\d{4}-\\d{4}$|^\\d{11}$";
        return input.matches(regex);
    }

}