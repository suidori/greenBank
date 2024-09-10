package org.example;

import org.example.sang.History;
import org.example.sang.Owners;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import static org.example.ClerkMenu6.ownersManage;


public class AccClerk {
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    Connection conn = null;


    public AccClerk() {
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


//=========================== ▼ 고객메인>3.본인 계좌 조회===================================

    //로그인할 때 입력한 나의 u_id값을 받아서.....?

    public void selectMyAcc(int u_id) {
        List<Owners> list = new ArrayList<>();

        System.out.println("내 계좌 조회");

        try {
            pstmt = conn.prepareStatement(
                    "SELECT * FROM users, owners WHERE u_id = ? and users.u_idx = owners.u_idx"); //order by a_number desc 필요할까....?
            pstmt.setInt(1, u_id);  //로그인할 떄 입력한 아이디값 받은 거
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Owners owners = Owners.builder()
                        .a_number(rs.getInt("a_number"))
                        .build();
                list.add(owners);
            }
            list.stream()
                    .forEach(System.out::println);
            //빌더 안 쓰고 걍 출력되도록!

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


//=========================== ▼ 관리자메뉴 > 4.계좌 정보 조회===================================


    public void select() throws SQLException {      //최근거래내역 5개
        Scanner sc = new Scanner(System.in);
        List<Owners> list1 = new ArrayList<>();
        List<History> list2 = new ArrayList<>();

        while (true) {
            System.out.println();
            System.out.println("계좌 정보 조회");
            while (true) {
                System.out.print("예금주명: ");
                String uName = sc.next();
                if (Pattern.matches("^[a-zA-Z가-힣]+$", uName)) {

                    while (true) {
                        System.out.println("아이디: ");
                        String uId = sc.next();
                        if (Pattern.matches("^[a-zA-Z0-9]+$", uId)) {

                            try {
                                pstmt = conn.prepareStatement("SELECT * FROM users WHERE u_name = ? and u_id = ?");
                                pstmt.setString(1, uName);
                                pstmt.setString(2, uId);
                                rs = pstmt.executeQuery();

                                if (rs.next()) {    //존재하는회원
                                    try {
                                        pstmt = conn.prepareStatement("SELECT * FROM owners,users WHERE owners.u_idx = users.u_idx and users.u_name = ? and users.u_id = ?");
                                        pstmt.setString(1, uName);
                                        pstmt.setString(2, uId);
                                        rs = pstmt.executeQuery();

                                        while (rs.next()) {
                                            Owners owners = Owners.builder()
                                                    .u_idx(rs.getInt("u_idx"))
                                                    .a_number(rs.getInt("a_number"))
                                                    .build();
                                            list1.add(owners);
                                        }
                                        list1.stream()
                                                .forEach(System.out::println);

                                        while (true) {
                                            System.out.println("조회할 계좌번호: ");
                                            int aNumber = sc.nextInt();
                                            try {
                                                pstmt = conn.prepareStatement("SELECT * from history where a_number = ? ORDER BY h_timestamp DESC LIMIT 5");
                                                pstmt.setInt(1, aNumber);
                                                ResultSet rs = pstmt.executeQuery();
                                                if (rs.next()) {
                                                    while (true) {
                                                        System.out.println("비밀번호: ");
                                                        int aPassword = sc.nextInt();
                                                        try {
                                                            pstmt = conn.prepareStatement("SELECT * from history, accounts where history.a_number = ? AND history.a_number = accounts.a_number AND a_password = ? ORDER BY h_timestamp DESC LIMIT 5");
                                                            pstmt.setInt(1, aNumber);
                                                            pstmt.setInt(2, aPassword);
                                                            rs = pstmt.executeQuery();
                                                            if (rs.next()) {
                                                                while (rs.next()) {
                                                                    History history = History.builder()
                                                                            .u_idx(rs.getInt("u_idx"))
                                                                            .a_number(rs.getInt("a_number"))
                                                                            .h_calc(rs.getInt("h_calc"))
                                                                            .h_balance(rs.getInt("h_balance"))
                                                                            .h_timestamp(rs.getString("h_timestamp"))
                                                                            .build();
                                                                    list2.add(history);
                                                                }
                                                                list2.stream()
                                                                        .forEach(System.out::println);
                                                                break;
                                                            } else {
                                                                System.out.println("비밀번호가 틀렸습니다.");
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                } else {
                                                    System.out.println("다시 입력하세요.");
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();

                                            }
                                            new ClerkMenu();
                                            return; // 메서드 종료
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    System.out.println("존재하지 않는 회원입니다.");
                                }
                                break;

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            System.out.println("아이디는 영어 대소문자와 숫자만 사용할 수 있습니다.");
                        }
                    }
                    break;
                } else {
                    System.out.println("영문으로만, 또는 한글로만 작성되어야 합니다.");
                }

            }
        }

    }
//=========================== ▼ 관리자메뉴 > 6.계좌 관리 > 1. 신규 계좌 개설 ===================================


    public void insert() throws SQLException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("신규계좌개설");
            System.out.println();
            System.out.print("회원명: ");
            String uName = sc.next();
            if (Pattern.matches("^[a-zA-Z가-힣]+$", uName)) {
                System.out.println("아이디: ");
                String uId = sc.next();
                if (Pattern.matches("^[a-zA-Z0-9]+$", uId)) {


                    try {
                        pstmt = conn.prepareStatement("SELECT * FROM users WHERE u_name = ? and u_id = ?");
                        pstmt.setString(1, uName);
                        pstmt.setString(2, uId);
                        rs = pstmt.executeQuery();

                        if (rs.next()) {    //존재하는회원
                            while (true) {
                                System.out.println(uName + "님(회원번호: " + rs.getInt("u_idx") + ") 신규계좌 개설");


                                System.out.print("계좌 비밀번호 설정(숫자 네자리): ");
                                int aPassword = sc.nextInt();
                                String password = Integer.toString(aPassword);

                                if (password.length() != 4) {
                                    System.out.println("숫자 네자리를 입력하세요");
                                } else if (password.contains(" ")) {
                                    System.out.println("공백은 들어갈 수 없습니다.");
                                } else {

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

                                    break;

                                }

                            }
                        } else {
                            System.out.println("존재하지 않는 회원입니다.");

                        }


                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                } else {
                    System.out.println("아이디는 영어 대소문자와 숫자만 사용할 수 있습니다.");
                }

            } else {
                System.out.println("영문으로만, 또는 한글로만 작성되어야 합니다.");
            }
            new ClerkMenu6();
        }
    }

//=========================== ▼ 관리자메뉴 > 6.계좌 관리 > 2.예금주 관리 > 1.추가 ====살려주세요===============================

    public void updateOwners() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println("예금주 추가");
        while (true) {

            System.out.println("예금주를 추가할 계좌번호: ");
            int aNumber = sc.nextInt();
            try {
                pstmt = conn.prepareStatement("SELECT * FROM owners WHERE a_number = ?");
                pstmt.setInt(1, aNumber);
                rs = pstmt.executeQuery();
                if (rs.next()) {//존재하는 계좌라면
                    while (true) {
                        System.out.println("비밀번호: ");
                        int aPassword = sc.nextInt();
                        try {
                            pstmt = conn.prepareStatement("SELECT * from history, accounts where history.a_number = ? AND history.a_number = accounts.a_number AND a_password = ? ORDER BY h_timestamp DESC LIMIT 5");
                            pstmt.setInt(1, aNumber);
                            pstmt.setInt(2, aPassword);
                            rs = pstmt.executeQuery();
                            if (rs.next()) {    //비번 맞으면

                                while (true) {
                                    System.out.print("추가할 회원명: ");
                                    String uName = sc.next();

                                    System.out.println("아이디: ");
                                    String uId = sc.next();
                                    try {
                                        pstmt = conn.prepareStatement("SELECT * FROM users WHERE u_id = ? AND u_name = ?");
                                        pstmt.setString(1, uId);
                                        pstmt.setString(2, uName);
                                        rs = pstmt.executeQuery();
                                        if (rs.next()) {
                                            try {
                                                pstmt = conn.prepareStatement("SELECT * FROM users, owners WHERE users.u_id = ? AND users.u_name = ? AND users.u_idx = ? AND users.u_idx = owners.i_idx");
                                                pstmt.setString(1, uId);
                                                pstmt.setString(2, uName);
                                                pstmt.setInt(3, rs.getInt("owners.u_idx"));
                                                rs = pstmt.executeQuery();
                                                if (!rs.next()) {
                                                    try {

                                                        pstmt = conn.prepareStatement("INSERT INTO owners (u_idx, a_number) VALUES (?,?)");
                                                        pstmt.setInt(1, rs.getInt("u_idx"));
                                                        pstmt.setInt(2, aNumber);
                                                        pstmt.executeUpdate();
                                                        System.out.println("추가되었습니다.");
                                                        break;

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                } else {
                                                    System.out.println("이미 존재하는 예금주입니다.");
                                                }

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        } else {
                                            System.out.println("아이디 혹은 비밀번호를 다시 입력해주세요.");
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                System.out.println("비밀번호가 틀렸습니다.");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
//                } else {
//                    System.out.println("회원명 또는 아이디를 다시 입력하세요");
//                }
                } else {
                    System.out.

                            println("존재하지 않는 계좌입니다.");
                    System.out.

                            println();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

//=========================== ▼ 관리자메뉴 > 6.계좌 관리 > 2.예금주 관리 > 2.삭제 ===================================


    public void delOwners() throws SQLException {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("계좌번호: ");
            int aNumber = sc.nextInt();

            try {
                pstmt = conn.prepareStatement("SELECT * FROM owners WHERE a_number = ?");
                pstmt.setInt(1, aNumber);
                rs = pstmt.executeQuery();

                if (rs.next()) {   //존재하는 계좌라면
                    while (true) {
                        System.out.print("삭제할 예금주 이름: ");
                        String uName = sc.next();
                        if (Pattern.matches("^[a-zA-Z가-힣]+$", uName)) {

                            System.out.println("삭제할 예금주 아이디: ");
                            String uId = sc.next();
                            if (Pattern.matches("^[a-zA-Z0-9]+$", uId)) {

                                try {
                                    pstmt = conn.prepareStatement(
                                            "SELECT * FROM users, owners WHERE users.u_name = ? AND users.u_id = ? AND users.u_idx = owners.u_idx");
                                    pstmt.setString(1, uName);
                                    pstmt.setString(2, uId);
                                    rs = pstmt.executeQuery();
                                    if (rs.next()) {
                                        int count = rs.getInt(1);

                                        if (count == 1) {
                                            System.out.println("예금주를 삭제할 수 없습니다.");
                                            break;
                                        } else {
                                            try {
                                                pstmt = conn.prepareStatement("DELETE FROM owners where u_idx = ?");
                                                pstmt.setInt(1, rs.getInt("u_idx"));
                                                pstmt.executeUpdate();
                                                System.out.println("삭제되었습니다.");
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            break;
                                        }

                                    } else {
                                        System.out.println("해당 계좌의 예금주 정보와 일치하지 않습니다.");
                                    }
                                    break;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                System.out.println("아이디는 영어 대소문자와 숫자만 사용할 수 있습니다.");
                            }
                        } else {
                            System.out.println("영문으로만, 또는 한글로만 작성되어야 합니다.");
                        }
                    }
                } else {
                    System.out.println("존재하지 않는 계좌입니다.");
                }
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        ownersManage();
    }


//=========================== ▼ 관리자메뉴 > 6.계좌 관리 > 2.예금주 관리 > 3.예금주 조회 ===================================

    public void checkOwners() throws SQLException {
        Scanner sc = new Scanner(System.in);
        System.out.println();
        System.out.println("예금주 조회");

        while (true) {
            System.out.print("조회할 계좌번호: ");

            int aNumber = sc.nextInt();
            String number = Integer.toString(aNumber);

            if (number.contains(" ")) {
                System.out.println("공백은 들어갈 수 없습니다.");
            } else if (sc.equals("취소")) {
                new ClerkMenu6();
            }

            try {
                pstmt = conn.prepareStatement("SELECT * FROM owners WHERE a_number = ?");
                pstmt.setInt(1, aNumber);
                rs = pstmt.executeQuery();
                if (rs.next()) {    //존재하는 계좌라면
                    try {
                        System.out.println();
                        System.out.println(aNumber + "계좌 예금주 목록: ");

                        pstmt = conn.prepareStatement("SELECT users.u_idx, users.u_name FROM users JOIN owners ON users.u_idx = owners.u_idx WHERE owners.a_number = ?");
                        pstmt.setInt(1, aNumber);
                        rs = pstmt.executeQuery();

                        while (rs.next()) {
                            int u_idx = rs.getInt("u_idx");
                            String u_name = rs.getString("u_name");
                            System.out.println("회원번호: " + u_idx + ", 예금주명: " + u_name);
                        }
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                    break;
                } else {
                    System.out.println("존재하지 않는 계좌입니다.");
                    System.out.println();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        ownersManage();

    }


//=========================== ▼ 관리자메뉴 > 6.계좌 개설 및 수정 > 3.계좌 비밀번호 변경===================================


    public void updateApassword() throws SQLException {
        Scanner sc = new Scanner(System.in);

        while (true) {  // 맞느 계좌번호를 받을 때까지 반복
            System.out.println("계좌 비밀번호 변경");
            System.out.println("비밀번호를 변경할 계좌번호: ");
            int aNumber = sc.nextInt();

            try {
                pstmt = conn.prepareStatement("SELECT * FROM accounts WHERE a_number = ?");
                pstmt.setInt(1, aNumber);
                rs = pstmt.executeQuery();

                if (rs.next()) {    // 존재하는 계좌번호라면
                    while (true) {  // 비밀번호가 맞을 때까지 반복
                        System.out.println("기존 비밀번호: ");
                        int aPassword = sc.nextInt();
                        String password = Integer.toString(aPassword);

                        if (password.length() != 4) {
                            System.out.println("숫자 네자리를 입력하세요");
                        } else if (password.contains(" ")) {
                            System.out.println("공백은 들어갈 수 없습니다.");

                        } else {

                            try {
                                pstmt = conn.prepareStatement("SELECT * FROM accounts WHERE a_number = ? AND a_password = ?");
                                pstmt.setInt(1, aNumber);
                                pstmt.setInt(2, aPassword);
                                rs = pstmt.executeQuery();

                                if (rs.next()) {    // 비밀번호가 맞다면
                                    System.out.print("새로운 비밀번호: ");
                                    aPassword = sc.nextInt();
                                    String newPassword = Integer.toString(aPassword);

                                    if (newPassword.length() != 4) {
                                        System.out.println("숫자 네자리를 입력하세요");
                                    } else if (newPassword.contains(" ")) {
                                        System.out.println("공백은 들어갈 수 없습니다.");
                                    } else {
                                        try {
                                            pstmt = conn.prepareStatement(
                                                    "UPDATE accounts SET a_password = ? WHERE a_number = ?");
                                            pstmt.setInt(1, aPassword);
                                            pstmt.setInt(2, aNumber);
                                            pstmt.executeUpdate();
                                            System.out.println("비밀번호가 성공적으로 변경되었습니다.");

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } else {
                                    System.out.println("기존 비밀번호가 틀렸습니다.");
                                }
                                break;

                            } catch (Exception e) {
                                e.printStackTrace();

                                // 계좌비번 다 끝
                                break;
                            }
                        }
                    }
                } else {
                    System.out.println("존재하지 않는 계좌번호입니다.");
                }
                break;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        new ClerkMenu6();
    }


}














