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
public class GestionFactureModel {
    private static Connection connection; 
    
    public GestionFactureModel() {
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





    public int modifierFacture(Connection conn, Facture facture) {
        try {
            // Préparation de la requête SQL pour la mise à jour de la facture
            String query = "UPDATE facture SET codeclient=?, designation=?, quantite=?, pu=?, pt=?, total=?, fodec=?, tva=?, totalttc=?, netAPayer=? WHERE id=?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            
            // Remplacement des paramètres dans la requête
            preparedStatement.setInt(1, facture.getCodeClient());
            preparedStatement.setString(2, facture.getDesignation());
            preparedStatement.setInt(3, facture.getQuantite());
            preparedStatement.setFloat(4, facture.getPU());
            preparedStatement.setFloat(5, facture.getPt());
            preparedStatement.setFloat(6, facture.getTotal());
            preparedStatement.setFloat(7, facture.getFodec());
            preparedStatement.setFloat(8, facture.getTVA());
            preparedStatement.setFloat(9, facture.getTotalTTC());
            preparedStatement.setFloat(10, facture.getNetAPayer());
            preparedStatement.setInt(11, facture.getId());

            // Exécution de la requête
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Gestion de l'exception SQL
            return 0; // Indique un échec de la mise à jour
        }
    }

    
    
    
    
    public int ajouterFacture(Connection connection, int codeClient, int quantite, String designation, Date date, float pu, float pt, float total, float fodec, float tva, float totalTTC, float netAPayer) {
        try {
            String req = "INSERT INTO facture (CodeClient, designation, quantite, date, PU, PT, total, Fodec, TVA, TotalTTC, NetAPayer) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setInt(1, codeClient);
            ps.setString(2, designation);
            ps.setInt(3, quantite);
            ps.setDate(4, new java.sql.Date(date.getTime()));
            ps.setFloat(5, pu);
            ps.setFloat(6, pt);
            ps.setFloat(7, total);
            ps.setFloat(8, fodec);
            ps.setFloat(9, tva);
            ps.setFloat(10, totalTTC);
            ps.setFloat(11, netAPayer);

            return ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

			
			
	 public void supprimerFacture(int id) {
				        String query = "DELETE FROM facture WHERE id = ?";
				        try (PreparedStatement statement = connection.prepareStatement(query)) {
				            statement.setInt(1, id);
				            statement.executeUpdate();
				        } catch (SQLException e) {
				            e.printStackTrace();
				        }
				    }
			}
