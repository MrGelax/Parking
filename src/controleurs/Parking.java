package controleurs;

import java.awt.Window;
import java.io.IOException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.text.TableView.TableCell;

import dao.IStationnementDAO;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import modele.Stationnement;
import service.Facade;

public class Parking extends Application {

	private final Locale DEFAULT_LOCALE = new Locale("fr", "BE");
	
	// Fenêtre pour ajouter une place
	private Stage pr;
	private Stage vueAjout_Place;
	private Stage vueAjoutPerso;
	// Fenêtre pour garer une voiture
	private Stage vueBorneIn;
	// Fenêtre pour visualiser l'hsorique d'une voiture
	private Stage vueHisto_Voit;
	// Fenêtre pour afficher la liste des personnes
	private Stage vueListePerso;
	// Locale par défaut de l'application
	private Locale locale = DEFAULT_LOCALE;

	private Facade facade;

	// Conteneur principal de la vue principale
	private BorderPane contentPane;

	// nodes de la vue principale
	private Label lbl;
	private Button btnAddPlace;
	private Button btnListePerso;
	private Button btnBorneIn;
	private Button btnBorneOut;
	TableView<Stationnement> tab;
	ObservableList<Stationnement> staList;
	private IStationnementDAO stationnement;
	@Override
	public void start(Stage primaryStage) {
		pr=primaryStage;
		// Création du conteneur principal
		contentPane = new BorderPane();
		//Création de la facade
		facade = new Facade();
		stationnement=facade.getStationnementDAO();
		
		// Titre de l'aplication
		lbl = new Label("  BIENVENUE AU PARKING ISFCE  ");
		lbl.setId("AccueilResto");// id pour CSS
		contentPane.setPadding(new Insets(5, 5, 5, 5));
		// La taille doit être celle du conteneur
		lbl.prefWidthProperty().bind(contentPane.widthProperty());
		lbl.setAlignment(Pos.CENTER);
		contentPane.setTop(lbl);
		
		// Bouton pour afficher la vueAjout_Place
		btnAddPlace = new Button("Ajout Place");
		btnAddPlace.setPrefWidth(100);
		btnAddPlace.setOnAction(a -> {
		if (vueAjout_Place != null)
			vueAjout_Place.show();
		});

		// Bouton pour afficher la VueListePerso
		btnListePerso = new Button("Liste Personnel");
		btnListePerso.setPrefWidth(100);
		btnListePerso.setOnAction(a -> {
			vueListePerso.show();
		});
		// Bouton pour afficher la liste des serveurs
		btnBorneIn = new Button("Borne In");
		btnBorneIn.setPrefWidth(100);
		btnBorneIn.setOnAction(a -> {
			if(facade.getPlaceDAO().getListeDispo().size()==0)
				showErreur("Le Parking est plein veuillez liberer de la place");
			else
				vueBorneIn.show();
		});
		//Buton de pour lancer la sortie du véhicule
		btnBorneOut=new Button("Quiter Parking");
		btnBorneOut.setPrefWidth(100);
		btnBorneOut.setOnAction(a ->{
			if(tab.getSelectionModel().getSelectedItem()==null)
				showErreur("Il faut selectionner une voiture stationner pour pouvoir sortir");
			else {
				Stationnement s=tab.getSelectionModel().getSelectedItem();
				String pla=s.getFKPlace();
				long min=0;
				try {
					min=facade.getStationnementDAO().deleteInt(s);
					Alert alrtSortie = new Alert(AlertType.CONFIRMATION,"Place :"+pla+"\nDurée Stationnement :"+min +"minutes");
					alrtSortie.showAndWait();
				} catch (Exception e) {
					showErreur(e.getMessage());
				}
				getStationnements();
			}
		});
		
		// Création des cols de la tab
		TableColumn<Stationnement,String> colfkPersonneCol=new TableColumn<Stationnement,String>("Immatr");
		colfkPersonneCol.setMinWidth(150);
		colfkPersonneCol.setCellValueFactory(new PropertyValueFactory<Stationnement,String>("FKPersonne"));
		TableColumn<Stationnement,String> colfkPlaceCol=new TableColumn<Stationnement,String>("Place");
		colfkPlaceCol.setMinWidth(150);
		colfkPlaceCol.setCellValueFactory(new PropertyValueFactory<Stationnement,String>("FKPlace"));
		TableColumn<Stationnement,String> colMomentACol=new TableColumn<Stationnement,String>("Moment Arrivé");
		colMomentACol.setMinWidth(150);
		colMomentACol.setCellValueFactory(new PropertyValueFactory<Stationnement,String>("momentA"));
		tab=new TableView<>();
		tab.getColumns().addAll(colfkPersonneCol,colfkPlaceCol,colMomentACol);
		VBox vBox=new VBox();
		vBox.getChildren().addAll(tab);
		getStationnements();
		
		
		// Ajout des 3 boutons au conteneur left
		//Pane pour les boutons
		TilePane lstBoutons = new TilePane();
		lstBoutons.getChildren().addAll(btnAddPlace,btnListePerso,btnBorneIn,btnBorneOut);
		// rajoute le conteneur des boutons à gauche dans le cp
		contentPane.setLeft(lstBoutons);
		contentPane.setCenter(vBox);
		lstBoutons.setStyle("-fx-background-color: azure;");
		// création d'une scène avec son conteneur parent
		Scene scene = new Scene(contentPane, 900, 600);
		scene.getStylesheets().add("/vues/css/resto.css");
		primaryStage.setTitle("PARKING");
		
		
		
		// Ajout de la scène à la Stage
		primaryStage.setScene(scene);
		
		
		vueAjout_Place=getVueAjout_Place(primaryStage);
		vueListePerso=getVueListePerso(primaryStage);
		vueAjoutPerso=getVueAjout_Place(primaryStage);
		vueBorneIn=getVueBorneIn(primaryStage);
		//vueHisto_Voit=getVueHistorique(primaryStage);
		// Affichage de la vue principale
		primaryStage.show();
	}
	public Stage getPr() {
		return this.pr;
	}
	
