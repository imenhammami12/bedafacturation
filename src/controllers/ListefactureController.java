package controllers;
import javafx.scene.control.TableCell;
import javafx.util.Callback;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import pdf.FacturePdf;
import javax.swing.text.Document;

import DAO.ConnectionSingleton;
import crud.GestionFactureModel;
import crud.ListeFactureModel;
import entities.Facture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.IOException;
public class ListefactureController {

    @FXML
    private TableColumn<Facture, String> PT;

    @FXML
    private TableColumn<Facture, String> PU;

    @FXML
    private TextField Zrecherche;

    @FXML
    private Button btnImprimerFacture;

    @FXML
    private Button btnListeFactures;

    @FXML
    private Button btnModifierFactures;

    @FXML
    private Button btnSignout;

    @FXML
    private TableColumn<Facture, Integer> codeclient;

    @FXML
    private TableColumn<Facture, Date> date;

    @FXML
    private TableColumn<Facture, String> des;

    @FXML
    private TableView<Facture> factureTableView;

    @FXML
    private TableColumn<Facture, String> fodec;

    @FXML
    private TableColumn<Facture, Integer> id;

    @FXML
    private TableColumn<Facture, String> netAPayer;

    @FXML
    private TableColumn<Facture, String> exportPdfColumn;

    @FXML
    private TableColumn<Facture, Integer> quantite;

    @FXML
    private Button searchButton;

    @FXML
    private TableColumn<Facture, Float> tolale;

    @FXML
    private TableColumn<Facture, Float> totalTTC;

    @FXML
    private TableColumn<Facture, Float> tva;
    @FXML
    private  ListeFactureModel listefactureModel;
    @FXML
    private Button open_word_button;

  
    @FXML
    private void initialize() {
        // Définir le titre de la page
        // titleLabel.setText("Gestion des Factures");

        // Créer une instance du modèle ListeFactureModel
        listefactureModel = new ListeFactureModel();

        // Configurer les colonnes de la TableView
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        codeclient.setCellValueFactory(new PropertyValueFactory<>("CodeClient"));
        quantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        des.setCellValueFactory(new PropertyValueFactory<>("designation"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        PU.setCellValueFactory(new PropertyValueFactory<>("PU"));
        PT.setCellValueFactory(new PropertyValueFactory<>("Pt"));
        tolale.setCellValueFactory(new PropertyValueFactory<>("Total"));
        fodec.setCellValueFactory(new PropertyValueFactory<>("Fodec"));
        tva.setCellValueFactory(new PropertyValueFactory<>("TVA"));
        totalTTC.setCellValueFactory(new PropertyValueFactory<>("TotalTTC"));
        netAPayer.setCellValueFactory(new PropertyValueFactory<>("NetAPayer"));

        
    
        // Charger et afficher les factures depuis la base de données
        chargerEtAfficherFactures();
    }


    private void chargerEtAfficherFactures() {
        // Appeler la méthode getAllFactures() du modèle pour obtenir la liste des factures
        List<Facture> facturesList = listefactureModel.getAllFactures();

        // Convertir la liste en ObservableList pour l'affichage dans la TableView
        ObservableList<Facture> facturesObservableList = FXCollections.observableArrayList(facturesList);

        // Afficher les factures dans la TableView
        factureTableView.setItems(facturesObservableList);
    }

    

    @FXML
    void btnSignout(ActionEvent event) {

    }

  
    @FXML
    void onClickSearch(ActionEvent event) {

    }

    @FXML
    void versImprimerFacture(ActionEvent event) {

    }

    @FXML
    void versListeFacture(ActionEvent event) {
    	 try {
             Parent root = FXMLLoader.load(getClass().getResource("/vue/listeFacture.fxml"));
             Zrecherche.getScene().setRoot(root);
         } catch (IOException e) {
             System.err.println(e.getMessage());
         }
    }
    @FXML
    void versmodifierFacture(ActionEvent event) {
   	 try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/facture.fxml"));
            Zrecherche.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
   	 
   }
    
   /* public void openWordFile() {
        try {
            File file = new File("C:\\Users\\LENOVO\\Desktop\\facture_word_12.pdf");
            if (!file.exists()) {
                throw new IOException("Le fichier spécifié n'existe pas.");
            }
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible d'ouvrir le fichier Word : " + e.getMessage());
            alert.showAndWait();
            e.printStackTrace();
        }*/
   @FXML
   void generateSelectedRowPDF(ActionEvent event) {
       Facture selectedFacture = factureTableView.getSelectionModel().getSelectedItem();

       if (selectedFacture != null) {
           try {
               File fichier = new File("facture_" + selectedFacture.getId() + ".pdf");
               FacturePdf.generate(selectedFacture, fichier);

               Alert alert = new Alert(Alert.AlertType.INFORMATION);
               alert.setTitle("Succès");
               alert.setHeaderText(null);
               alert.setContentText("PDF créé avec succès : " + fichier.getAbsolutePath());
               alert.showAndWait();

               Desktop.getDesktop().open(fichier);   // ouvre le PDF automatiquement
           } catch (IOException e) {
               e.printStackTrace();
           }
       } else {
           Alert alert = new Alert(Alert.AlertType.ERROR);
           alert.setTitle("Erreur");
           alert.setHeaderText(null);
           alert.setContentText("Aucune ligne sélectionnée.");
           alert.showAndWait();
       }
   }

   private void drawRow(PDPageContentStream contentStream, float y, float tableWidth, float rowHeight, float cellMargin, String[] row) throws IOException {
        float x = 50;
        float nextX = x;
        for (int i = 0; i < row.length; i++) {
            String text = row[i];
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 10);
            contentStream.newLineAtOffset(nextX, y);
            contentStream.showText(text);
            contentStream.endText();
            nextX += tableWidth / row.length;
        }
    }

    // Méthode pour dessiner le tableau
    @SuppressWarnings("deprecation")
    private void drawTable(PDPageContentStream contentStream, float y, float tableWidth, float rowHeight, float cellMargin, String[] headers, float[] columnWidths) throws IOException {
        float nextY = y;
        for (String header : headers) {
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
            contentStream.newLineAtOffset(50, nextY);
            contentStream.showText(header);
            contentStream.endText();
            nextY -= rowHeight;
        }
        contentStream.drawLine(50, y, 50, nextY + rowHeight);
        float nextX = 50;
        for (float columnWidth : columnWidths) {
            contentStream.drawLine(nextX, y, nextX + columnWidth, y);
            nextX += columnWidth;
        }
        contentStream.drawLine(50, nextY + rowHeight, 50, y);
        contentStream.drawLine(nextX, nextY + rowHeight, nextX, y);
    }

    
    @FXML
    void generatePDFButtonClicked(ActionEvent event) {
    	generateSelectedRowPDF(event);
    }
    @FXML
    private void openPDF() {
        try {
            File file = new File("C:\\Users\\LENOVO\\Desktop\\java-workspace\\bedafacturation\\bin\\facture.pdf");
            if (!file.exists()) {
                throw new IOException("Le fichier spécifié n'existe pas.");
            }
            Desktop.getDesktop().open(file);
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer les erreurs d'ouverture du fichier PDF
        }
    }
}
    

    
