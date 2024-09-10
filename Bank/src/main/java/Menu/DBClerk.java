package Menu;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DBClerk {

    private int u_idx;
    Scanner sc;
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;

    public DBClerk(int u_idx, Scanner sc, Connection conn) {
        this.u_idx = u_idx;
        this.sc = sc;
        this.conn = conn;
    }

    // 나의 정보 조회(마이페이지)
    public void myPage(int u_idx) {

        try{
            pstmt = conn.prepareStatement("select * from users where u_idx = ?");
            pstmt.setInt(1, u_idx);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("""
                                <마이페이지>
                                회원번호 = %d
                                아이디 = %s
                                비밀번호 = %s
                                이름 = %s
                                전화번호 = %s
                                """.formatted(
                                rs.getInt("u_idx"),
                                rs.getString("u_id"),
                                rs.getString("u_password"),
                                rs.getString("u_name"),
                                rs.getString("u_phone")
                        )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 직원 -> 고객 정보 조회
    public void customerInfo() {
            try {
                System.out.println("정보를 열람할 고객의 이름을 입력하세요.");
                pstmt = conn.prepareStatement("select * from users where u_name = ? and u_level = ?");
                sc.nextLine();
                String name = sc.nextLine();
                pstmt.setString(1, name);
                pstmt.setString(2, "customer");
                rs = pstmt.executeQuery();

                List<Integer> selectList = new ArrayList<>(List.of());

                while (rs.next()) {
                    System.out.println("""
                            이름 = %s
                            회원번호 = %d
                            아이디 = %s
                            """.formatted(
                            rs.getString("u_name"),
                            rs.getInt("u_idx"),
                            rs.getString("u_id")
                    ));
                    selectList.add(rs.getInt("u_idx"));
                }

                System.out.println("""
                        정보를 열람할 고객의 회원번호를 선택해주세요.
                        """);
                pstmt = conn.prepareStatement("select * from users where u_idx = ?");
                int u_idx = sc.nextInt();
                if(selectList.contains(u_idx)) {
                    pstmt.setInt(1, u_idx);
                    rs = pstmt.executeQuery();
                    if(rs.next()) {
                        System.out.println("""
                                        <고객 정보>
                                        은행고유번호 = %d
                                        아이디 = %s
                                        비밀번호 = %s
                                        이름 = %s
                                        전화번호 = %s
                                        """.formatted(
                                        rs.getInt("u_idx"),
                                        rs.getString("u_id"),
                                        rs.getString("u_password"),
                                        rs.getString("u_name"),
                                        rs.getString("u_phone")
                                )
                        );
                    }
                }else {
                    System.out.println("""
                            화면에 표시된 목록 중 선택 해 주시기 바랍니다.
                            """);
                }

            } catch (Exception e) {
                e.printStackTrace();
            System.out.println("""
                입력이 잘못되었습니다.
                다시 입력 바랍니다.
                """);
            }
    }

    // 본인 정보 수정
    public void myPageEdit() {

        try{
            pstmt = conn.prepareStatement("select u_name from users where u_idx = ?");
            pstmt.setInt(1,u_idx);
            ResultSet rs = pstmt.executeQuery();
            rs.next();

            System.out.println("""
                    <%s 고객님> 수정할 정보의 번호를 입력해주세요.
                    1. 아이디
                    2. 비밀번호
                    3. 이름
                    4. 핸드폰번호
                    """.formatted(rs.getString("u_name")));

            switch (sc.nextInt()){
                case 1:
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_id = ? WHERE u_idx = ?
                    """);
                    sc.nextLine();
                    String inputId = getValidInput(sc, "아이디", this::isValidString, "SELECT * FROM users WHERE u_id = ?");
                    pstmt.setString(1, inputId);
                    pstmt.setInt(2, u_idx);
                    break;
                case 2:
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_password = ? WHERE u_idx = ?
                    """);
                    sc.nextLine();
                    String inputPassword = getValidInput(sc, "비밀번호", this::isValidString, null);
                    pstmt.setString(1, inputPassword);
                    pstmt.setInt(2, u_idx);
                    break;
                case 3:
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_name = ? WHERE u_idx = ?
                    """);
                    sc.nextLine();
                    String inputName = getValidInput(sc, "이름", this::isValidName, null);
                                pstmt.setString(1, inputName);
                                pstmt.setInt(2, u_idx);
                                break;
                case 4:
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_phone = ? WHERE u_idx = ?
                    """);
                    sc.nextLine();
                    String inputPhone = getValidInput(sc, "전화번호", this::isValidPhone, "SELECT * FROM users WHERE u_phone = ?");
                    pstmt.setString(1, inputPhone);
                    pstmt.setInt(2, u_idx);
                    break;
            }
            pstmt.executeUpdate();
            System.out.println("수정이 완료되었습니다.");
            System.out.println();

        } catch (Exception e) {
            System.out.println("입력이 잘못되었습니다.");
        }
    }

    // 직원 -> 고객 정보 수정
    public int custInfoEdit() {
        int u_idx2;
        try{
            System.out.println("""
                    정보를 수정할 고객의 이름을 입력하세요.
                    """);
            pstmt = conn.prepareStatement("select u_name, u_id, u_idx from users where u_name = ? and u_level = ?");
            sc.nextLine();
            String name = sc.nextLine();
            pstmt.setString(1,name);
            pstmt.setString(2, "customer");
            rs = pstmt.executeQuery();
            List<Integer> selectList = new ArrayList<>(List.of());
            while (rs.next()){
                System.out.println("""
                    이름 = %s
                    회원번호 = %d
                    아이디 = %s
                    """.formatted(
                        rs.getString("u_name"),
                        rs.getInt("u_idx"),
                        rs.getString("u_id")
                ));
                selectList.add(rs.getInt("u_idx"));
            }

            System.out.println("""
                    정보를 수정할 고객의 회원번호를 선택해주세요.
                    """);
            pstmt = conn.prepareStatement("select u_name from users where u_idx = ?");
            u_idx2 = sc.nextInt();
            if(selectList.contains(u_idx2)) {
                pstmt.setInt(1, u_idx2);
                rs = pstmt.executeQuery();
                rs.next();
                System.out.println("""
                        <%s 고객님> 수정할 정보의 번호를 입력해주세요.
                        1. 아이디
                        2. 비밀번호
                        3. 이름
                        4. 휴대폰번호
                        """.formatted(rs.getString("u_name")));

                switch (sc.nextInt()){
                    case 1:
                        pstmt = conn.prepareStatement("""
                    UPDATE users SET u_id = ? WHERE u_idx = ?
                    """);
                        sc.nextLine();
                        String inputId = getValidInput(sc, "아이디", this::isValidString, "SELECT * FROM users WHERE u_id = ?");
                        pstmt.setString(1, inputId);
                        pstmt.setInt(2, u_idx);
                        break;
                    case 2:
                        pstmt = conn.prepareStatement("""
                    UPDATE users SET u_password = ? WHERE u_idx = ?
                    """);
                        sc.nextLine();
                        String inputPassword = getValidInput(sc, "비밀번호", this::isValidString, null);
                        pstmt.setString(1, inputPassword);
                        pstmt.setInt(2, u_idx);
                        break;
                    case 3:
                        pstmt = conn.prepareStatement("""
                    UPDATE users SET u_name = ? WHERE u_idx = ?
                    """);
                        sc.nextLine();
                        String inputName = getValidInput(sc, "이름", this::isValidName, null);
                        pstmt.setString(1, inputName);
                        pstmt.setInt(2, u_idx);
                        break;
                    case 4:
                        pstmt = conn.prepareStatement("""
                    UPDATE users SET u_phone = ? WHERE u_idx = ?
                    """);
                        sc.nextLine();
                        String inputPhone = getValidInput(sc, "전화번호", this::isValidPhone, "SELECT * FROM users WHERE u_phone = ?");
                        pstmt.setString(1, inputPhone);
                        pstmt.setInt(2, u_idx);
                        break;
                }
                pstmt.executeUpdate();
                System.out.println("""
                        수정이 완료되었습니다.
                        """);
                System.out.println();
                return u_idx2;
            }else {
            System.out.println("화면에 표시된 목록에 있는 회원을 선택 해 주세요");
        }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("""
                    실패하였습니다.
                    """);
        }
        return 0;
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