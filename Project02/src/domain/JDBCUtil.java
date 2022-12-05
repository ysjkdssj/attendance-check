
package domain;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtil {
	public Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String connectionString = "jdbc:mysql://localhost/check";
		String name = "root";
		String age = "";
		String id = "";
		String password = "";
		
		Connection con = null;
		try {
			con = DriverManager
					.getConnection(connectionString, name, age);
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		return con;
		
	}
}