package crud;


	import javafx.collections.FXCollections;
	import javafx.collections.ObservableList;
	import java.sql.Connection;
	import java.sql.PreparedStatement;
	import java.sql.ResultSet;
	import java.sql.SQLException;
	import java.util.Date;
	import entities.Facture;
	import DAO.ConnectionSingleton;
	public class ListeFactureModel {
	    private static Connection connection; 
	    
	    public ListeFactureModel() {
	        connection = ConnectionSingleton.getConnex(); // Utilisation de la classe ConnectionSingleton pour obtenir la connexion
	    }

	    public ObservableList<Facture> getAllFactures() {
	        ObservableList<Facture> factures = FXCollections.observableArrayList();

	        try {
	            String query = "SELECT * FROM facture";
	            PreparedStatement statement = connection.prepareStatement(query);
	            ResultSet resultSet = statement.executeQuery();

	            // Parcourir les résultats et créer des objets Facture correspondants
	            while (resultSet.next()) {
	                int id = resultSet.getInt("id");
	                int codeClient = resultSet.getInt("CodeClient");
	                int quantite = resultSet.getInt("quantite");
	                String designation = resultSet.getString("designation");
	                Date date = resultSet.getDate("date");
	                float PU = resultSet.getFloat("PU");
	                float PT = resultSet.getFloat("Pt");
	                float total = resultSet.getFloat("Total");
	                float fodec = resultSet.getFloat("Fodec");
	                float tva = resultSet.getFloat("TVA");
	                float totalTTC = resultSet.getFloat("TotalTTC");
	                float netAPayer = resultSet.getFloat("NetAPayer");

	                // Créer un nouvel objet Facture
	                Facture facture = new Facture(id, codeClient, quantite, designation, date, PU, PT, total, fodec, tva, totalTTC, netAPayer);
	                factures.add(facture);
	            }

	            // Fermer les ressources
	            resultSet.close();
	            statement.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return factures;
	    }


}