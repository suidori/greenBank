//package org.example;
//
//public class memo {
//
//    import java.sql.*;
//
//    public class BankAccountManager {
//
//        public static void insert(int userId, String password) {
//            System.out.println("신규계좌개설\n" +
//                    "회원번호: " + userId + "\n" +
//                    "비밀번호: " + password);
//
//            String jdbcUrl = "jdbc:mysql://192.168.0.53:8888/Bank";
//            String jdbcUser = "root";
//            String jdbcPassword = "1234";
//
//            try (Connection conn = DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword)) {
//                // Start a transaction
//                conn.setAutoCommit(false);
//
//                // Insert a new account with a balance of 0
//                String insertAccountSQL = "INSERT INTO accounts (a_balance) VALUES (0)";
//                try (PreparedStatement pstmt = conn.prepareStatement(insertAccountSQL, Statement.RETURN_GENERATED_KEYS)) {
//                    pstmt.executeUpdate();
//
//                    // Get the generated account number
//                    ResultSet rs = pstmt.getGeneratedKeys();
//                    if (rs.next()) {
//                        int accountNumber = rs.getInt(1);
//
//                        // Insert into owners table with the generated account number
//                        String insertOwnerSQL = "INSERT INTO owners (u_idx, a_number) VALUES (?, ?)";
//                        try (PreparedStatement pstmt2 = conn.prepareStatement(insertOwnerSQL)) {
//                            pstmt2.setInt(1, userId);
//                            pstmt2.setInt(2, accountNumber);
//                            pstmt2.executeUpdate();
//                        }
//
//                        // Commit transaction
//                        conn.commit();
//
//                        // Print the account number
//                        System.out.println("계좌가 개설되었습니다. 계좌번호는 [" + accountNumber + "] 입니다");
//                    } else {
//                        System.out.println("계좌 개설 실패.");
//                    }
//                } catch (SQLException e) {
//                    // Rollback transaction if any error occurs
//                    conn.rollback();
//                    e.printStackTrace();
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//        public static void main(String[] args) {
//            // Example usage
//            insert(1, "password123");
//        }
//    }
//
//}
