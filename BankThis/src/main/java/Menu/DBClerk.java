package Menu;

import java.sql.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
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

        try {
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
            if (selectList.contains(u_idx)) {
                pstmt.setInt(1, u_idx);
                rs = pstmt.executeQuery();
                if (rs.next()) {
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
            } else {
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

        try {
            pstmt = conn.prepareStatement("select u_name from users where u_idx = ?");
            pstmt.setInt(1, u_idx);
            ResultSet rs = pstmt.executeQuery();
            rs.next();

            System.out.println("""
                    <%s 고객님> 수정할 정보의 번호를 입력해주세요.
                    1. 아이디
                    2. 비밀번호
                    3. 이름
                    4. 핸드폰번호
                    """.formatted(rs.getString("u_name")));

            switch (sc.nextInt()) {
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
        try {
            System.out.println("""
                    정보를 수정할 고객의 이름을 입력하세요.
                    """);
            pstmt = conn.prepareStatement("select u_name, u_id, u_idx from users where u_name = ? and u_level = ?");
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
                    정보를 수정할 고객의 회원번호를 선택해주세요.
                    """);
            pstmt = conn.prepareStatement("select u_name from users where u_idx = ?");
            u_idx2 = sc.nextInt();
            if (selectList.contains(u_idx2)) {
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

                switch (sc.nextInt()) {
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
            } else {
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

    //본인 계좌 조회  <<<<<<<<<<<<<<<<<<<<<<
//    public void selectMyAcc() {
//        try {
//            pstmt = conn.prepareStatement(
//                    "SELECT a_number FROM owners WHERE u_idx = ?");
//            pstmt.setInt(1, u_idx);
//            rs = pstmt.executeQuery();
//            System.out.println("내 계좌 조회");
//            System.out.println("<계좌목록>");
//            while (rs.next()) {
//                System.out.println(""+rs.getInt("a_number"));
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    //입금/출금
    public void depositWithdraw() {
        int u_idx2;
        try {
            System.out.println("""
                    거래를 진행할 고객의 이름을 입력하세요.
                    """);
            pstmt = conn.prepareStatement("select * from users where u_name = ?");
            sc.nextLine();
            String name = sc.nextLine();
            pstmt.setString(1, name);
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
                    거래를 진행할 고객의 회원번호를 선택해주세요.
                    """);
            u_idx2 = sc.nextInt();
            if (selectList.contains(u_idx2)) {
                pstmt = conn.prepareStatement("select * from users where u_idx = ?");
                pstmt.setInt(1, u_idx2);
                rs = pstmt.executeQuery();

                if (rs.next() && u_idx2 != u_idx && rs.getString("u_level").equals("clerk")) {
                    System.out.println("다른 직원의 계좌는 거래할 수 없습니다");
                    return;
                }

                while (true) {
                    pstmt = conn.prepareStatement("select * from owners where u_idx = ?");
                    pstmt.setInt(1, u_idx2);
                    int accountNumbers;
                    rs = pstmt.executeQuery();
                    try {
                        System.out.print("고객님의 모든 계좌 목록을 확인 중입니다.\n=====================================\n");
                        int count = 1;
                        List<Integer> accountList = new ArrayList<>(List.of());
                        while (rs.next()) {
                            System.out.println(count + ". 계좌번호: " + rs.getInt("a_number"));
                            count++;
                            accountList.add(rs.getInt("a_number"));
                        }
                        System.out.println("=====================================\n입출금을 원하시는 계좌번호를 입력해 주세요(0번 입력시 메인매뉴로 돌아갑니다.)");

                        accountNumbers = sc.nextInt();
                        if (accountNumbers == 0) {
                            return;
                        }
                        //계좌검증하는곳
                        if (!accountList.contains(accountNumbers)) {
                            System.out.println("본인 소유의 계좌번호가 아닙니다.");
                            continue;
                        }

                        pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
                        pstmt.setInt(1, accountNumbers);
                        rs = pstmt.executeQuery();

                        if (rs.next()) {
                            System.out.println("현재 고객님의 계좌 잔액은 " + rs.getLong("a_balance") + "원입니다.");
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("죄송합니다, 숫자만 입력해 주세요.");
                        sc.nextLine();
                        continue;
                    }
                    //여기로 돌아가게 하고싶음
                    System.out.println("""
                            1. 입금
                            2. 출금
                            3. 계좌 다시 선택
                            4. 종료
                            """);

                    try {
                        switch (sc.nextInt()) {
                            case 1:
                                System.out.println("입금하실 금액을 입력해 주세요.");
                                while (true) {
                                    long balances;
                                    try {
                                        balances = sc.nextLong();
                                        if (balances < 0) {
                                            throw new InputMismatchException("음수가 입력됨");
                                        }

                                    } catch (InputMismatchException e) {
                                        System.out.println("올바른 숫자를 입력 해 주세요.");
                                        sc.nextLine();
                                        continue;
                                    }

                                    pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
                                    pstmt.setInt(1, accountNumbers);
                                    rs = pstmt.executeQuery();

                                    if (rs.next()) {
                                        pstmt = conn.prepareStatement("update accounts set a_balance = a_balance + ? where a_number = ?");
                                        pstmt.setLong(1, balances);
                                        pstmt.setInt(2, accountNumbers);
                                        pstmt.executeUpdate();

                                        pstmt = conn.prepareStatement("insert into history(a_number,u_idx,h_calc,h_balance) values(?,?,?,?)");
                                        pstmt.setInt(1, accountNumbers);
                                        pstmt.setInt(2, u_idx);
                                        pstmt.setLong(3, balances);
                                        pstmt.setLong(4, rs.getLong("a_balance") + balances);
                                        pstmt.executeUpdate();
                                    }


                                    pstmt = conn.prepareStatement("SELECT * FROM history WHERE a_number = ? ORDER BY h_timestamp DESC LIMIT 1");
                                    pstmt.setInt(1, accountNumbers);
                                    rs = pstmt.executeQuery();

                                    if (rs.next()) {
                                        System.out.println("""
                                                거래가 완료되었습니다.
                                                거래 내역
                                                계좌번호: %d,
                                                입금액: %d,
                                                잔액: %d,
                                                일시: %s
                                                """.formatted(rs.getInt("a_number"), balances, rs.getInt("h_balance"), rs.getString("h_timestamp")));
                                    }
                                    break;
                                }
                                break;
                            case 2:
                                pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
                                pstmt.setInt(1, accountNumbers);
                                rs = pstmt.executeQuery();

                                if (rs.next()) {
                                    System.out.println("""
                                            선택한 계좌의 잔액을 조회합니다.
                                            계좌번호: %d,
                                            잔액: %d
                                            """.formatted(rs.getInt("a_number"), rs.getInt("a_balance")));
                                }

                                if (rs.getInt("a_balance") == 0) {
                                    System.out.println("잔액이 0원이므로 출금이 불가능합니다.");
                                    break;
                                }
                                System.out.println("출금하실 금액을 입력해 주세요.");
                                while (true) {
                                    long balances;
                                    try {
                                        balances = sc.nextLong();
                                        if (balances > rs.getInt("a_balance")) {
                                            System.out.println("잔액이 부족합니다.");
                                            throw new InputMismatchException("잔액이 부족함");
                                        } else if (balances < 0) {
                                            System.out.println("음수를 입력할 수 없습니다.");
                                            throw new InputMismatchException("음수가 입력됨");
                                        }
                                    } catch (InputMismatchException e) {
                                        System.out.println("올바른 값을 입력 해 주세요.");
                                        sc.nextLine();
                                        continue;
                                    }

                                    balances *= -1;

                                    pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
                                    pstmt.setInt(1, accountNumbers);
                                    rs = pstmt.executeQuery();

                                    if (rs.next()) {
                                        pstmt = conn.prepareStatement("update accounts set a_balance = a_balance + ? where a_number = ?");
                                        pstmt.setLong(1, balances);
                                        pstmt.setInt(2, accountNumbers);
                                        pstmt.executeUpdate();

                                        pstmt = conn.prepareStatement("insert into history(a_number,u_idx,h_calc,h_balance) values(?,?,?,?)");
                                        pstmt.setInt(1, accountNumbers);
                                        pstmt.setInt(2, u_idx);
                                        pstmt.setLong(3, balances);
                                        pstmt.setLong(4, rs.getLong("a_balance") + balances);
                                        pstmt.executeUpdate();
                                    }


                                    pstmt = conn.prepareStatement("SELECT * FROM history WHERE a_number = ? ORDER BY h_timestamp DESC LIMIT 1");
                                    pstmt.setInt(1, accountNumbers);
                                    rs = pstmt.executeQuery();

                                    if (rs.next()) {
                                        System.out.println("""
                                                거래가 완료되었습니다.
                                                거래 내역
                                                계좌번호: %d,
                                                출금액: %d,
                                                잔액: %d,
                                                일시: %s
                                                """.formatted(rs.getInt("a_number"), (-1 * balances), rs.getInt("h_balance"), rs.getString("h_timestamp")));
                                    }
                                    break;
                                }
                                break;
                            case 3:
                                break;
                            case 4:
                                return;
                            default:
                        }
                        break;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //계좌 정보 조회  <<<<<<<<<<<<<<<<<<<<<<
    public void select(){
        sc.nextLine();
        try {
            System.out.println();
            System.out.println("계좌 정보 조회");
            System.out.print("예금주명: ");
            String uName = sc.nextLine();

            pstmt = conn.prepareStatement("SELECT * FROM users WHERE u_name = ?");
            pstmt.setString(1, uName);
            rs = pstmt.executeQuery();
            List<Integer> cusNum = new ArrayList<>(List.of());
            while (rs.next()) {
                System.out.println("""
                        회원번호 = %d
                        이름 = %s
                        아이디 = %s
                        """.formatted(
                        rs.getInt("u_idx"),
                        rs.getString("u_name"),
                        rs.getString("u_id")
                ));
                cusNum.add(rs.getInt("u_idx"));
            }

            System.out.print("예금주 회원번호 입력: ");
            int uIdx = sc.nextInt();

            if (cusNum.contains(uIdx)) {
                List<Integer> accountNumbers = new ArrayList<>();
                pstmt = conn.prepareStatement("SELECT * FROM owners WHERE u_idx = ?");
                pstmt.setInt(1, uIdx);
                rs = pstmt.executeQuery();
                System.out.println("계좌 목록:");
                while (rs.next()) {
                    int aNumber = rs.getInt("a_number");
                    System.out.println(aNumber);
                    accountNumbers.add(aNumber);
                }
                System.out.print("계좌 선택: ");
                int selectedNumber = sc.nextInt();
                System.out.print("비밀번호: ");
                int aPassword = sc.nextInt();
                if (accountNumbers.contains(selectedNumber)) {
                    pstmt = conn.prepareStatement(
                            "SELECT * FROM history h, accounts a WHERE h.a_number = a.a_number AND a.a_number = ? AND a.a_password = ? ORDER BY h.h_timestamp DESC LIMIT 5"
                    );
                    pstmt.setInt(1, selectedNumber);
                    pstmt.setInt(2, aPassword);
                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        System.out.println("최근 거래 내역:");
                        do {
                            System.out.printf(
                                    "======================%n" +
                                            "%d 조회%n" +
                                            "거래: %d%n" +
                                            "잔액: %d%n" +
                                            "거래 일시: %s%n" +
                                            "======================%n",
                                    rs.getInt("a_number"),
                                    rs.getInt("h_calc"),
                                    rs.getInt("h_balance"),
                                    rs.getString("h_timestamp")
                            );
                        } while (rs.next());
                    } else {
                        System.out.println("비밀번호가 잘못되었거나 거래 내역이 없습니다.");
                    }
                } else {
                    System.out.println("선택한 계좌번호가 유효하지 않습니다.");
                }
            } else {
                System.out.println("화면에 표시된 목록에서 선택 해 주세요.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //직원 -> 계좌관리 > 1. 신규 계좌 개설  <<<<<<<<<<<<<<<<<<<<<<
    public void insert(){
        try {
            System.out.println("신규계좌개설");
            System.out.println();
            System.out.print("회원명: ");
            String uName = sc.next();
            System.out.println("아이디: ");
            String uId = sc.next();

            pstmt = conn.prepareStatement("SELECT * FROM users WHERE u_name = ? and u_id = ?");
            pstmt.setString(1, uName);
            pstmt.setString(2, uId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println(uName + "님(회원번호: " + rs.getInt("u_idx") + ") 신규계좌 개설");
                System.out.println();
                System.out.print("계좌 비밀번호 설정(숫자 네자리): ");
                int aPassword = sc.nextInt();
                if (1000>aPassword || aPassword>9999) {
                    System.out.println("숫자 네자리를 입력하세요");
                    return;
                }
                pstmt = conn.prepareStatement(
                        "INSERT INTO accounts (a_balance,a_password) VALUES (0,?);");
                pstmt.setInt(1, aPassword);
                pstmt.executeUpdate();

                pstmt = conn.prepareStatement("SET @last_a_number = LAST_INSERT_ID();");
                pstmt.executeUpdate();

                pstmt = conn.prepareStatement(
                        "INSERT INTO owners (u_idx, a_number) VALUES (?,@last_a_number);");
                pstmt.setInt(1, rs.getInt("u_idx"));
                pstmt.executeUpdate();

                System.out.print("계좌가 개설되었습니다. 계좌번호는 [");
                pstmt = conn.prepareStatement("SELECT * FROM accounts WHERE a_number = @last_a_number;");
                rs = pstmt.executeQuery();
                rs.next();

                System.out.println(rs.getInt("a_number") + "] 입니다");
            } else {
                System.out.println("해당 회원정보를 찾을 수 없습니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("다시 입력해주세요.");
        }

    }

    //직원 -> 계좌관리 > 2. 예금주 관리 > 1. 추가  <<<<<<<<<<<<<<<<<<<<<<
    public void updateOwners() throws SQLException {
        try {
            System.out.println("예금주 추가");
            System.out.println("예금주를 추가할 계좌번호: ");
            int aNumber = sc.nextInt();
            pstmt = conn.prepareStatement("SELECT * FROM owners WHERE a_number = ?");
            pstmt.setInt(1, aNumber);
            rs = pstmt.executeQuery();
            if (rs.next()) {//존재하는 계좌라면
                while (true) {
                    System.out.println("비밀번호: ");
                    int aPassword = sc.nextInt();
                    String password = Integer.toString(aPassword);
                    if (password.length() != 4) {
                        System.out.println("숫자 네자리를 입력하세요");
                    } else {
                        pstmt = conn.prepareStatement("SELECT * FROM accounts WHERE a_number = ? AND a_password = ?");
                        pstmt.setInt(1, aNumber);
                        pstmt.setInt(2, aPassword);
                        rs = pstmt.executeQuery();
                        if (rs.next()) {
                            while (true) {//비번 맞으면
                                System.out.print("추가할 회원명: ");
                                String uName = sc.next();
                                System.out.println("아이디: ");
                                String uId = sc.next();
                                pstmt = conn.prepareStatement("SELECT * FROM users, owners WHERE users.u_id = ? AND users.u_name = ?");
                                pstmt.setString(1, uId);
                                pstmt.setString(2, uName);
                                rs = pstmt.executeQuery();
                                if (rs.next()) {
                                    pstmt = conn.prepareStatement("INSERT INTO owners (u_idx, a_number) VALUES (?,?)");
                                    pstmt.setInt(1, rs.getInt("u_idx"));
                                    pstmt.setInt(2, aNumber);
                                    pstmt.executeUpdate();
                                    System.out.println("추가되었습니다.");
                                    break;
                                } else {
                                    System.out.println("다시 입력하세요.");
                                }
                            }
                        } else {
                            System.out.println("비밀번호가 틀렸습니다.");
                        }
                    }
                    break;
                }
            } else {
                System.out.println("존재하지 않는 계좌입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("다시 입력하세요.");
        }
    }

    //직원 -> 계좌관리 > 2. 예금주 관리 > 2. 삭제  <<<<<<<<<<<<<<<<<<<<<<
    public void delOwners() {
        try {
            conn.setAutoCommit(false);
            System.out.println("계좌번호: ");
            int aNumber = sc.nextInt();
            pstmt = conn.prepareStatement("SELECT * FROM owners WHERE a_number = ?");
            pstmt.setInt(1, aNumber);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                while (true) {
                    System.out.print("삭제할 예금주 이름: ");
                    String uName = sc.next();
                    System.out.println("삭제할 예금주 아이디: ");
                    String uId = sc.next();
                    pstmt = conn.prepareStatement("SELECT users.u_idx FROM users WHERE users.u_name = ? AND users.u_id = ?");
                    pstmt.setString(1, uName);
                    pstmt.setString(2, uId);
                    rs = pstmt.executeQuery();

                    if (rs.next()) {
                        int uIdx = rs.getInt("u_idx");

                        // 해당 예금주가 해당 계좌에 몇 명 있는지 확인
                        pstmt = conn.prepareStatement("SELECT COUNT(*) FROM owners WHERE a_number = ?");
                        pstmt.setInt(1, aNumber);
                        rs = pstmt.executeQuery();
                        rs.next();
                        int count = rs.getInt(1);
                        if (count <= 1) {
                            System.out.println("예금주가 한 명 이하이므로 삭제할 수 없습니다.");
                            conn.rollback(); // 트랜잭션 롤백
                        } else {
                            // 예금주 삭제
                            pstmt = conn.prepareStatement("DELETE FROM owners WHERE u_idx = ? AND a_number = ?");
                            pstmt.setInt(1, uIdx);
                            pstmt.setInt(2, aNumber);
                            pstmt.executeUpdate();
                            System.out.println("삭제되었습니다.");
                            conn.commit(); // 트랜잭션 커밋
                        }
                    } else {
                        System.out.println("해당 계좌의 예금주 정보와 일치하지 않습니다.");
                    }
                    break;
                }
            } else {
                System.out.println("존재하지 않는 계좌입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("다시 입력하세요");
        }
    }

    //직원 -> 계좌관리 > 2. 예금주 관리 > 2. 예금주 조회  <<<<<<<<<<<<<<<<<<<<<<
    public void checkOwners() {
        System.out.println();
        try {
            while (true) {
                System.out.println("예금주 조회");
                System.out.print("조회할 계좌번호: ");
                int aNumber = sc.nextInt();
                pstmt = conn.prepareStatement("SELECT * FROM owners WHERE a_number = ?");
                pstmt.setInt(1, aNumber);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    System.out.println();
                    System.out.println(aNumber + " 계좌 예금주 목록: ");
                    pstmt = conn.prepareStatement("SELECT users.u_idx, users.u_name FROM users JOIN owners ON users.u_idx = owners.u_idx WHERE owners.a_number = ?");
                    pstmt.setInt(1, aNumber);
                    rs = pstmt.executeQuery();
                    while (rs.next()) {
                        int u_idx = rs.getInt("u_idx");
                        String u_name = rs.getString("u_name");
                        System.out.println("회원번호: " + u_idx + ", 예금주명: " + u_name);
                    }
                } else {
                    System.out.println("존재하지 않는 계좌입니다.");
                    System.out.println();
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //직원 -> 계좌관리 > 3. 계좌 비밀번호 변경  <<<<<<<<<<<<<<<<<<<<<<
    public void updateApassword() {
        try {
            while (true) {
                System.out.println("계좌 비밀번호 변경");
                System.out.println("비밀번호를 변경할 계좌번호: ");
                int aNumber = sc.nextInt();
                pstmt = conn.prepareStatement("SELECT * FROM accounts WHERE a_number = ?");
                pstmt.setInt(1, aNumber);
                rs = pstmt.executeQuery();
                if (rs.next()) {    // 존재하는 계좌번호라면
                    System.out.println("기존 비밀번호: ");
                    int aPassword = sc.nextInt();
                    if (Integer.toString(aPassword).length() != 4) {
                        System.out.println("숫자 네자리를 입력하세요");
                        return;
                    } else {
                        pstmt = conn.prepareStatement("SELECT * FROM accounts WHERE a_number = ? AND a_password = ?");
                        pstmt.setInt(1, aNumber);
                        pstmt.setInt(2, aPassword);
                        rs = pstmt.executeQuery();
                        if (rs.next()) {    // 비밀번호가 맞다면
                            System.out.print("새로운 비밀번호: ");
                            int newPassword = sc.nextInt();
                            if (Integer.toString(newPassword).length() != 4) {
                                System.out.println("숫자 네자리를 입력하세요");
                                return;
                            } else {
                                pstmt = conn.prepareStatement(
                                        "UPDATE accounts SET a_password = ? WHERE a_number = ?");
                                pstmt.setInt(1, newPassword);
                                pstmt.setInt(2, aNumber);
                                pstmt.executeUpdate();
                                System.out.println("비밀번호가 성공적으로 변경되었습니다.");
                            }
                            break;
                        } else {
                            System.out.println("비밀번호가 틀렸습니다.");
                        }

                    }
                } else {
                    System.out.println("존재하지 않는 계좌입니다.");
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("다시 입력하세요");
        }
    }




    private String getValidInput(Scanner sc, String prompt, Validator validator, String query) throws
            SQLException {
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
        if (input.matches("^\\d{11}$")) {
            input = input.substring(0, 3) + "-" + input.substring(3, 7) + "-" + input.substring(7);
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
        if (input.matches("^[a-zA-Z0-9]+$")) {
            return true;
        } else {
            System.out.println("영어 대소문자와 숫자만 사용 가능합니다.");
            return false;
        }
    }

    private boolean isValidName(String input) {
        if (input.matches("^[a-zA-Z]+$") || input.matches("^[가-힣]+$")) {
            return true;
        } else {
            System.out.println("영문으로만, 또는 한글로만 작성되어야 합니다.");
            return false;
        }
    }

    private boolean isValidPhone(String input) {
        if (input.matches("^\\d{3}-\\d{4}-\\d{4}$|^\\d{11}$")) {
            return true;
        } else {
            System.out.println("3-4-4의 숫자 형태나 11자리의 숫자 형태만 허용됩니다.");
            return false;
        }
    }
}