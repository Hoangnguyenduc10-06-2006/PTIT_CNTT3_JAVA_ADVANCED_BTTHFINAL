import java.sql.*;

public class TransferService {

    public static void transfer(String fromId, String toId, double amount) {
        Connection conn = null;

        try {
            conn = DataConnection.openConnection();


            conn.setAutoCommit(false);


            String Sql = "SELECT Balance FROM Accounts WHERE AccountId = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(Sql);

            preparedStatement.setString(1, fromId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                throw new RuntimeException("Tài khoản gửi không tồn tại");
            }

            double balance = resultSet.getDouble("Balance");
            if (balance < amount) {
                throw new RuntimeException("Không đủ tiền để chuyển");
            }



            CallableStatement callableStatement = conn.prepareCall("{CALL sp_UpdateBalance(?, ?)}");


            callableStatement.setString(1, fromId);
            callableStatement.setDouble(2, -amount);
            callableStatement.execute();

            callableStatement.setString(1, toId);
            callableStatement.setDouble(2, amount);
            callableStatement.execute();


            conn.commit();
            System.out.println("Chuyển khoản thành công!");


            String resultSql = "SELECT * FROM Accounts WHERE AccountId IN (?, ?)";
            PreparedStatement preparedStatementResult = conn.prepareStatement(resultSql);
            preparedStatementResult.setString(1, fromId);
            preparedStatementResult.setString(2, toId);

            ResultSet rs2 = preparedStatementResult.executeQuery();

            System.out.println("======== thông tin giao dịch ============");
            while (rs2.next()) {
                System.out.println(
                        rs2.getString("AccountId") + " | " +
                                rs2.getString("FullName") + " | " +
                                rs2.getDouble("Balance")
                );
            }

        } catch (Exception e) {
            try {
                if (conn != null) {
                    conn.rollback();
                    System.err.println("Giao dịch thất bại!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("Lỗi: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}