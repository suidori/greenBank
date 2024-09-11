package Menu;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class ClerkDetailedMenu {
    public ClerkDetailedMenu() {
        this.conn = conn;
    }

    private Connection conn;



    public void cdm() throws SQLException {
        Scanner sc = new Scanner(System.in);
        DBClerk dbClerk = new DBClerk();

        while (true) {
            System.out.println("""
                    [계좌 관리]
                    1. 신규 계좌 개설
                    2. 예금주 관리
                    3. 계좌 비밀번호 변경
                    4. 뒤로
                    """);
            try {
                switch (sc.nextInt()) {
                    case 1:
                        dbClerk.insert();
                        throw new Exception("메인 메뉴로");
                    case 2:
                        while (true) {
                            System.out.println("""
                                    예금주 관리
                                    1. 추가
                                    2. 삭제         
                                    3. 예금주 조회               
                                    4. 뒤로
                                    """);
                            switch (sc.nextInt()) {
                                case 1:
                                    dbClerk.updateOwners();
                                    throw new Exception("메인 메뉴로");
                                case 2:
                                    dbClerk.delOwners();
                                    throw new Exception("메인 메뉴로");
                                case 3:
                                    dbClerk.checkOwners();
                                    throw new Exception("메인 메뉴로");
                                case 4:

                                default:
                                    throw new RuntimeException("잘못된 입력");
                            }
                        }
                    case 3:
                        dbClerk.updateApassword();
                        break;
                    case 4:
                        break;
                    default:

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
