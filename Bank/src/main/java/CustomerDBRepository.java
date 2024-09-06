import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class CustomerDBRepository {

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
                                은행고유번호 = %d
                                권한 = %s
                                아이디 = %s
                                비밀번호 = %s
                                이름 = %s
                                전화번호 = %s
                                """.formatted(
                                rs.getInt("u_idx"),
                                rs.getString("u_level"),
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
    public void custInfo() {
        Scanner scan = new Scanner(System.in);
        try(Connection conn = DriverManager.getConnection
                ("jdbc:mysql://192.168.0.53:8888/Bank",
                        "root", "1234")) {

            System.out.println("""
                    정보를 열람할 고객의 이름을 입력하세요.
                    """);
            PreparedStatement pstmt1 = conn.prepareStatement("select u_name, u_id, u_idx from users where u_name = ?");
            String name = scan.nextLine();
            pstmt1.setString(1,name);
            ResultSet rs = pstmt1.executeQuery();
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
                    정보를 열람할 고객의 은행고유번호를 선택해주세요.
                    """);
            PreparedStatement pstmt2 = conn.prepareStatement("select * from users where u_idx = ?");
            int u_idx = scan.nextInt();
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
            System.out.println("입력이 잘못되었습니다.");
        }
    }

    // 직원 -> 고객 정보 조회2
    public void custInfo(int num3) {
        Scanner scan = new Scanner(System.in);
        try(Connection conn = DriverManager.getConnection
                ("jdbc:mysql://192.168.0.53:8888/Bank",
                        "root", "1234")) {

            PreparedStatement pstmt2 = conn.prepareStatement("select * from users where u_idx = ?");
            int u_idx = num3;
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
            System.out.println("입력이 잘못되었습니다.");
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
                    String id = scan.next();
                    pstmt.setString(1, id);
                    pstmt.setInt(2, u_idx);
                    break;

                case 2:
                    System.out.println("수정할 비밀번호를 입력해주세요.");
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_password = ? WHERE u_idx = ?
                    """);
                    int pw = scan.nextInt();
                    pstmt.setInt(1, pw);
                    pstmt.setInt(2, u_idx);
                    break;

                case 3:
                    System.out.println("수정할 이름을 입력해주세요.");
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_name = ? WHERE u_idx = ?
                    """);
                    String name = scan.next();
                    pstmt.setString(1, name);
                    pstmt.setInt(2, u_idx);

                    break;

                case 4:
                    System.out.println("수정할 핸드폰번호를 입력해주세요.");
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_phone = ? WHERE u_idx = ?
                    """);
                    String pn = scan.next();
                    pstmt.setString(1, pn);
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
        int u_idx2 = 0;
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
                    UPDATE users SET u_id = ? WHERE u_idx = ?
                    """);
                    String id = scan.next();
                    pstmt.setString(1, id);
                    pstmt.setInt(2, u_idx2);
                    break;

                case 2:
                    System.out.println("수정할 비밀번호를 입력해주세요.");
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_password = ? WHERE u_idx = ?
                    """);
                    String pw = scan.next();
                    pstmt.setString(1, pw);
                    pstmt.setInt(2, u_idx2);
                    break;

                case 3:
                    System.out.println("수정할 이름을 입력해주세요.");
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_name = ? WHERE u_idx = ?
                    """);
                    String name2 = scan.next();
                    pstmt.setString(1, name2);
                    pstmt.setInt(2, u_idx2);
                    break;

                case 4:
                    System.out.println("수정할 핸드폰번호를(-까지 입력해주세요)");
                    pstmt = conn.prepareStatement("""
                    UPDATE users SET u_phone = ? WHERE u_idx = ?
                    """);
                    String pn = scan.next();
                    pstmt.setString(1, pn);
                    pstmt.setInt(2, u_idx2);
                    break;
            }

            pstmt.executeUpdate();
            System.out.println("수정이 완료되었습니다.");
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
//            System.out.println("입력이 잘못되었습니다.");
        }
        return u_idx2;
    }
}