package Menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class DBCustomer {

    private int u_idx;
    Scanner sc;
    Connection conn;
    PreparedStatement pstmt;
    ResultSet rs;

    public DBCustomer(int u_idx, Scanner sc, Connection conn) {
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

    //입금/출금
    public void depositWithdraw() {
        try {
            while (true) {
                pstmt = conn.prepareStatement("select * from owners where u_idx = ?");
                pstmt.setInt(1, u_idx);
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
                        break;
                    }
                    //계좌검증하는곳
                    if (!accountList.contains(accountNumbers)) {
                        System.out.println("본인 소유의 계좌번호가 아닙니다.");
                        continue;
                    }

                    pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
                    pstmt.setInt(1, accountNumbers);
                    rs = pstmt.executeQuery();

                    System.out.println("현재 고객님의 계좌 잔액은 " + rs.getLong("a_balance") + "원입니다.");
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


                                pstmt = conn.prepareStatement("SELECT * FROM history WHERE a_number = ? ORDER BY timestamp_column DESC LIMIT 1");
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
                        case 2:
                            pstmt = conn.prepareStatement("select * from accounts where a_number = ?");
                            pstmt.setInt(1, accountNumbers);
                            rs = pstmt.executeQuery();

                            System.out.println("""
                                    선택한 계좌의 잔액을 조회합니다.
                                    계좌번호: %d,
                                    잔액: %d
                                    """.formatted(rs.getInt("a_number"), rs.getInt("a_balance")));

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


                                pstmt = conn.prepareStatement("SELECT * FROM history WHERE a_number = ? ORDER BY timestamp_column DESC LIMIT 1");
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
                        case 3:
                            break;
                        case 4:
                            return;
                        default:
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            } catch(Exception e){
                e.printStackTrace();
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