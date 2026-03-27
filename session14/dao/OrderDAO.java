package session14.dao;

import session14.utils.DBConnection;

import java.sql.*;

public class OrderDAO {

	public void placeOrder(int userId, int productId, int quantity) {
		Connection conn = null;

		try {
			conn = DBConnection.openConnection();

			conn.setAutoCommit(false);

			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

			// ================== 1. CHECK STOCK + LOCK ==================
			String checkSql = "SELECT stock, price FROM Products WHERE product_id=? FOR UPDATE";
			PreparedStatement psCheck = conn.prepareStatement(checkSql);
			psCheck.setInt(1, productId);

			ResultSet rs = psCheck.executeQuery();

			if (!rs.next()) {
				throw new Exception("Product not found");
			}

			int stock = rs.getInt("stock");
			double price = rs.getDouble("price");

			if (stock < quantity) {
				throw new Exception("Out of stock");
			}

			// ================== 2. UPDATE STOCK ==================
			String updateStock = "UPDATE Products SET stock = stock - ? WHERE product_id=?";
			PreparedStatement psUpdate = conn.prepareStatement(updateStock);
			psUpdate.setInt(1, quantity);
			psUpdate.setInt(2, productId);
			psUpdate.executeUpdate();

			// ================== 3. CREATE ORDER ==================
			String insertOrder = "INSERT INTO Orders(user_id) VALUES(?)";
			PreparedStatement psOrder = conn.prepareStatement(insertOrder, Statement.RETURN_GENERATED_KEYS);
			psOrder.setInt(1, userId);
			psOrder.executeUpdate();

			ResultSet key = psOrder.getGeneratedKeys();
			key.next();
			int orderId = key.getInt(1);

			// ================== 4. BATCH INSERT ORDER DETAILS ==================
			String detailSql = "INSERT INTO Order_Details(order_id, product_id, quantity, price) VALUES(?,?,?,?)";
			PreparedStatement psDetail = conn.prepareStatement(detailSql);

			psDetail.setInt(1, orderId);
			psDetail.setInt(2, productId);
			psDetail.setInt(3, quantity);
			psDetail.setDouble(4, price);

			psDetail.addBatch();
			psDetail.executeBatch();

			// ================== COMMIT ==================
			conn.commit();
			System.out.println("Order success by user " + userId);

		} catch (Exception e) {
			try {
				if (conn != null) conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			System.out.println("Fail: " + e.getMessage());

		} finally {
			try {
				if (conn != null) {
					conn.setAutoCommit(true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}