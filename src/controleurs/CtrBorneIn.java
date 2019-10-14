/**
 * Sample Skeleton for 'VueBorneIn.fxml' Controller Class
 */

package controleurs;

import java.net.URL;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import dao.ConstraintException;
import dao.IPlaceDAO;
import dao.IVoitureDAO;
import dao.PKExecption;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import modele.Personnel;
import modele.Place;
import modele.Stationnement;
import service.Facade;

public class CtrBorneIn {
	private Stationnement sta=null;
	
	private service.Facade facade;
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnAdd"
    private Button btnAdd; // Value injected by FXMLLoader

    @FXML // fx:id="btnCancel"
    private Button btnCancel; // Value injected by FXMLLoader

    @FXML // fx:id="lblTitre"
    private Label lblTitre; // Value injected by FXMLLoader

    @FXML // fx:id="cbPalce"
    private ComboBox<String> cbPalce; // Value injected by FXMLLoader

    @FXML // fx:id="cbImmatr"
    private ComboBox<String> cbImmatr; // Value injected by FXMLLoader

    @FXML // fx:id="txtErreur"
    private TextArea txtErreur; // Value injected by FXMLLoader

	private Parking pr;
	private IPlaceDAO place;
	private IVoitureDAO voit;
	ObservableList<String> listPlaceDispo;
	ObservableList<String> listVoitureDispo;
	
    void actAjout() {
		//Fonctionne
		if(cbImmatr.getSelectionModel().isEmpty())
			showErreur("Vous devez au moins entrer la plaque");
		else {
			if(cbPalce.getSelectionModel().isEmpty())
				sta=new Stationnement (cbImmatr.getValue(),null,new Timestamp(System.currentTimeMillis()));
			else {
				sta = new Stationnement (cbImmatr.getValue().trim(),cbPalce.getValue().trim(),new Timestamp(System.currentTimeMillis()));
				if(facade.getPlaceDAO().getFromID(sta.getFKPlace()).getLibre()==false)
					showErreur("La place "+cbPalce.getValue()+" n'est pas libre");
			}
			if (sta!=null) {
				String err=null;
				try {
					// Insert dans BD
					String code = facade.getStationnementDAO().insert(sta);
					// Si tout test OK on ferme la fenêtre
					Stage stage = (Stage) btnAdd.getScene().getWindow();
					stage.close();
				} catch (PKExecption e) {
					err+="La voiture se trouve déjà dans le parking";
					showErreur(err);
				} catch (ConstraintException e) {
					//recherche le code i18n et affiche dans le TexteArea
					txtErreur.setText(this.resources.getString("ConstrainteErreur") + e.getChamp());

				} catch (Exception e) {
					e.printStackTrace();
					txtErreur.setText("Erreur: " + e.getMessage());
				}
				this.pr.getStationnements();
				cbImmatr.getSelectionModel().clearSelection();
				cbPalce.getSelectionModel().clearSelection();
				cbImmatr.setValue(null);
				cbPalce.setValue(null);
			}
		}
    }
    public void setPr(Parking p) {
		this.pr=p;
	}
    @FXML
	void actAnnuler() {
		Stage stage = (Stage) btnAdd.getScene().getWindow();
		cbImmatr.getSelectionModel().clearSelection();
		cbPalce.getSelectionModel().clearSelection();
		cbImmatr.setValue(null);
		cbPalce.setValue(null);
		stage.close();
	}
    public void getListPlaceDispo(){
		List<String> pl = place.getListeDispo();
		listPlaceDispo =FXCollections.observableList(pl);
		cbPalce.setItems(listPlaceDispo);
	}
    public void getListVoitDispo(){
		List<String> voitList=voit.getListeDispo();
		listVoitureDispo=FXCollections.observableList(voitList);
		cbImmatr.setItems(listVoitureDispo);
	}
    public void setUP(Facade facade) {
		this.facade = facade;
		this.place=facade.getPlaceDAO();
		this.voit=facade.getPersonnel();
		getListPlaceDispo();
		getListVoitDispo();
	}
	private void showErreur(String message) {
		Alert a = new Alert(AlertType.ERROR, message);
		a.showAndWait();
	}
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnAdd != null : "fx:id=\"btnAdd\" was not injected: check your FXML file 'VueBorneIn.fxml'.";
        assert btnCancel != null : "fx:id=\"btnCancel\" was not injected: check your FXML file 'VueBorneIn.fxml'.";
        assert lblTitre != null : "fx:id=\"lblTitre\" was not injected: check your FXML file 'VueBorneIn.fxml'.";
        assert cbPalce != null : "fx:id=\"cbPalce\" was not injected: check your FXML file 'VueBorneIn.fxml'.";
        assert cbImmatr != null : "fx:id=\"cbImmatr\" was not injected: check your FXML file 'VueBorneIn.fxml'.";
        assert txtErreur != null : "fx:id=\"txtErreur\" was not injected: check your FXML file 'VueBorneIn.fxml'.";
        //cbPlace.setItems(FXCollections.observableArrayList());
        cbPalce.setPromptText("Format : P01");
        cbImmatr.setPromptText("Format : AAA-111");
        cbPalce.setValue(null);
        cbImmatr.setValue(null);
        btnAdd.setOnAction(a->{
        	actAjout();
        });
        btnCancel.setOnAction(a->{
        	actAnnuler();
        });
        
    }
}
