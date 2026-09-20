package controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import crud.LoginModel;

public class LoginController {

    @FXML
    private Button BtnAnnuler;

    @FXML
    private Button BtnConnecter;

    @FXML
    private PasswordField ZMdp;

    @FXML
    private TextField Zlogin;

    private LoginModel model;

    public 	LoginController() {
        model = new LoginModel();
    }

    @FXML
    void initialize() {
    	BtnConnecter.setOnAction(this::handleConnexion);
    	BtnAnnuler.setOnAction(this::handleInscription);
    }

    private void handleConnexion(ActionEvent event) {
        String username = Zlogin.getText(); // Récupérer le nom d'utilisateur à partir du champ de texte z1
        String password = ZMdp.getText(); // Récupérer le mot de passe à partir du champ de texte z2

        if (model.authenticate(username, password)) {
            // Si l'authentification réussit, rediriger vers la fenêtre gestionClient.fxml
        	try {
        	    FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/facture.fxml"));
        	    Parent root = loader.load();
        	    Stage stage = new Stage();
        	    stage.setScene(new Scene(root));
        	    stage.show();

        	    // Fermer la fenêtre d'authentification
        	    Stage authStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        	    authStage.close();
        	} catch (Exception e) {
        	    e.printStackTrace();
        	}
        } else {
            // Sinon, afficher une boîte de dialogue d'alerte indiquant un problème d'authentification
            showAlert("Votre nom d'utilisateur ou mot de passe est incorrect.");
        }
    }

    private void handleInscription(ActionEvent event) {
        try {
        	 // Fermer la fenêtre d'authentification
    	    Stage authStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    	    authStage.close();
    	    
            // Charger le fichier FXML de la page d'inscription
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/inscreptionVue.fxml"));
            Parent root = loader.load();

            // Créer une nouvelle scène
            Scene scene = new Scene(root);

            // Obtenir la scène actuelle à partir de l'événement
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Afficher la nouvelle scène
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur d'authentification");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


