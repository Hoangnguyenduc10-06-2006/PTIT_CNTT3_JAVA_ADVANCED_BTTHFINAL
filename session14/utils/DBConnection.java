package session14.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private final static String DRIVER="com.mysql.cj.jdbc.Driver";
	private final static String URL="jdbc:mysql://localhost:3306/flash_sale";
	private final static String USERNAME="root";
	private final static String PASSWORD="Hoangduc2006";
	public static Connection openConnection(){
		Connection con=null;
		try{
			Class.forName(DRIVER);
			con= DriverManager.getConnection(URL,USERNAME,PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			throw new RuntimeException(e);
		}
		return con;
	}
}
