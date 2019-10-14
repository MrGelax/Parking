/**
 * Sample Skeleton for 'VueAjoutPerso.fxml' Controller Class
 */

package controleurs;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Pattern;

import dao.ConstraintException;
import dao.PKExecption;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import modele.Personnel;
import service.Facade;

public class CtrAjout_Voit {
	
	private Personnel per; 
	private Stage vueList;
	private Set<TextField> errorsField = new HashSet<>();
	private service.Facade facade;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txfImmatr"
    private TextField txfImmatr; // Value injected by FXMLLoader

    @FXML // fx:id="txfNom"
    private TextField txfNom; // Value injected by FXMLLoader

    @FXML // fx:id="tfxPrenom"
    private TextField tfxPrenom; // Value injected by FXMLLoader

    @FXML // fx:id="txtErreur"
    private TextArea txtErreur; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdd"
    private Button btnAdd; // Value injected by FXMLLoader

    @FXML // fx:id="btnCancel"
    private Button btnCancel; // Value injected by FXMLLoader

    void actAjout() {
    	errorsField.forEach(tf -> {
			tf.getStyleClass().remove("error");
		});
		errorsField.clear();
		String erreur=null;
		boolean er=false;
		try {
			if(Pattern.matches("^([a-zA-Z]{3})-[0-9]{3}",txfImmatr.getText().trim())==false)
				erreur="L'immatriculation doit respecter le format 'AAA-111' ou 'aaa-111'";
			/*Personnel personnel=facade.getPersonnel().getFromID(txfImmatr.getText().trim());
			if(personnel!=null)
				erreur+="L'immatriculation existe déjà";*/
			else {
				per = new Personnel(txfImmatr.getText(),txfNom.getText(),tfxPrenom.getText());
				String code = facade.getPersonnel().insert(per);
				// Si tout test OK on ferme la fenêtre
				Stage stage = (Stage) btnAdd.getScene().getWindow();
				stage.close();
			}
		} catch (PKExecption e) {
			showErreur("La place "+txfImmatr.getText()+" existe déjà");
			er=true;
		} catch (ConstraintException e) {
			switch (e.getChamp()) {
			case "NOM":
				erreur+="\nLe champ Nom ne peut être vide";
				er=true;
				break;
			case "PRENOM":
				erreur+="\nLe champ Prenom ne peut être vide";
				er=true;
			default:
				break;
			}
			showErreur(erreur);
		} catch (Exception e) {
			e.printStackTrace();
			txtErreur.setText("Erreur: " + e.getMessage());
		}
		if (!er)showErreur(erreur);
		//Change la classe CSS pour les Zones de Texte en erreur
		// En cas d'erreur Applique la classe css error
		errorsField.forEach(tf -> {
			tf.getStyleClass().add("error");
		});
    }
    @FXML
	void actAnnuler() {
		Stage stage = (Stage) btnAdd.getScene().getWindow();
		stage.close();
	}

	public void setUP(Facade facade,Stage vueList) {
		this.facade = facade;
		this.vueList=vueList;
	}
	private void showErreur(String message) {
		Alert a = new Alert(AlertType.ERROR, message);
		a.showAndWait();
	}
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txfImmatr != null : "fx:id=\"txfImmatr\" was not injected: check your FXML file 'VueAjoutPerso.fxml'.";
        assert txfNom != null : "fx:id=\"txfNom\" was not injected: check your FXML file 'VueAjoutPerso.fxml'.";
        assert tfxPrenom != null : "fx:id=\"tfxPrenom\" was not injected: check your FXML file 'VueAjoutPerso.fxml'.";
        assert txtErreur != null : "fx:id=\"txtErreur\" was not injected: check your FXML file 'VueAjoutPerso.fxml'.";
        assert btnAdd != null : "fx:id=\"btnAdd\" was not injected: check your FXML file 'VueAjoutPerso.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'VueAjoutPerso.fxml'.";
        btnAdd.setOnAction(a -> {
			actAjout();
		});
		btnCancel.setOnAction(a -> {
			actAnnuler();;
		});
    }
}
