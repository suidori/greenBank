package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBSign {

    private int u_idx;

    public int getU_idx() {
        return u_idx;
    }

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet userInfo = null;
    ResultSet userId = null;

    public loginState login(Scanner sc) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.0.53:8888/Bank",
                    "root",
                    "1234"
            );

            pstmt = conn.prepareStatement("SELECT * FROM users;");
            userInfo = pstmt.executeQuery();
            System.out.println("""
                                        
                    1. 로그인
                    아이디와 비밀번호를 입력 해 주세요.
                    """);
            String idInput = sc.next();
            String passwordInput = sc.next();

            while (userInfo.next()) {
                if (idInput.equals(userInfo.getString("u_id"))) {
                    if (passwordInput.equals(userInfo.getString("u_password"))) {
                        System.out.println("로그인 성공");
                        u_idx = userInfo.getInt("u_idx");
                        if (userInfo.getString("u_level").equals("clerk")) {
                            System.out.println(userInfo.getString("u_name") + " 직원 계정으로 로그인했습니다.");
                            return loginState.CLERK;
                        } else {
                            System.out.println(userInfo.getString("u_name") + " 고객님, 반갑습니다.");
                            return loginState.CUSTOMER;
                        }
                    } else {
                        System.out.println("비밀번호가 올바르지 않습니다.");
                        return loginState.FAILED;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return loginState.FAILED;
    }

    public boolean register(Scanner sc) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.0.53:8888/Bank",
                    "root",
                    "1234"
            );

            System.out.println("""
                                        
                    2. 회원가입
                    아이디는 영어 대소문자와 숫자만 사용할 수 있습니다.""");
            sc.nextLine();
            boolean idCheck = true;
            String idInput;
            while (true) {
                System.out.println("아이디를 입력 해 주세요");
                idInput = sc.nextLine();
                String checkInput;

                if (idInput.contains(" ")) {
                    System.out.println("아이디에 공백은 들어갈 수 없습니다.");
                    continue;
                } else if (idInput.isEmpty()) {
                    continue;
                }

                checkInput = idInput.replaceAll("[0-9]", "");
                checkInput = checkInput.replaceAll("[a-z]", "");
                checkInput = checkInput.replaceAll("[A-Z]", "");

                if (!checkInput.isEmpty()) {
                    System.out.println("아이디는 영어 대소문자와 숫자만 사용할 수 있습니다.");
                    continue;
                }

                pstmt = conn.prepareStatement("SELECT u_id FROM users;");
                userId = pstmt.executeQuery();
                while (userId.next()) {
                    if (idInput.equals(userId.getString("u_id"))) {
                        System.out.println("중복된 아이디입니다.");
                        idCheck = false;
                        break;
                    }
                }

                if (idCheck) {
                    break;
                }
            }

            System.out.println("입력된 아이디는 [" + idInput + "] 입니다");

            String passwordInput;


            System.out.println("비밀번호는 영어 대소문자와 숫자만 입력할 수 있습니다.");
            while (true) {
                System.out.println("비밀번호를 입력 해 주세요.");
                passwordInput = sc.nextLine();
                String checkInput;

                if (passwordInput.contains(" ")) {
                    System.out.println("비밀번호에 공백은 들어갈 수 없습니다.");
                    continue;
                } else if (passwordInput.isEmpty()) {
                    continue;
                }

                checkInput = passwordInput.replaceAll("[0-9]", "");
                checkInput = checkInput.replaceAll("[a-z]", "");
                checkInput = checkInput.replaceAll("[A-Z]", "");

                if (!checkInput.isEmpty()) {
                    System.out.println("비밀번호는 영어 대소문자와 숫자만 입력할 수 있습니다.");
                } else {
                    break;
                }
            }

            System.out.println("이름을 입력 해 주세요.");
            String nameInput = sc.nextLine();
            System.out.printf("당신의 이름은 %s 입니다.\n", nameInput);

            String phoneInput;

            while(true) {
                System.out.println("전화번호를 입력 해 주세요.");
                phoneInput = sc.nextLine();

                String regex = "\\d{3}-\\d{4}-\\d{4}";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(phoneInput);

                if (matcher.matches()) {
                    System.out.printf("당신의 전화번호는 %s 입니다.\n", phoneInput);
                    break;
                } else {
                    if (phoneInput.length() == 11) {
                        phoneInput = phoneInput.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
                        System.out.printf("당신의 전화번호는 %s 입니다.\n\n", phoneInput);
                        break;
                    }else {
                        System.out.println("올바른 전화번호를 입력 해 주세요.");
                    }
                }
            }

            pstmt = conn.prepareStatement("INSERT INTO users (u_id, u_password, u_name, u_phone) VALUES (?, ?, ?, ?);");
            pstmt.setString(1, idInput);
            pstmt.setString(2, passwordInput);
            pstmt.setString(3, nameInput);
            pstmt.setString(4, phoneInput);
            pstmt.executeUpdate();

            pstmt = conn.prepareStatement("SELECT * FROM users WHERE u_id = ?;");
            pstmt.setString(1, idInput);
            userInfo = pstmt.executeQuery();
            if(userInfo.next()) {
                System.out.println("입력된 정보");
                System.out.println("회원번호: " + userInfo.getInt("u_idx"));
                System.out.println("아이디: " + userInfo.getString("u_id"));
                System.out.println("비밀번호: " + userInfo.getString("u_password"));
                System.out.println("이름: " + userInfo.getString("u_name"));
                System.out.println("전화번호: " + userInfo.getString("u_phone"));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
