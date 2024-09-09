import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CustomerDBRepository {

    // 아이디 수정 검증
    boolean idCheck(String str){
        char cha;
        for (int i = 0; i < str.length(); i++) {
            cha = str.charAt(i);
            if (cha >= 0x61 && cha <= 0x7A){
                // 영어 소문자
            } else if (cha >= 0x41 && cha <= 0x5A) {
                // 영어 대문자
            } else if (cha >= 0x30 && cha <= 0x39) {
                // 숫자
            } else {
                return false;
            }
        }
        return true;
    }

    // 이름 수정 검증
    boolean nameCheck(String str){
        char cha;
        for (int i = 0; i < str.length(); i++) {
            cha = str.charAt(i);
            if (cha >= 0x61 && cha <= 0x7A){
                // 영어 소문자
            } else if (cha >= 0x41 && cha <= 0x5A) {
                // 영어 대문자
            } else if (cha >= 44032 && cha <= 55203) {
                // 한글
            } else {
                return false;
            }
        }
        return true;
    }

    // 핸드폰 번호 수정 검증
    boolean phoneCheck(String str){
        char cha;
        for (int i = 0; i < str.length(); i++) {
            cha = str.charAt(i);
            if (cha == 0x2D){
                // -
            } else if (cha >= 0x30 && cha <= 0x39) {
                // 숫자
            } else {
                return false;
            }
        }
        return true;
    }


    // 나의 정보 조회(마이페이지)
    public void myPage(int u_idx) {

        try(Connection conn = DriverManager.getConnection
                ("jdbc:mysql://192.168.0.53:8888/Bank",
                        "root", "1234")) {

            PreparedStatement pstmt = conn.prepareStatement("select * from users where u_idx = ?");
            pstmt.setInt(1, u_idx);

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.println("""
                                <마이페이지>
                                아이디 = %s
                                비밀번호 = %s
                                이름 = %s
                                전화번호 = %s
                                """.formatted(
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
    public void custInfo(boolean answer) {
        Scanner scan = new Scanner(System.in);
        if (answer == true) {
            try(Connection conn = DriverManager.getConnection
                ("jdbc:mysql://192.168.0.53:8888/Bank",
                        "root", "1234")) {

                System.out.println("""
                        정보를 열람할 고객의 이름을 입력하세요.
                        """);
                PreparedStatement pstmt1 = conn.prepareStatement("select u_name, u_id, u_idx from users where u_name = ?");
                String name = scan.nextLine();
                pstmt1.setString(1, name);
                ResultSet rs = pstmt1.executeQuery();

//                List<Integer> test = List.of();

                while (rs.next()) {
                    System.out.println("""
                            이름 = %s
                            은행고유번호 = %d
                            아이디 = %s
                            """.formatted(
                            rs.getString("u_name"),
                            rs.getInt("u_idx"),
                            rs.getString("u_id")
                    ));
//                    test.add(rs.getInt("u_idx"));
                }

                System.out.println("""
                        정보를 열람할 고객의 은행고유번호를 선택해주세요.
                        """);
                PreparedStatement pstmt2 = conn.prepareStatement("select * from users where u_idx = ?");
                int u_idx = scan.nextInt();
//                if(test.contains(u_idx)){
//                    System.out.println("여기 진행");
//                }
                pstmt2.setInt(1, u_idx);
                ResultSet rs2 = pstmt2.executeQuery();
                while (rs2.next()) {
                    System.out.println("""
                                    <고객 정보>
                                    은행고유번호 = %d
                                    권한 = %s
                                    아이디 = %s
                                    비밀번호 = %s
                                    이름 = %s
                                    전화번호 = %s
                                    """.formatted(
                                    rs2.getInt("u_idx"),
                                    rs2.getString("u_level"),
                                    rs2.getString("u_id"),
                                    rs2.getString("u_password"),
                                    rs2.getString("u_name"),
                                    rs2.getString("u_phone")
                            )
                    );
                }

            } catch (Exception e) {
            System.out.println("""
                입력이 잘못되었습니다.
                다시 입력 바랍니다.
                """);
            }
        }
        else {
            System.out.println("""
                        직원 확인에 실패했습니다.
                        다시 시도해주세요.
                        """);
        }
    }

    // 본인 정보 수정
    public void myPageEdit(int u_idx) {

        try(Connection conn = DriverManager.getConnection
                ("jdbc:mysql://192.168.0.53:8888/Bank","root",
                        "1234")){
            Scanner scan = new Scanner(System.in);

            PreparedStatement pstmt = conn.prepareStatement("select u_name from users where u_idx = ?");
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

            switch (scan.nextInt()){
                case 1:
                    System.out.println("수정할 아이디를 입력해주세요.");
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_id = ? WHERE u_idx = ?
                    """);
                    scan.nextLine();
                    String id = scan.nextLine();
                        if (id.contains(" ")) {
                            System.out.println("공백이 포함되어 강제종료됩니다.");
                            break;
                        } else {
                            if (idCheck(id)) {
                                pstmt.setString(1, id);
                                pstmt.setInt(2, u_idx);
                                break;
                            } else {
                                System.out.println("""
                                        영어와 숫자만 가능합니다.
                                        종료됩니다.
                                        """);
                                break;
                            }
                        }

                case 2:
                    System.out.println("수정할 비밀번호를 입력해주세요.");
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_password = ? WHERE u_idx = ?
                    """);
                    scan.nextLine();
                    String pw = scan.nextLine();
                    if (pw.contains(" ")){
                        System.out.println("공백이 포함되어 강제종료됩니다.");
                        break;
                    } else {
                        pstmt.setString(1, pw);
                        pstmt.setInt(2, u_idx);
                        break;
                    }

                case 3:
                    System.out.println("수정할 이름을 입력해주세요.");
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_name = ? WHERE u_idx = ?
                    """);
                    scan.nextLine();
                    String name = scan.nextLine();
                        if (name.contains(" ")) {
                            System.out.println("공백이 포함되어 강제종료됩니다.");
                            break;
                        } else {
                            if (nameCheck(name)) {
                                pstmt.setString(1, name);
                                pstmt.setInt(2, u_idx);
                                break;
                            } else {
                                System.out.println("""
                                        한글과 영어만 가능합니다.
                                        종료됩니다.
                                        """);
                                break;
                            }
                        }

                case 4:
                    System.out.println("수정할 핸드폰번호를 - 포함하여 입력해주세요.");
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_phone = ? WHERE u_idx = ?
                    """);
                    scan.nextLine();
                    String pn = scan.nextLine();
                        if (pn.contains(" ")) {
                            System.out.println("공백이 포함되어 강제종료됩니다.");
                            break;
                        } else {
                            if (phoneCheck(pn)) {
                                pstmt.setString(1, pn);
                                pstmt.setInt(2, u_idx);
                                break;

                            } else {
                                System.out.println("""
                                        숫자와 - 만 가능합니다.
                                        종료됩니다.
                                        """);
                                break;
                            }
                        }
            }
            pstmt.executeUpdate();
            System.out.println("수정이 완료되었습니다.");
            System.out.println();

        } catch (Exception e) {
            System.out.println("입력이 잘못되었습니다.");
        }
    }

    // 직원 -> 고객 정보 수정
    public int custInfoEdit(boolean answer) {
        int u_idx2 = 0;
        if (answer == true) {
        try(Connection conn = DriverManager.getConnection
                ("jdbc:mysql://192.168.0.53:8888/Bank","root",
                        "1234")){
            Scanner scan = new Scanner(System.in);

            System.out.println("""
                    정보를 수정할 고객의 이름을 입력하세요.
                    """);
            PreparedStatement pstmt = conn.prepareStatement("select u_name, u_id, u_idx from users where u_name = ?");
            String name = scan.nextLine();
            pstmt.setString(1,name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                System.out.println("""
                    이름 = %s
                    은행고유번호 = %d
                    아이디 = %s
                    """.formatted(
                        rs.getString("u_name"),
                        rs.getInt("u_idx"),
                        rs.getString("u_id")
                ));
            }

            System.out.println("""
                    정보를 수정할 고객의 은행고유번호를 선택해주세요.
                    """);
            pstmt = conn.prepareStatement("select u_name from users where u_idx = ?");
            u_idx2 = scan.nextInt();
            pstmt.setInt(1,u_idx2);
            rs = pstmt.executeQuery();
            rs.next();
            System.out.println("""
                    <%s 고객님> 수정할 정보의 번호를 입력해주세요.
                    1. 아이디
                    2. 비밀번호
                    3. 이름
                    4. 핸드폰번호
                    """.formatted(rs.getString("u_name")));

            int cho = scan.nextInt();

            switch (cho){
                case 1:
                    System.out.println("수정할 아이디를 입력해주세요.");
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_id = ? WHERE u_idx = ? and u_level != "clerk"
                    """);
                    String id = scan.next();

                        if (id.contains(" ")) {
                            System.out.println("공백이 포함되어 강제종료됩니다.");
                            break;
                        } else {
                            if (idCheck(id)) {
                                pstmt.setString(1, id);
                                pstmt.setInt(2, u_idx2);
                                break;
                            } else {
                                System.out.println("""
                                        영어와 숫자만 가능합니다.
                                        종료됩니다.
                                        """);
                                break;
                            }
                        }

                case 2:
                    System.out.println("수정할 비밀번호를 입력해주세요.");
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_password = ? WHERE u_idx = ? and u_level != "clerk"
                    """);
                    String pw = scan.next();
                    if (pw.contains(" ")){
                        System.out.println("공백이 포함되어 강제종료됩니다.");
                        break;
                    }
                    else {
                        pstmt.setString(1, pw);
                        pstmt.setInt(2, u_idx2);
                        break;
                    }

                case 3:
                    System.out.println("수정할 이름을 입력해주세요.");
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_name = ? WHERE u_idx = ? and u_level != "clerk"
                    """);
                    String name2 = scan.next();
                        if (name2.contains(" ")) {
                            System.out.println("공백이 포함되어 강제종료됩니다.");
                            break;
                        } else {
                            if (nameCheck(name2)) {
                                pstmt.setString(1, name2);
                                pstmt.setInt(2, u_idx2);
                                break;
                            } else {
                                System.out.println("""
                                        한글과 영어만 가능합니다.
                                        종료됩니다.
                                        """);
                                break;
                            }
                        }

                case 4:
                    System.out.println("수정할 핸드폰번호를(-까지 입력해주세요)");
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_phone = ? WHERE u_idx = ? and u_level != "clerk"
                    """);
                    String pn = scan.next();
                        if (pn.contains(" ")) {
                            System.out.println("공백이 포함되어 강제종료됩니다.");
                            break;
                        } else {
                            if (phoneCheck(pn)) {
                                pstmt.setString(1, pn);
                                pstmt.setInt(2, u_idx2);
                                break;
                            } else {
                                System.out.println("""
                                        숫자와 - 만 가능합니다.
                                        종료됩니다.
                                        """);
                                break;
                            }
                        }
            }

            pstmt.executeUpdate();
            System.out.println("""
                수정이 완료되었습니다.
                (같은 직원의 정보는 수정할 수 없으므로 변경되지 않습니다.
                 직원일 경우 내 정보 수정에서 수정하시길 바랍니다.)
                """);
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("""
                    실패하였습니다.
                    (같은 직원의 정보를 수정할 수 없습니다.)
                    """);
        }
        }
        else {
            System.out.println("""
                        직원 확인에 실패했습니다.
                        다시 시도해주세요.
                        """);
        }
        return u_idx2;
    }
}