package DB;

import java.sql.*;
import java.util.Scanner;

public class DBSign {

    private boolean connection;
    private int u_idx;
    private Connection conn;

    public int getU_idx() {
        return u_idx;
    }

    public DBSign() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.0.53:8888/Bank", "root", "1234"
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

        try (PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM users WHERE u_id = ? AND u_password = ?")) {
            pstmt.setString(1, inputId);
            pstmt.setString(2, passwordInput);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                u_idx = rs.getInt("u_idx");
                String level = rs.getString("u_level").equals("clerk") ? "직원" : "고객";
                System.out.printf("권한: %s\n%s님, 반갑습니다.\n", level, rs.getString("u_name"));
                return rs.getString("u_level").equals("clerk") ? loginState.CLERK : loginState.CUSTOMER;
            } else {
                System.out.println("아이디 또는 비밀번호가 일치하지 않습니다.");
                return loginState.FAILED;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return loginState.FAILED;
        }
    }

    public void register(Scanner sc) {
        System.out.println("회원 가입을 진행합니다.");
        try {
            String inputId = getValidInput(sc, "아이디", this::isValidString, "SELECT * FROM users WHERE u_id = ?");
            String inputPassword = getValidInput(sc, "비밀번호", this::isValidString, null);
            String inputName = getValidInput(sc, "이름", this::isValidName, null);
            String inputPhone = getValidInput(sc, "전화번호", this::isValidPhone, "SELECT * FROM users WHERE u_phone = ?");

            String insertQuery = "INSERT INTO users (u_id, u_password, u_name, u_phone) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {
                pstmt.setString(1, inputId);
                pstmt.setString(2, inputPassword);
                pstmt.setString(3, inputName);
                pstmt.setString(4, inputPhone);
                pstmt.executeUpdate();
                System.out.printf("회원 가입이 완료되었습니다.\n아이디: %s\n이름: %s\n전화번호: %s\n", inputId, inputName, inputPhone);
            }
        } catch (SQLException e) {
            System.out.println("초기 화면으로 돌아갑니다.");
        }
    }

    private String getValidInput(Scanner sc, String prompt, Validator validator, String query) throws SQLException {
        String input;
        while (true) {
            System.out.printf("%s 입력 해 주세요: ", prompt);
            input = sc.nextLine();
            cancel(input);
            if (validator.validate(input)) {
                if (query != null && isDuplicate(input, query)) {
                    System.out.println("이미 가입된 " + prompt + "입니다.");
                } else {
                    break;
                }
            }
        }
        return input;
    }

    private boolean isDuplicate(String input, String query) throws SQLException {
        if(input.matches("^\\d{11}$")){
            input = input.substring(0,3) + "-" + input.substring(3,7) + "-" + input.substring(7);
        }
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, input);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        }
    }

    private interface Validator {
        boolean validate(String input);
    }

    private void cancel(String input) {
        if (input.equals("취소")) {
            throw new RuntimeException("취소되었습니다.");
        }
    }

    private boolean isValidString(String input) {
        if(input.matches("^[a-zA-Z0-9]+$")){
            return true;
        }else{
            System.out.println("영어 대소문자와 숫자만 사용 가능합니다.");
            return false;
        }
    }

    private boolean isValidName(String input) {
        if(input.matches("^[a-zA-Z]+$") || input.matches("^[가-힣]+$")){
            return true;
        }else{
            System.out.println("영문으로만, 또는 한글로만 작성되어야 합니다.");
            return false;
        }
    }

    private boolean isValidPhone(String input) {
        if(input.matches("^\\d{3}-\\d{4}-\\d{4}$|^\\d{11}$")){
            return true;
        }else{
            System.out.println("3-4-4의 숫자 형태나 11자리의 숫자 형태만 허용됩니다.");
            return false;
        }
    }
}