	//Fonctionne
	public Stage getVueAjout_Place(Stage primaryStage) {
		/*if(vueAjout_Place!=null)
			return vueAjout_Place;*/
		ResourceBundle bundle;
		// Création d'une nouvelle fenêtre
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.initModality(Modality.NONE);
		stage.initOwner(primaryStage);
		// Position lors de l'ouverture;
		stage.setX(primaryStage.getX() + 100);
		stage.setY(primaryStage.getY() + 40);

		// Crée un loader pour charger la vue FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/VueAjouterPlace.fxml"));
		try {
			bundle = ResourceBundle.getBundle("vues.bundles.vuePlace", locale);
			loader.setResources(bundle);
			// Obtenir la traduction du titre dans la locale
			stage.setTitle(bundle.getString("Titre"));
		} catch (Exception e) {
			showErreur(e.getMessage());
			stage.setTitle("Vue Ajout Place");
		}

		// Charge la vue à partir du Loader
		// et initialise son contenu en appelant la méthode setUp du controleur
		Pane root;
		try {
			root = loader.load();
			// récupère le ctrl (après l'initialisation)
			CtrAjout_Place ctrl = loader.getController();
			// fourni la fabrique au ctrl pour charger les articles
			ctrl.setUP(facade);
			// charge le Pane dans la Stage
			stage.setScene(new Scene(root, 400, 400));
		} catch (IOException e) {
			showErreur("Impossible de charger la vueAjout_Place: " + e.getMessage());
			stage = null;
		}

		return stage;
	}
	
	//Fonctionne
	public Stage getVueListePerso(Stage primaryStage) {
		if(vueListePerso!=null)
			return vueListePerso;
		ResourceBundle bundle;
		// Création d'une nouvelle fenêtre
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.initModality(Modality.NONE);
		stage.initOwner(primaryStage);
		// Position lors de l'ouverture;
		stage.setX(primaryStage.getX() + 100);
		stage.setY(primaryStage.getY() + 40);

		// Crée un loader pour charger la vue FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/VueListePerso.fxml"));
		try {
			bundle = ResourceBundle.getBundle("vues.bundles.vueListePerso", locale);
			loader.setResources(bundle);
			// Obtenir la traduction du titre dans la locale
			stage.setTitle(bundle.getString("Titre"));
		} catch (Exception e) {
			showErreur(e.getMessage());
			stage.setTitle("Vue Liste Personnel");
		}

		// Charge la vue à partir du Loader
		// et initialise son contenu en appelant la méthode setUp du controleur
		Pane root;
		try {
			root = loader.load();
			// récupère le ctrl (après l'initialisation)
			Liste_Personnel ctrl = loader.getController();
			ctrl.setPr(this);
			// fourni la fabrique au ctrl pour charger les articles
			ctrl.setUP(facade);
			// charge le Pane dans la Stage
			stage.setScene(new Scene(root, 400, 400));
		} catch (IOException e) {
			showErreur("Impossible de charger la vueListePerso: " + e.getMessage());
			stage = null;
		}
		return stage;
	}

