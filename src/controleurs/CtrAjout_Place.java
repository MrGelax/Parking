/**
 * Sample Skeleton for 'VueAjouterPlace.fxml' Controller Class
 */

package controleurs;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.*;

import com.sun.media.jfxmedia.events.NewFrameEvent;

import dao.ConstraintException;
import dao.PKExecption;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import modele.Place;
import service.Facade;

public class CtrAjout_Place {
	
	private Place place; 
	
	private Set<TextField> errorsField = new HashSet<>();
	
	private service.Facade facade;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txfCode"
    private TextField txfCode; // Value injected by FXMLLoader

    @FXML // fx:id="txfSize"
    private TextField txfSize; // Value injected by FXMLLoader

    @FXML // fx:id="txfLibre"
    private TextField txfLibre; // Value injected by FXMLLoader

    @FXML // fx:id="lblTitre"
    private Label lblTitre; // Value injected by FXMLLoader

    @FXML // fx:id="btnCancel"
    private Button btnCancel; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdd"
    private Button btnAdd; // Value injected by FXMLLoader

    @FXML // fx:id="txtErreur"
    private TextArea txtErreur; // Value injected by FXMLLoader
    
    void actAjout() {
    	errorsField.forEach(tf -> {
			tf.getStyleClass().remove("error");
		});
		errorsField.clear();
		try{
			String erreur=null;
			if(txfCode.getText().isEmpty()||Pattern.matches("^P\\d{2,3}",txfCode.getText().trim())==false)
				erreur="- Vous devez entrer un code de place ex : PXX";
			if(txfSize.getText().isEmpty()||Integer.parseInt(txfSize.getText())<1||Integer.parseInt(txfSize.getText())>20)
				erreur+="\n- Vous devez entrer une taille entre 1 et 20";
			if(txfLibre.getText().isEmpty()||(!txfLibre.getText().trim().equals("true")&&!txfLibre.getText().trim().equals("false")))
				erreur+="\n- Vous devez entrer 'true' ou 'false'";
			if(erreur!=null) {
				showErreur(erreur);
			}else {
				if(Integer.parseInt(txfSize.getText())<1||Integer.parseInt(txfSize.getText())>20)
					showErreur("Erreur : La taille doit être comprise entre 1 et 20");
				else {
					// Création d'une place
					place = new Place(txfCode.getText(),Integer.parseInt(txfSize.getText()),Boolean.parseBoolean(txfLibre.getText()));
					// Insert dans BD
					String code = facade.getPlaceDAO().insert(place);
					// Si tout test OK on ferme la fenêtre
					Stage stage = (Stage) btnAdd.getScene().getWindow();
					stage.close();
				}
			}
		} catch (PKExecption e) {
			//recherche le code i18n et affiche dans le TexteArea
			showErreur("La place "+txfCode.getText()+" existe déjà");
		} catch (ConstraintException e) {
			//recherche le code i18n et affiche dans le TexteArea
			txtErreur.setText(this.resources.getString("ConstrainteErreur") + e.getChamp());
			//Mémorise la Zone de Texte en erreur
			switch (e.getChamp()) {
			case "CODE_PLA":
				errorsField.add(txfCode);
				break;
			case "TAILLE":
				errorsField.add(txfSize);
				break;
			case "LIBRE":
				errorsField.add(txfLibre);
			default:
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
			txtErreur.setText("Erreur: " + e.getMessage());
		}
		//Change la classe CSS pour les Zones de Texte en erreur
		// En cas d'erreur Applique la classe css error
		errorsField.forEach(tf -> {
			tf.getStyleClass().add("error");
		});
    }
    @FXML
	void actAnnuler() {
		Stage stage = (Stage) btnAdd.getScene().getWindow();
		txfCode.clear();
		txfSize.clear();;
		txfLibre.clear();
		txtErreur.clear();
		stage.close();
	}
    private void showErreur(String message) {
		Alert a = new Alert(AlertType.ERROR, message);
		a.showAndWait();
	}
	public void setUP(Facade facade) {
		this.facade = facade;
	}

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txfCode != null : "fx:id=\"txfCode\" was not injected: check your FXML file 'VueAjouterPlace.fxml'.";
        assert txfSize != null : "fx:id=\"txfSize\" was not injected: check your FXML file 'VueAjouterPlace.fxml'.";
        assert txfLibre != null : "fx:id=\"txfLibre\" was not injected: check your FXML file 'VueAjouterPlace.fxml'.";
        assert lblTitre != null : "fx:id=\"lblTitre\" was not injected: check your FXML file 'VueAjouterPlace.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'VueAjouterPlace.fxml'.";
        assert btnAdd != null : "fx:id=\"btnAdd\" was not injected: check your FXML file 'VueAjouterPlace.fxml'.";
        assert txtErreur != null : "fx:id=\"txtErreur\" was not injected: check your FXML file 'VueAjouterPlace.fxml'.";
        txtErreur.setText("");
		txfCode.setText("");
		txfSize.setText("");
		txfLibre.setText("");
		btnAdd.setOnAction(a -> {
			actAjout();
		});
		
		btnCancel.setOnAction(a -> {
			actAnnuler();;
		});
    }
}
