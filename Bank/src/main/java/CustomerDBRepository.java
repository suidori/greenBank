import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class CustomerDBRepository {
    //+ 고객 정보 조회(select)
    //+ 정보 수정 2개(update)


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
                                rs.getInt("idx"),
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
                                rs2.getInt("idx"),
                                rs2.getString("u_level"),
                                rs2.getString("u_id"),
                                rs2.getString("u_password"),
                                rs2.getString("u_name"),
                                rs2.getString("u_phone")
                        )
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 본인 정보 수정
    public void c_update(int u_idx) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://192.168.0.53:8888/Bank",
                    "root", "1234");

            pstmt = conn.prepareStatement("UPDATE users SET u_id = ?, u_password = ?, u_name = ?, u_phone = ? WHERE idx = ?");

            pstmt.setInt(5, u_idx);

            String id = JOptionPane.showInputDialog("수정할 아이디");
            pstmt.setString(1, id);

            int pw = Integer.parseInt(JOptionPane.showInputDialog("수정할 비밀번호"));
            pstmt.setInt(2, pw);

            String name = JOptionPane.showInputDialog("수정할 이름");
            pstmt.setString(3, name);

            String pn = JOptionPane.showInputDialog("수정할 핸드폰번호(-까지 입력해주세요)");
            pstmt.setString(4, pn);


            pstmt.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}