	//Fonctionne
	public Stage getVueAjout_Perso(Stage primaryStage) {
		/*if(vueAjout_Place!=null)
			return vueAjout_Place;*/
		ResourceBundle bundle;
		// Création d'une nouvelle fenêtre
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.initModality(Modality.NONE);
		stage.initOwner(primaryStage);
		// Position lors de l'ouverture;
		stage.setX(primaryStage.getX() + 100);
		stage.setY(primaryStage.getY() + 40);

		// Crée un loader pour charger la vue FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/VueAjoutPerso.fxml"));
		try {
			bundle = ResourceBundle.getBundle("vues.bundles.vuePerso",locale);
			loader.setResources(bundle);
			// Obtenir la traduction du titre dans la locale
			stage.setTitle(bundle.getString("Titre"));
		} catch (Exception e) {
			showErreur(e.getMessage());
			stage.setTitle("Vue Ajout Voiture");
		}

		// Charge la vue à partir du Loader
		// et initialise son contenu en appelant la méthode setUp du controleur
		Pane root;
		try {
			root = loader.load();
			// récupère le ctrl (après l'initialisation)
			CtrAjout_Voit ctrl = loader.getController();
			// fourni la fabrique au ctrl pour charger les articles
			ctrl.setUP(facade,vueListePerso);
			// charge le Pane dans la Stage
			stage.setScene(new Scene(root, 400, 400));
		} catch (IOException e) {
			showErreur("Impossible de charger la vueAjout_Perso " + e.getMessage());
			stage = null;
		}
		return stage;
	}
    /*
     * Affiche l'historique de la voiture
     * manque juste passer en param de la vueListeVoiture la voiture selectionner
     * et afficher l'immatr de la voiture selectionné dans le comboBox de vueHisto.fxml 
    */
	public Stage getVueHistorique(Stage primaryStage,String str) {
		if(vueHisto_Voit!=null)
			return vueHisto_Voit;
			
		ResourceBundle bundle;
		// Création d'une nouvelle fenêtre
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.initModality(Modality.NONE);
		stage.initOwner(primaryStage);
		// Position lors de l'ouverture;
		stage.setX(primaryStage.getX() + 100);
		stage.setY(primaryStage.getY() + 40);

		// Crée un loader pour charger la vue FXML
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/VueHistoV.fxml"));
		try {
			bundle = ResourceBundle.getBundle("vues.bundles.vueHistoVoiture", locale);
			loader.setResources(bundle);
			// Obtenir la traduction du titre dans la locale
			stage.setTitle(bundle.getString("titreHistoVoit"));
		} catch (Exception e) {
			showErreur(e.getMessage());
			stage.setTitle("Historique Voiture N°");
		}

		// Charge la vue à partir du Loader
		// et initialise son contenu en appelant la méthode setUp du controleur
		Pane root;
		try {
			root = loader.load();
			// récupère le ctrl (après l'initialisation)
			Historique_Voiture ctrl = loader.getController();
			// fourni la fabrique au ctrl pour charger les articles
			ctrl.setUP(facade,str);

			// charge le Pane dans la Stage
			stage.setScene(new Scene(root, 400, 400));
		} catch (IOException e) {
			showErreur("Impossible de charger la VueHistoriqueVoiture: " + e.getMessage());
			stage = null;
		}
		return stage;
	}
	
	public Stage getVueBorneIn(Stage primaryStage) {
		/*if(vueAjout_Place!=null)
		return vueAjout_Place;*/
	ResourceBundle bundle;
	// Création d'une nouvelle fenêtre
	Stage stage = new Stage(StageStyle.DECORATED);
	stage.initModality(Modality.NONE);
	stage.initOwner(primaryStage);
	// Position lors de l'ouverture;
	stage.setX(primaryStage.getX() + 100);
	stage.setY(primaryStage.getY() + 40);

	// Crée un loader pour charger la vue FXML
	FXMLLoader loader = new FXMLLoader(getClass().getResource("/vues/VueBorneIn.fxml"));
	try {
		bundle = ResourceBundle.getBundle("vues.bundles.vueBorneIn",locale);
		loader.setResources(bundle);
		// Obtenir la traduction du titre dans la locale
		stage.setTitle(bundle.getString("Titre"));
	} catch (Exception e) {
		showErreur(e.getMessage());
		stage.setTitle("Vue Ajout Voiture");
	}

	// Charge la vue à partir du Loader
	// et initialise son contenu en appelant la méthode setUp du controleur
	Pane root;
	try {
		root = loader.load();
		// récupère le ctrl (après l'initialisation)
		CtrBorneIn ctrl = loader.getController();
		ctrl.setPr(this);
		// fourni la fabrique au ctrl pour charger les articles
		ctrl.setUP(facade);
		// charge le Pane dans la Stage
		stage.setScene(new Scene(root, 400, 400));
	} catch (IOException e) {
		showErreur("Impossible de charger la vueBorneIn " + e.getMessage());
		stage = null;
	}
	return stage;
	}
	
	private void showErreur(String message) {
		Alert a = new Alert(AlertType.ERROR, message);
		a.showAndWait();
	}
	public void getStationnements(){
		List<Stationnement> sta = stationnement.getListe("");
		staList =FXCollections.observableList(sta);
		tab.setItems(staList);
	}

	/**
	 * Point d'entrée de l'application
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(Parking.class, args);

	}
}
