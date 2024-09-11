package DB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DBClerk {

    private int u_idx;

    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;

    public DBClerk(int u_idx) {
        this.u_idx = u_idx;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://192.168.0.53:8888/Bank",
                    "root",
                    "1234"
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void myInfo() {
        try {
            pstmt = conn.prepareStatement("SELECT * from users where u_idx = ?");
            pstmt.setInt(1, u_idx);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.printf("""
                                아이디: %s
                                비밀번호: %s
                                이름: %s
                                전화번호: %s
                                """,
                        rs.getString("u_id"), rs.getString("u_password"),
                        rs.getString("u_name"), rs.getString("u_phone"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void customerInfo(Scanner sc) {
        System.out.println("이름을 입력 해 주세요.");
        try {
            pstmt = conn.prepareStatement("SELECT * from users where u_name like ?");
            pstmt.setString(1, sc.next());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.printf("""
                                [회원번호: %d], [이름: %s], [권한: %s]
                                """,
                        rs.getInt("u_idx"), rs.getString("u_name"),
                        (rs.getString("u_level").equals("clerk")) ? "직원" : "고객");
            }
            System.out.println("회원번호를 입력 해 주세요.");
            pstmt = conn.prepareStatement("SELECT * from users where u_idx like ?");
            pstmt.setInt(1, sc.nextInt());
            rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.printf("""
                                [회원번호: %d]
                                [아이디: %s]
                                [비밀번호: %s]
                                [이름: %s]
                                [전화번호: %s]
                                """,
                        rs.getInt("u_idx"),
                        rs.getString("u_id"), rs.getString("u_password"),
                        rs.getString("u_name"), rs.getString("u_phone"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void edit(Scanner sc) {
        System.out.println("이름을 입력 해 주세요.");
        try {
            pstmt = conn.prepareStatement("SELECT * from users where u_name like ?");
            pstmt.setString(1, sc.next());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.printf("""
                                [회원번호: %d], [이름: %s], [권한: %s]
                                """,
                        rs.getInt("u_idx"), rs.getString("u_name"),
                        (rs.getString("u_level").equals("clerk")) ? "직원" : "고객");
            }
        int accountIdx;
        System.out.println("수정 할 회원의 회원번호를 입력 해 주세요.\n" +
                "본인이 아닌 다른 직원의 정보는 수정할 수 없습니다.");
            pstmt = conn.prepareStatement("SELECT * from users where u_idx like ?");
            accountIdx = sc.nextInt();
            pstmt.setInt(1, accountIdx);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                if (rs.getString("u_level").equals("clerk") && rs.getInt(u_idx) != u_idx) {
                    throw new RuntimeException("다른 직원의 정보는 수정할 수 없습니다.");
                } else {
                    System.out.printf("""
                                    수정할 정보를 선택 해 주세요
                                                            
                                    1. 아이디: %s
                                    2. 비밀번호: %s
                                    3. 이름: %s
                                    4. 전화번호: %s
                                    """,
                            rs.getString("u_id"), rs.getString("u_password"),
                            rs.getString("u_name"), rs.getString("u_phone"));
                }
            }

            try {
                switch (sc.nextInt()) {
                    case 1:
                        sc.nextLine();
                        boolean idCheck = true;
                        String idInput;
                        while (true) {
                            ResultSet userId = null;
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
                        pstmt = conn.prepareStatement("UPDATE `users` SET `u_id`=? WHERE  `u_idx`=?;");
                        pstmt.setString(1, idInput);
                        pstmt.setInt(2, accountIdx);
                        pstmt.executeUpdate();
                        System.out.println("아이디 변경이 완료되었습니다.");
                        break;
                    case 2:
                        sc.nextLine();
                        String passInput;
                        while (true) {
                            System.out.println("새 비밀번호를 입력 해 주세요");
                            passInput = sc.nextLine();
                            String checkInput;

                            if (passInput.contains(" ")) {
                                System.out.println("비밀번호에 공백은 들어갈 수 없습니다.");
                                continue;
                            } else if (passInput.isEmpty()) {
                                continue;
                            }

                            checkInput = passInput.replaceAll("[0-9]", "");
                            checkInput = checkInput.replaceAll("[a-z]", "");
                            checkInput = checkInput.replaceAll("[A-Z]", "");

                            if (!checkInput.isEmpty()) {
                                System.out.println("비밀번호는 영어 대소문자와 숫자만 사용할 수 있습니다.");
                            } else {
                                break;
                            }
                        }
                        pstmt = conn.prepareStatement("UPDATE `users` SET `u_password`=? WHERE  `u_idx`=?;");
                        pstmt.setString(1, passInput);
                        pstmt.setInt(2, accountIdx);
                        pstmt.executeUpdate();
                        System.out.println("비밀번호 변경이 완료되었습니다.");
                        break;
                    case 3:
                        sc.nextLine();
                        System.out.println("새 이름을 입력 해 주세요.");
                        pstmt = conn.prepareStatement("UPDATE `users` SET `u_name`=? WHERE  `u_idx`=?;");
                        pstmt.setString(1, sc.nextLine());
                        pstmt.setInt(2, accountIdx);
                        pstmt.executeUpdate();
                        System.out.println("이름이 변경되었습니다.");
                        break;
                    case 4:
                        sc.nextLine();
                        String phoneInput;
                        while (true) {
                            System.out.println("새 전화번호를 입력 해 주세요.");
                            phoneInput = sc.nextLine();

                            String regex = "\\d{3}-\\d{4}-\\d{4}";
                            Pattern pattern = Pattern.compile(regex);
                            Matcher matcher = pattern.matcher(phoneInput);

                            if (matcher.matches()) {
                                break;
                            } else {
                                if (phoneInput.length() == 11) {
                                    phoneInput = phoneInput.replaceFirst("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
                                    break;
                                } else {
                                    System.out.println("올바른 전화번호를 입력 해 주세요.");
                                }
                            }
                        }
                        pstmt = conn.prepareStatement("UPDATE `users` SET `u_phone`=? WHERE  `u_idx`=?;");
                        pstmt.setString(1, phoneInput);
                        pstmt.setInt(2, accountIdx);
                        pstmt.executeUpdate();
                        System.out.println("전화번호가 변경되었습니다.");
                        break;
                    default:
                        throw new RuntimeException();
                }
            } catch (Exception e) {
                System.out.println("올바른 값을 입력 해 주세요.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void accountInfo(Scanner sc) {
        System.out.println("이름을 입력 해 주세요.");
        try {
            pstmt = conn.prepareStatement("SELECT * from users where u_name like ?");
            pstmt.setString(1, sc.next());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.printf("""
                                [회원번호: %d], [이름: %s]
                                """,
                        rs.getInt("u_idx"), rs.getString("u_name"));
            }
            System.out.println("회원번호를 입력 해 주세요.");
            pstmt = conn.prepareStatement("SELECT * from owners where u_idx = ?");
            pstmt.setInt(1, sc.nextInt());
            rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.printf("[계좌번호: %d]", rs.getInt("a_number"));
            }
            System.out.println("계좌번호를 입력 해 주세요.");
            int anum = sc.nextInt();
            pstmt = conn.prepareStatement("SELECT * from accounts where a_number = ?");
            pstmt.setInt(1, anum);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                System.out.printf("[계좌번호: %d], [잔액: %d]\n\n", rs.getInt("a_number"), rs.getInt("a_balance"));
            }
            pstmt = conn.prepareStatement("SELECT * from history where a_number = ? ORDER BY h_timestamp DESC LIMIT 5");
            pstmt.setInt(1, anum);
            rs = pstmt.executeQuery();
            System.out.println("최근 거래 내역");
            while(rs.next()) {
                System.out.printf("[계좌번호: %d], [거래 회원 번호: %d], [입/출금 금액: %d], [잔액: %d], [일시: %s]\n",
                        rs.getInt("a_number"), rs.getInt("u_idx"), rs.getInt("h_calc"),
                        rs.getInt("h_balance"), (rs.getDate("h_timestamp")).toString().toString());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void IOMoney(Scanner sc){
        System.out.println("입/출금 메뉴입니다.\n" +
                "계좌번호를 입력 해 주세요.");
        int accountNum = sc.nextInt();
        try {
            pstmt = conn.prepareStatement("SELECT * from owners where a_number like ?");
            pstmt.setInt(1, accountNum);
            rs = pstmt.executeQuery();
            List<Integer> ownerList = new ArrayList<>();
            if(rs.next()){
                System.out.printf("[계좌번호: %d], [계좌주: %d]",
                        rs.getInt("a_number"), rs.getInt("u_idx"));
                ownerList.add(rs.getInt(u_idx));
            }
            while(rs.next()){
                System.out.printf("[계좌주: %d]",
                        rs.getInt("u_idx"));
                ownerList.add(rs.getInt(u_idx));
            }

            System.out.println("고객번호를 입력 해 주세요.");
            int ownerIndex = sc.nextInt();
            if(!ownerList.contains(ownerIndex)){
                System.out.println("계좌의 소유주가 아닙니다.");
                throw new RuntimeException("계좌주가 아님");
            }

            pstmt = conn.prepareStatement("SELECT * from accounts where a_number like ?");
            pstmt.setInt(1, accountNum);
            rs = pstmt.executeQuery();
            if(rs.next()){
                System.out.printf("[계좌번호: %d], [잔액: %d]",
                        rs.getInt("a_number"), rs.getInt("a_balance"));
            }
            int beforeBalance = rs.getInt("a_balance");
            System.out.println("입/출금 금액을 입력 해 주세요.(출금 금액은 음수로 입력 해 주세요.)");
            int calc = sc.nextInt();
            if(calc+beforeBalance < 0){
                System.out.println("잔액이 부족합니다.");
                throw new RuntimeException("잔액 부족");
            }else {
                pstmt = conn.prepareStatement("INSERT INTO history (a_number, u_idx, h_calc, h_balance) VALUES (?, ?, ?, ?)");
                pstmt.setInt(1, accountNum);
                pstmt.setInt(2, ownerIndex);
                pstmt.setInt(3, calc);
                pstmt.setInt(4, beforeBalance+calc);
                pstmt.executeUpdate();
                pstmt = conn.prepareStatement("UPDATE accounts SET a_balance = ? WHERE a_number = ?;");
                pstmt.setInt(1, beforeBalance + calc);
                pstmt.setInt(2, accountNum);
                pstmt.executeUpdate();
                System.out.println("거래가 완료되었습니다.");
            }
            pstmt = conn.prepareStatement("SELECT * from accounts where a_number like ?");
            pstmt.setInt(1, accountNum);
            rs = pstmt.executeQuery();
            if(rs.next()){
                System.out.printf("[계좌번호: %d], [잔액: %d]",
                        rs.getInt("a_number"), rs.getInt("a_balance"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void accountEdit(Scanner sc){
        System.out.println("""
                계좌 관리 메뉴입니다.
                
                1. 신규 계좌 개설
                2. 계좌 소유주 추가
                """);

        try{
            switch (sc.nextInt()){
                case 1:
                    System.out.println("신규 계좌를 개설할 고객의 회원번호를 입력 해 주세요.");
                    int newIdx = sc.nextInt();
                    pstmt = conn.prepareStatement(
                            "INSERT INTO accounts (a_balance) VALUES (0);" +
                            "SET @last_a_number = LAST_INSERT_ID();" +
                            "INSERT INTO owners (u_idx, a_number) VALUES (?, @last_a_number);");
                    pstmt.setInt(1, newIdx);
                    pstmt.executeUpdate();
                    System.out.print("계좌가 개설되었습니다. 계좌번호는 [");
                    pstmt = conn.prepareStatement("SELECT * FROM accounts WHERE a_number = @last_a_number;");
                    rs = pstmt.executeQuery();
                    System.out.println(rs.getInt("a_number") + "] 입니다");
                    break;
                case 2:
                    System.out.println("소유주를 추가할 기존 계좌의 계좌번호를 입력 해 주세요.");
                    int anum = sc.nextInt();
                    pstmt = conn.prepareStatement("SELECT * FROM owners WHERE a_number = ?");
                    pstmt.setInt(1, anum);
                    rs = pstmt.executeQuery();

                    List<Integer> idxList = new ArrayList<>();

                    System.out.println("계좌주 목록: ");

                    while(rs.next()){
                        System.out.println(rs.getInt("u_idx"));
                        idxList.add(rs.getInt("u_idx"));
                    }

                    System.out.println("추가할 고객의 고객번호를 입력하세요.");
                    int index = sc.nextInt();
                    List<Integer> listAll = new ArrayList<>();
                    pstmt = conn.prepareStatement("SELECT u_idx FROM users;");
                    rs = pstmt.executeQuery();
                    while(rs.next()){
                        listAll.add(rs.getInt("u_idx"));
                    }

                    if(!idxList.contains(index) && listAll.contains(index)){
                        pstmt = conn.prepareStatement("INSERT INTO owners (u_idx, a_number) VALUES (?, ?);");
                        pstmt.setInt(1, index);
                        pstmt.setInt(2, anum);
                        System.out.println("계좌 소유주가 추가되었습니다.");
                    }else{
                        System.out.println("존재하지 않거나 중복된 고객번호입니다.");
                        throw new RuntimeException("올바르지 않은 계좌주 추가");
                    }
                    break;
                default:
                    throw new RuntimeException("올바르지 않은 번호");
            }
        }catch(Exception e){
            e.printStackTrace();
        }



    }
}
