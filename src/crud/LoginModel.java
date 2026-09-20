package crud;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import DAO.ConnectionSingleton;

public class LoginModel {
	 private Connection connection;

	    public LoginModel() {
	        connection = ConnectionSingleton.getConnex(); // Utilisation de la classe ConnectionSingleton pour obtenir la connexion
	    }

	    public boolean authenticate(String username, String password) {
	        try {
	            Statement stmt = connection.createStatement();
	            ResultSet loginMP = stmt.executeQuery("SELECT * FROM admin");

	            while (loginMP.next()) {
	                String s1 = loginMP.getString(3);
	                String s2 = loginMP.getString(4);

	                if (username.equals(s1) && password.equals(s2)) {
	                    return true;
	                }
	            }
	            
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return false;
	    }
	}
