package session14.dao;

import session14.utils.DBConnection;

import java.sql.*;

public class ReportDAO {

	public void getTopBuyers() throws Exception {
		Connection conn = DBConnection.openConnection();

		CallableStatement cs = conn.prepareCall("{CALL SP_GetTopBuyers()}");
		ResultSet rs = cs.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("username") + " - " + rs.getInt("total_items"));
		}
	}
	public void getRevenue() throws Exception {
		Connection conn = DBConnection.openConnection() ;

		CallableStatement cs = conn.prepareCall("{CALL SP_GetRevenue()}");
		ResultSet rs = cs.executeQuery();

		while (rs.next()) {
			System.out.println(rs.getString("product_name")
					                   + " - Revenue: " + rs.getDouble("revenue"));
		}
	}
}
