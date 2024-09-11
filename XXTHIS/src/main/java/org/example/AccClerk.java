package org.example;

import org.example.sang.History;
import org.example.sang.Owners;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
                    "SELECT * FROM users, owners WHERE u_id = ? and users.u_idx = owners.u_idx"); //order by a_number desc 필요할까....?딱히..
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


    public void select() {      //최근거래내역 5개
        Scanner sc = new Scanner(System.in);
        List<History> list = new ArrayList<>();

        while (true) {
            System.out.println("계좌 정보 조회");
            System.out.print("성함: ");
            int uName = sc.nextInt();
            try {
                pstmt = conn.prepareStatement("SELECT * FROM users WHERE u_name = ?");
                pstmt.setInt(1, uName);
                rs = pstmt.executeQuery();

                if (rs.next()) {    //존재하는 회원이라면 그 사람이 가지고있는 계좌 쭉 보이도록

                    //계좌출력~!~!!

                    while (true) {
                        System.out.println("계좌번호: ");
                        int aNumber = sc.nextInt();

                        try {
                            pstmt = conn.prepareStatement("SELECT * FROM history WHERE a_number = ?");
                            pstmt.setInt(1, aNumber);
                            rs = pstmt.executeQuery();

                            if (rs.next()) {
                                while (true) {      //존재하는 계좌라면
                                    try {
                                        pstmt = conn.prepareStatement("SELECT * from history where a_number = ? ORDER BY h_timestamp DESC LIMIT 5");
                                        pstmt.setInt(1, aNumber);
                                        ResultSet rs = pstmt.executeQuery();

                                        while (rs.next()) {
                                            History history = History.builder()
                                                    .u_idx(rs.getInt("u_idx"))
                                                    .a_number(rs.getInt("a_number"))
                                                    .h_calc(rs.getInt("h_calc"))
                                                    .h_balance(rs.getInt("h_balance"))
                                                    .h_timestamp(rs.getString("h_timestamp"))
                                                    .build();
                                            list.add(history);
                                        }
                                        list.stream()
                                                .forEach(System.out::println);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                }
                            } else {
                                System.out.println("존재하지 않는 계좌입니다.");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                } else {
                    System.out.println("존재하지 않는 회원입니다.");

                }

            } catch (Exception e) {
                e.printStackTrace();

            }

        }

    }
//=========================== ▼ 관리자메뉴 > 6.계좌 개설 및 수정 > 1. 신규 계좌 개설 ===================================


    public void insert() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("신규계좌개설");
            System.out.print("회원번호: ");         //회원명 받기
            int uIdx = sc.nextInt();

            try {
                pstmt = conn.prepareStatement("SELECT * FROM users WHERE u_idx = ?");
                pstmt.setInt(1, uIdx);
                rs = pstmt.executeQuery();

                if (rs.next()) {    //존재하는회원번호
                    System.out.print("계좌 비밀번호 설정: ");
                    int aPassword = sc.nextInt();

                    pstmt = conn.prepareStatement(
                            "INSERT INTO accounts (a_balance,a_password) VALUES (0,?);");
                    pstmt.setInt(1, aPassword);
                    pstmt.executeUpdate();

                    pstmt = conn.prepareStatement("SET @last_a_number = LAST_INSERT_ID();");
                    pstmt.executeUpdate();

                    pstmt = conn.prepareStatement(
                            "INSERT INTO owners (u_idx, a_number) VALUES (?,@last_a_number);");
                    pstmt.setInt(1, uIdx);
                    pstmt.executeUpdate();

                    System.out.print("계좌가 개설되었습니다. 계좌번호는 [");
                    pstmt = conn.prepareStatement("SELECT * FROM accounts WHERE a_number = @last_a_number;");
                    rs = pstmt.executeQuery();

                    rs.next();
                    System.out.println(rs.getInt("a_number") + "] 입니다");

                    break;

                } else {
                    System.out.println("존재하지 않는 회원입니다.");

                }

            } catch (Exception e) {
                e.printStackTrace();

            }

        }
        new ClerkMenu6();

    }

//=========================== ▼ 관리자메뉴 > 6.계좌 개설 및 수정 > 2.예금주 관리 ===================================


    public void updateOwners() {
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!switch구문으로 수정하기
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!예금주 관리 메뉴 컁 따로 빼자....넘더러움


        while (true) {
            Scanner sc = new Scanner(System.in);
            System.out.println();
            System.out.println("예금주 관리");
            System.out.println("""
                    1. 추가
                    2. 삭제           //예금주 한 명만 있을 때는 삭제 못하도록
                    3. 예금주 조회               
                    4. 뒤로
                    """);
            int uo = sc.nextInt();

            if (uo == 1) {

                while (true) {  // 계좌번호 확인
                    System.out.println("추가");
                    System.out.println("소유주를 추가할 계좌번호: ");
                    int aNumber = sc.nextInt();

                    try {
                        pstmt = conn.prepareStatement("SELECT * FROM owners WHERE a_number = ?");
                        pstmt.setInt(1, aNumber);
                        rs = pstmt.executeQuery();

                        if (rs.next()) {                            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!중복처리 추가
                            while (true) {  // 회원번호 확인                  이름 받고 회원번호 출력되게..?
                                System.out.println("추가할 회원번호: ");
                                int uIdx = sc.nextInt();

                                try {
                                    pstmt = conn.prepareStatement("INSERT INTO owners (u_idx, a_number) VALUES (?,?)");
                                    pstmt.setInt(1, uIdx);
                                    pstmt.setInt(2, aNumber);
                                    int rowsAffected = pstmt.executeUpdate();

                                    if (rowsAffected > 0) {
                                        System.out.println("추가되었습니다.");
                                        break;  // 회원번호 끗
                                    } else {
                                        System.out.println("회원번호 추가에 실패했습니다.");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            break;  // 계좌번호 끗
                        } else {
                            System.out.println("존재하지 않는 계좌입니다.");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            } else if (uo == 2) {
                while (true) {
                    System.out.println("삭제");
                    System.out.println("소유주를 삭제할 계좌번호: ");
                    int aNumber = sc.nextInt();

                    try {
                        pstmt = conn.prepareStatement("SELECT * FROM owners WHERE a_number = ?");
                        pstmt.setInt(1, aNumber);
                        rs = pstmt.executeQuery();

                        if (rs.next()) {    //존재하는 계좌라면
                            System.out.println("삭제할 고객번호: ");
                            int uIdx = sc.nextInt();
                            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!존재하지 않는 고객번호 추가
                            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!이미 등록된 고객번호 추가
                            try {
                                pstmt = conn.prepareStatement("DELETE FROM owners where u_idx = ?");
                                pstmt.setInt(1, uIdx);
                                pstmt.executeUpdate();

                            } catch (Exception e) {
                                e.printStackTrace();
                                // 소유주 삭제할 때 계정비번 입력받아서 본인확인 해야할까...?
                                System.out.println("삭제되었습니다.");
                            }
                            break;
                        } else {
                            System.out.println("존재하지 않는 계좌입니다.");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            } else if (uo == 3) {

                while (true) {
                    System.out.println();

                    System.out.println("예금주 조회");
                    System.out.print("조회할 계좌번호: ");
                    int aNumber = sc.nextInt();

                    try {
                        pstmt = conn.prepareStatement("SELECT * FROM owners WHERE a_number = ?");
                        pstmt.setInt(1, aNumber);
                        rs = pstmt.executeQuery();

                        if (rs.next()) {    //존재하는 계좌라면

                            List<Owners> list = new ArrayList<>();
                            try {
                                System.out.println();
                                System.out.println("예금주 목록: ");
                                pstmt = conn.prepareStatement("select * from owners where a_number = ? order by u_idx desc");
                                pstmt.setInt(1, aNumber);
                                ResultSet rs = pstmt.executeQuery();

                                while (rs.next()) {
                                    Owners owners = Owners.builder()
                                            .u_idx(rs.getInt("u_idx"))
                                            .a_number(rs.getInt("a_number"))
                                            .build();
                                    list.add(owners);
                                }
                                list.stream()
                                        .forEach(System.out::println);
                            } catch (Exception e) {
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
                    break;
                }


            } else {
                new ClerkMenu6();
                break;

            }
//            System.out.println("올바른 번호를 입력하세요.");


        }


    }
//=========================== ▼ 관리자메뉴 > 6.계좌 개설 및 수정 > 3.계좌 비밀번호 변경===================================


    public void updateApassword() {
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

                        try {
                            pstmt = conn.prepareStatement("SELECT * FROM accounts WHERE a_number = ? AND a_password = ?");
                            pstmt.setInt(1, aNumber);
                            pstmt.setInt(2, aPassword);
                            rs = pstmt.executeQuery();

                            if (rs.next()) {    // 비밀번호가 맞다면
                                System.out.print("새로운 비밀번호: ");
                                aPassword = sc.nextInt();

                                try {
                                    pstmt = conn.prepareStatement(
                                            "UPDATE accounts SET a_password = ? WHERE a_number = ?");
                                    pstmt.setInt(1, aPassword);
                                    pstmt.setInt(2, aNumber);
                                    pstmt.executeUpdate();
                                    System.out.println("비밀번호가 성공적으로 변경되었습니다.");
                                    break;  // 비밀번호 확인 끝
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                break;  // 계좌번호 입력 종료
                            } else {
                                System.out.println("기존 비밀번호가 틀렸습니다.");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    // 계좌비번 다 끝
                    break;

                } else {
                    System.out.println("존재하지 않는 계좌번호입니다.");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        new ClerkMenu6();
    }


//=========================== ▼ 관리자메뉴 > 6.계좌 개설 및 수정 > 4.계좌 해지===================================

//
//    public void delete() {
//        Scanner sc = new Scanner(System.in);
//
//        while (true) {  // 계좌번호 확인
//            System.out.println("계좌 해지");
//            System.out.println("해지할 계좌번호: ");
//            int aNumber = sc.nextInt();
//
//            try {
//                pstmt = conn.prepareStatement("SELECT * FROM accounts WHERE a_number = ?");
//                pstmt.setInt(1, aNumber);
//                rs = pstmt.executeQuery();
//
//                if (rs.next()) {    // 존재하는 계좌번호라면
//                    while (true) {  // 비밀번호 확인
//                        System.out.println("계좌 비밀번호: ");
//                        int aPassword = sc.nextInt();
//
//                        try {
//                            pstmt = conn.prepareStatement("SELECT * FROM accounts WHERE a_number = ? AND a_password = ?");
//                            pstmt.setInt(1, aNumber);
//                            pstmt.setInt(2, aPassword);
//                            rs = pstmt.executeQuery();
//
//                            if (rs.next()) {    // 비밀번호가 맞다면
//                                try {
//                                    pstmt = conn.prepareStatement("DELETE FROM accounts WHERE a_number = ? AND a_password = ?");
//                                    pstmt.setInt(1, aNumber);
//                                    pstmt.setInt(2, aPassword);
//                                    pstmt.executeUpdate();
//
//                                    System.out.println("정상적으로 해지되었습니다.");
//                                    break;  // 비밀번호 확인끗
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//
//                                break;  // 계좌번호 확인끗
//                            } else {
//                                System.out.println("비밀번호가 틀렸습니다. 다시 입력해주세요.");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    //비번계좌 다 끝
//                    break;
//
//                } else {
//                    System.out.println("존재하지 않는 계좌번호입니다. 다시 입력해주세요.");
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//

}














