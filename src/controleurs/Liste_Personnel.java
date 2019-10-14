/**
 * Sample Skeleton for 'VueListePerso.fxml' Controller Class
 */

package controleurs;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.ConstraintException;
import dao.IStationnementDAO;
import dao.IVoitureDAO;
import dao.PKExecption;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modele.Personnel;
import modele.Place;
import modele.Stationnement;
import service.Facade;

public class Liste_Personnel implements Observer{

	private Set<String> PersonnelToUpdate = new HashSet<>();
	private static final Logger logger = LoggerFactory.getLogger(Liste_Personnel.class);
	private BooleanProperty changementFait = new SimpleBooleanProperty(false);
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="lblTitre"
    private Label lblTitre; // Value injected by FXMLLoader

    @FXML // fx:id="table"
    private TableView<Personnel> table; // Value injected by FXMLLoader

    @FXML // fx:id="colvoiture"
    private TableColumn<Personnel, String> colvoiture; // Value injected by FXMLLoader

    @FXML // fx:id="colplace"
    private TableColumn<Personnel, String> colplace; // Value injected by FXMLLoader

    @FXML // fx:id="colmomentA"
    private TableColumn<Personnel, String> colmomentA; // Value injected by FXMLLoader

    @FXML // fx:id="btnHisto"
    private Button btnHisto; // Value injected by FXMLLoader

    @FXML // fx:id="btnAddVoiture"
    private Button btnAddVoiture; // Value injected by FXMLLoader

    @FXML // fx:id="btnRecharger"
    private Button btnRecharger; // Value injected by FXMLLoader
    
    private Parking pr;
    Facade facade;
    ObservableList<Personnel> perList;
	private IVoitureDAO personnel;
	
	public void setUP(Facade facade) {
		this.facade = facade;
		this.personnel=facade.getPersonnel();
		getListePerso();
		// S'enregistre sur la facade pour écouter l'ajout ou suppression de serveur
		//facade.addPropertyChangeListener(this);
	}
	public void getListePerso(){
		List<Personnel> per = personnel.getListe("");
		perList =FXCollections.observableList(per);
		table.setItems(perList);
		PersonnelToUpdate.clear();
	}
	public void setPr(Parking p) {
		this.pr=p;
	}
	@FXML
	void actRecharger() {
		getListePerso();
	}
	private void showErreur(String message) {
		Alert a = new Alert(AlertType.ERROR, message);
		a.showAndWait();
	}
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert lblTitre != null : "fx:id=\"lblTitre\" was not injected: check your FXML file 'VueListePerso.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'VueListePerso.fxml'.";
        assert colvoiture != null : "fx:id=\"colvoiture\" was not injected: check your FXML file 'VueListePerso.fxml'.";
        assert colplace != null : "fx:id=\"colplace\" was not injected: check your FXML file 'VueListePerso.fxml'.";
        assert colmomentA != null : "fx:id=\"colmomentA\" was not injected: check your FXML file 'VueListePerso.fxml'.";
        assert btnHisto != null : "fx:id=\"btnHisto\" was not injected: check your FXML file 'VueListePerso.fxml'.";
        assert btnAddVoiture != null : "fx:id=\"btnAddVoiture\" was not injected: check your FXML file 'VueListePerso.fxml'.";
        assert btnRecharger != null : "fx:id=\"btnRecharger\" was not injected: check your FXML file 'VueListePerso.fxml'.";
        
        colvoiture.setCellValueFactory(new PropertyValueFactory<Personnel, String>("immatr"));
		colplace.setCellValueFactory(new PropertyValueFactory<Personnel, String>("nom"));
		colmomentA.setCellValueFactory(new PropertyValueFactory<Personnel, String>("prenom"));
		
		colmomentA.setCellFactory(TextFieldTableCell.forTableColumn());// TextField
		colmomentA.setEditable(true);
		
		table.setEditable(true);
		
		btnAddVoiture.setOnAction(a ->{
			pr.getVueAjout_Perso(pr.getPr()).show();
		});
		btnRecharger.setOnAction(a ->{
			actRecharger();
		});
		btnHisto.setOnAction(a->{
			if(table.getSelectionModel().getSelectedItem()==null)
				showErreur("Il faut selectionner une voiture");
			else
				pr.getVueHistorique(pr.getPr(),(String)table.getSelectionModel().getSelectedItem().getImmatr()).show();
			
		});
		colmomentA.setOnEditCommit(e->{
			
		});
    }
    /**
	 * Appelé lorsqu'il y a du changement sur les serveurs
	 */
	@Override
	public void update(Observable arg0, Object arg1) {
		logger.info("refresh Serveur Observer");
		getListePerso();

	}
}
