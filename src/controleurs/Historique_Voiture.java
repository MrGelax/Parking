/**
 * Sample Skeleton for 'VueHistoV.fxml' Controller Class
 */

package controleurs;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import dao.IHistoriqueDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Label;
import modele.Historique;
import service.Facade;

public class Historique_Voiture {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="lblVoiture"
    private Label lblVoiture; // Value injected by FXMLLoader

    @FXML // fx:id="table"
    private TableView<Historique> table; // Value injected by FXMLLoader

    @FXML // fx:id="colPlace"
    private TableColumn<Historique,String> colPlace; // Value injected by FXMLLoader

    @FXML // fx:id="colMomentA"
    private TableColumn<Historique,String> colMomentA; // Value injected by FXMLLoader

    @FXML // fx:id="colMomentS"
    private TableColumn<Historique,String> colMomentS; // Value injected by FXMLLoader
    
    @FXML // fx:id="colDuree"
    private TableColumn<Historique,Integer> colDuree; // Value injected by FXMLLoader
    
    Facade facade;
    ObservableList<Historique> histoList;
	private IHistoriqueDAO histo;
	
	public void setUP(Facade facade,String str) {
		lblVoiture.setText(str);
		this.facade = facade;
		this.histo=facade.getHistoriqueDAO();
		getListHisto(str);
	}
	public void getListHisto(String str){
		//Il ne fait pas histo.getListe
		List<Historique> h = histo.getListe(str);
		histoList =FXCollections.observableList(h);
		table.setItems(histoList);
	}
    private void showErreur(String message) {
		Alert a = new Alert(AlertType.ERROR, message);
		a.showAndWait();
	}
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert lblVoiture != null : "fx:id=\"lblVoiture\" was not injected: check your FXML file 'VueHistoV.fxml'.";
        assert table != null : "fx:id=\"table\" was not injected: check your FXML file 'VueHistoV.fxml'.";
        assert colPlace != null : "fx:id=\"colPlace\" was not injected: check your FXML file 'VueHistoV.fxml'.";
        assert colMomentA != null : "fx:id=\"colMomentA\" was not injected: check your FXML file 'VueHistoV.fxml'.";
        assert colMomentS != null : "fx:id=\"colMomentS\" was not injected: check your FXML file 'VueHistoV.fxml'.";
        assert colDuree != null : "fx:id=\"colDuree\" was not injected: check your FXML file 'VueHistoV.fxml'.";
        colPlace.setCellValueFactory(new PropertyValueFactory<Historique, String>("FKPlace"));
		colMomentA.setCellValueFactory(new PropertyValueFactory<Historique, String>("momentA"));
		colMomentS.setCellValueFactory(new PropertyValueFactory<Historique, String>("momentS"));
		colDuree.setCellValueFactory(new PropertyValueFactory<Historique,Integer>("duree"));
    }
}
