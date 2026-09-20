package controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import DAO.ConnectionSingleton;
import crud.GestionFactureModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import entities.Facture;

public class ModifierFactureController {

    @FXML
    private Button Ajouterbtn1;

    @FXML
    private TableColumn<Facture, Float> PT;

    @FXML
    private TableColumn<Facture, Float> PU;

    @FXML
    private TextField ZFODEC;

    @FXML
    private TextField ZTOTAL;

    @FXML
    private TextField ZTTC;

    @FXML
    private TextField ZTVA;

    @FXML
    private TextField Zcodeclient;

    @FXML
    private TextField Zdesignation;

    @FXML
    private TextField Znetpayer;

    @FXML
    private TextField Zpt;

    @FXML
    private TextField Zpu;

    @FXML
    private TextField Zrecherche;

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
    private Spinner<Integer> filtrerzone;

    @FXML
    private TableColumn<Facture, Float> fodec;

    @FXML
    private TableColumn<Facture, Integer> id;

    @FXML
    private Button modifierbtn;

    @FXML
    private TableColumn<Facture, Float> netAPayer;

    @FXML
    private TableColumn<Facture, Float> netpayer1;

    @FXML
    private TableColumn<Facture, Integer> quantite;

    @FXML
    private Spinner<Integer> quantityspinner;

    @FXML
    private Button searchButton;
    

    @FXML
    private Button supprimerbtn;
 

    @FXML
    private Button calcultolatlbtn;

    @FXML
    private TableColumn<Facture, Float> tolale;

    @FXML
    private TableColumn<Facture, Float> totalTTC;

    @FXML
    private TableColumn<Facture, Float> tva;
    
    @FXML
    private TableView<Facture> factureTableView;
    
    private GestionFactureModel gestionFactureModel;

    @FXML
    private void initialize() {
        // Définir le titre de la page
      // titleLabel.setText("Gestion des Factures");

        // Créer une instance du modèle GestionFactureModel
        gestionFactureModel = new GestionFactureModel();

        // Créer la SpinnerValueFactory pour le spinner de quantité
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 0);
        quantityspinner.setValueFactory(valueFactory);

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
        List<Facture> facturesList = gestionFactureModel.getAllFactures();

        // Convertir la liste en ObservableList pour l'affichage dans la TableView
        ObservableList<Facture> facturesObservableList = FXCollections.observableArrayList(facturesList);

