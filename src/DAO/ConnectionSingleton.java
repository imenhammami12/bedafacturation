package DAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionSingleton {
	private static Connection connex;


	public static Connection getConnex() {
		return connex;
	}
	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			connex=DriverManager.getConnection("jdbc:mysql://localhost:3306/facture?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=UTC","root","");
			System.out.println("conexion ok... ");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}