        // Afficher les factures dans la TableView
        factureTableView.setItems(facturesObservableList);
    }

   

    @FXML
    void charger(MouseEvent event) {
        Facture facture = factureTableView.getSelectionModel().getSelectedItem();
        if (facture != null) {
            Zcodeclient.setText(String.valueOf(facture.getCodeClient()));
            Zdesignation.setText(facture.getDesignation());
            quantityspinner.getValueFactory().setValue(facture.getQuantite());
            Zpu.setText(String.valueOf(facture.getPU()));
            Zpt.setText(String.valueOf(facture.getPt()));
            ZTOTAL.setText(String.valueOf(facture.getTotal()));
            ZFODEC.setText(String.valueOf(facture.getFodec()));
            ZTVA.setText(String.valueOf(facture.getTVA()));
            ZTTC.setText(String.valueOf(facture.getTotalTTC()));
            Znetpayer.setText(String.valueOf(facture.getNetAPayer()));

        }
    }

   

    @FXML
    void onClickmodifier(ActionEvent event) {
        Facture factureSelectionnee = factureTableView.getSelectionModel().getSelectedItem();

        if (factureSelectionnee != null) {
            Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationDialog.setTitle("Confirmation de mise à jour");
            confirmationDialog.setHeaderText(null);
            confirmationDialog.setContentText("Voulez-vous mettre à jour cette facture ?");

            Optional<ButtonType> result = confirmationDialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                int newCodeClient;
                String newDesignation;
                int newQuantite;
                float newPU;
               // float newFodec;
                //float newTVA;
                try {
                    newCodeClient = Integer.parseInt(Zcodeclient.getText());
                    newDesignation = Zdesignation.getText();
                    newQuantite = quantityspinner.getValue();
                    newPU = Float.parseFloat(Zpu.getText());
                    //newFodec = Float.parseFloat(ZFODEC.getText());
                    //newTVA = Float.parseFloat(ZTVA.getText());
                } catch (NumberFormatException e) {
                    // Handle the case where parsing fails due to empty or invalid input
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Veuillez remplir tous les champs avec des valeurs valides.");
                    errorAlert.showAndWait();
                    return;
                }
                float newPT = newQuantite * newPU;
                float newTotal = newPT;
                float newFodec = newTotal * 0.01f;
                float newTVA = (newTotal + newFodec) * 0.19f;
                float newTotalTTC =  newFodec + newTVA +newTotal;
                float newNetAPayer = newTotalTTC+1;
                
             // Affichage des résultats dans les zones spécifiques
                Zpt.setText(String.valueOf(newPT));
                ZFODEC.setText(String.valueOf(newFodec));
                ZTVA.setText(String.valueOf(newTVA));
                ZTOTAL.setText(String.valueOf(newTotal));
                Znetpayer.setText(String.valueOf(newNetAPayer));
                ZTTC.setText(String.valueOf(newTotalTTC));

                if (newDesignation.isEmpty()) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Erreur");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Veuillez remplir tous les champs.");
                    errorAlert.showAndWait();
                    return;
                }

                factureSelectionnee.setCodeClient(newCodeClient);
                factureSelectionnee.setDesignation(newDesignation);
                factureSelectionnee.setQuantite(newQuantite);
                factureSelectionnee.setPU(newPU);
                factureSelectionnee.setPt(newPT);
                factureSelectionnee.setTotal(newTotal);
                factureSelectionnee.setFodec(newFodec);
                factureSelectionnee.setTVA(newTVA);
                factureSelectionnee.setTotalTTC(newTotalTTC);
                factureSelectionnee.setNetaPayer(newNetAPayer);

                int rowsAffected = gestionFactureModel.modifierFacture(
                    ConnectionSingleton.getConnex(),
                    factureSelectionnee
                );

                if (rowsAffected > 0) {
                    // Refresh the TableView to reflect the changes
                    chargerEtAfficherFactures();
                    confirmationDialog.setContentText("Facture mise à jour avec succès.");

                     //System.out.println("Facture mise à jour avec succès.");
                } else {
                    // Handle the case where the update failed
                    confirmationDialog.setContentText("Échec de la mise à jour de la facture.");

                	System.out.println("Échec de la mise à jour de la facture.");
                }
            } else {
                // Show a cancellation message
                confirmationDialog.setContentText("Mise à jour annulée.");
                System.out.println("Mise à jour annulée.");
            }
        } else {
        	 Alert errorAlert = new Alert(Alert.AlertType.ERROR);
             errorAlert.setTitle("Erreur");
             errorAlert.setHeaderText(null);
             errorAlert.setContentText("Veuillez Selectionner la facture a modifier ");
             errorAlert.showAndWait();
        }
    }

    @FXML
    private void onClickAjouter() {
        // Récupérer les valeurs saisies dans les champs de texte
        int newCodeClient;
        String newDesignation;
        int newQuantite;
        float newPU;
        float newFodec;
        float newTVA;

        try {
            newCodeClient = Integer.parseInt(Zcodeclient.getText());
            newDesignation = Zdesignation.getText();
            newQuantite = quantityspinner.getValue();
            newPU = Float.parseFloat(Zpu.getText());
        } catch (NumberFormatException e) {
            // Afficher un message d'erreur si la saisie est incorrecte
            Alert erreurDialog = new Alert(Alert.AlertType.ERROR);
            erreurDialog.setTitle("Erreur");
            erreurDialog.setHeaderText(null);
            erreurDialog.setContentText("Veuillez remplir tous les champs avec des valeurs valides.");
            erreurDialog.showAndWait();
            return;
        }

        // Vérifier si tous les champs sont remplis
        if (newDesignation.isEmpty()) {
            // Afficher un message d'erreur si un champ est vide
            Alert erreurDialog = new Alert(Alert.AlertType.ERROR);
            erreurDialog.setTitle("Erreur");
            erreurDialog.setHeaderText(null);
            erreurDialog.setContentText("Veuillez remplir tous les champs.");
            erreurDialog.showAndWait();
            return;
        }

        float newPT = newQuantite * newPU;
        float newTotal = newPT;
        
        newFodec = newPT * 0.01f; 
        
        newTVA = (newPT + newFodec) * 0.19f; 
        
        float newTotalTTC = newPT + newFodec + newTVA;
        
        float newNetAPayer = newTotalTTC + 1; 
        

        // Appeler la méthode pour ajouter la nouvelle facture à la base de données
        int rowsAffected = gestionFactureModel.ajouterFacture(ConnectionSingleton.getConnex(), newCodeClient ,  newQuantite, newDesignation , new Date(System.currentTimeMillis()), newPU, newPT, newTotal, newFodec, newTVA, newTotalTTC, newNetAPayer);
        // Vérifier si l'ajout a réussi
        if (rowsAffected > 0) {
            // Rafraîchir la TableView pour refléter les changements
            chargerEtAfficherFactures();

            // Afficher un message de succès
            Alert succesDialog = new Alert(Alert.AlertType.INFORMATION);
            succesDialog.setTitle("Succès");
            succesDialog.setHeaderText(null);
            succesDialog.setContentText("Facture ajoutée avec succès.");
            succesDialog.showAndWait();

            // Effacer les champs de texte après l'ajout
            Zcodeclient.clear();
            Zdesignation.clear();
            quantityspinner.getValueFactory().setValue(0);
            Zpu.clear();
            Zpt.clear();
            ZTOTAL.clear();
            ZFODEC.clear();
            ZTVA.clear();
            ZTTC.clear();
            Znetpayer.clear();
        } else {
            // Afficher un message d'erreur en cas d'échec de l'ajout
            Alert erreurDialog = new Alert(Alert.AlertType.ERROR);
            erreurDialog.setTitle("Erreur");
            erreurDialog.setHeaderText(null);
            erreurDialog.setContentText("Erreur lors de l'ajout de la facture. Veuillez réessayer.");
            erreurDialog.showAndWait();
        }
    }


    @FXML
    void onClickSearch(ActionEvent event) {
        // Ajoutez le code ici pour gérer l'événement du bouton Search
    }

 

    @FXML
    public void onsupprimer() {
    	Facture factureSelectionnee = factureTableView.getSelectionModel().getSelectedItem();

    	// Vérifier si une facture est sélectionnée
    	if (factureSelectionnee != null) {
    	    // Afficher une boîte de dialogue de confirmation
    	    Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
    	    confirmationDialog.setTitle("Confirmation de suppression");
    	    confirmationDialog.setHeaderText(null);
    	    confirmationDialog.setContentText("Voulez-vous supprimer cette facture ?");

    	    Optional<ButtonType> result = confirmationDialog.showAndWait();

    	    // Vérifier si l'utilisateur a cliqué sur OK
    	    if (result.isPresent() && result.get() == ButtonType.OK) {
    	        // Récupérer l'ID de la facture sélectionnée
    	        int idFacture = factureSelectionnee.getId();

    	        // Appeler la méthode pour supprimer la facture avec cet ID
    	        gestionFactureModel.supprimerFacture(idFacture);

    	        // Recharger et afficher le tableau des factures après la suppression
    	        chargerEtAfficherFactures();
    	        // Effacer les champs de texte après l'ajout
                Zcodeclient.clear();
                Zdesignation.clear();
                quantityspinner.getValueFactory().setValue(0);
                Zpu.clear();
                Zpt.clear();
                ZTOTAL.clear();
                ZFODEC.clear();
                ZTVA.clear();
                ZTTC.clear();
                Znetpayer.clear();
    	        
    	        
    	    }
    	} else {
    	    // Afficher un message d'erreur si aucune facture n'est sélectionnée
    	    System.out.println("Veuillez sélectionner une facture à supprimer.");
    	}}
  

   
    
    @FXML
    void onClickClear(ActionEvent event) {
    	Zcodeclient.setText(null);
    	Zdesignation.setText(null);
    	Zpu.setText(null);
    	Zpt.setText(null);
    	ZTOTAL.setText(null);
    	ZFODEC.setText(null);
    	ZTVA.setText(null);
    	ZTTC.setText(null);
    	Znetpayer.setText(null);
    	quantityspinner.getValueFactory().setValue(0);
    }

    @FXML
    void versListeFacture(ActionEvent event) {
    	 try {
             Parent root = FXMLLoader.load(getClass().getResource("/vue/listeFacture.fxml"));
             Zpu.getScene().setRoot(root);
         } catch (IOException e) {
             System.err.println(e.getMessage());
         }
    }

    @FXML
    void versImprimerFacture(ActionEvent event) {
    	 try {
             Parent root = FXMLLoader.load(getClass().getResource("/vue/ajoutfacture.fxml"));
             Zpu.getScene().setRoot(root);
             
         } catch (IOException e) {
             System.err.println(e.getMessage());
         }
    }

    @FXML
    void versmodifierFacture(ActionEvent event) {
    	 try {
             Parent root = FXMLLoader.load(getClass().getResource("/vue/facture.fxml"));
             Zpu.getScene().setRoot(root);
         } catch (IOException e) {
             System.err.println(e.getMessage());
         }
    }
    
    @FXML
    void btnSignout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/vue/Login.fxml"));
            Zpu.getScene().setRoot(root);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
 // Déclarer totalPT en tant que variable membre de la classe du contrôleur
    private float totalPT = 0.0f;

    @FXML
    void onClickcalcultotal(ActionEvent event) {
        float newPT = totalPT; // Utiliser la valeur de pt calculée précédemment
        float newFodec = newPT * 0.01f;
        float newTVA = (newPT + newFodec) * 0.19f;
        float newTotalTTC = newPT + newFodec + newTVA;
        float newNetAPayer = newTotalTTC + 1; // Ajout de 1 pour une raison quelconque, à adapter selon votre logique

        // Affichage des résultats dans les zones spécifiques
        Zpt.setText(String.valueOf(newPT));
        ZFODEC.setText(String.valueOf(newFodec));
        ZTVA.setText(String.valueOf(newTVA));
        ZTOTAL.setText(String.valueOf(newPT)); // Utiliser newPT pour le total
        Znetpayer.setText(String.valueOf(newNetAPayer));
        ZTTC.setText(String.valueOf(newTotalTTC));
    }

    @FXML
    void onClickcalcultotal1(ActionEvent event) {
        int newCodeClient;
        String newDesignation;
        int newQuantite;
        float newPU;

        // Vérification des champs vides
        if (Zcodeclient.getText().isEmpty() || Zdesignation.getText().isEmpty() || Zpu.getText().isEmpty()) {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Veuillez remplir tous les champs avec des valeurs valides.");
            errorAlert.showAndWait();
            return;
        }

        try {
            newCodeClient = Integer.parseInt(Zcodeclient.getText());
            newDesignation = Zdesignation.getText();
            newQuantite = quantityspinner.getValue();
            newPU = Float.parseFloat(Zpu.getText());
        } catch (NumberFormatException e) {
            // Gérer le cas où la conversion échoue en raison d'une entrée invalide
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Erreur");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Veuillez saisir des valeurs numériques valides pour le code client, la quantité et le prix unitaire.");
            errorAlert.showAndWait();
            return;
        }

        float newPT = newQuantite * newPU;

        // Ajouter le nouveau prix total au dernier prix total
        totalPT += newPT; // Utiliser l'opérateur d'addition composée pour ajouter au total précédent

        // Afficher le nouveau prix total dans la zone spécifique
        Zpt.setText(String.valueOf(totalPT));

        // Réinitialiser les zones de texte pour qu'elles acceptent de nouvelles valeurs
        Zdesignation.clear();
        quantityspinner.getValueFactory().setValue(0);
        Zpu.clear();
    }

}
