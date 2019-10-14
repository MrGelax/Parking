package service;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.DAOFactory;
import dao.IHistoriqueDAO;
import dao.IPlaceDAO;
import dao.IStationnementDAO;
import dao.IVoitureDAO;
import dao.DAOFactory.TypePersistance;
import databases.ConnexionFromFile;
import databases.ConnexionSingleton;
import databases.Databases;
import databases.PersistanceException;
import service.ObservableSource;

public class Facade {
	// Logger
	private static final Logger logger = LoggerFactory.getLogger(Facade.class);
	private DAOFactory fabrique;
	private IVoitureDAO personnelDAO;
	private IStationnementDAO stationnementDAO;
	private IHistoriqueDAO historiqueDAO;
	private IPlaceDAO placeDAO;

	private ObservableSource canalVoiture;
	private ObservableSource canalPlace;
	private ObservableSource canalStationnement;
	private ObservableSource canalHistorique;
	public Facade() {
		// crée une fabrique pour les vues
		try {
			ConnexionSingleton.setInfoConnexion(
					new ConnexionFromFile("./ressources/connexion_parking.properties", Databases.FIREBIRD));
			fabrique = DAOFactory.getDAOFactory(TypePersistance.FIREBIRD, ConnexionSingleton.getConnexion());

			// charge les DAO
			// DAO Serveur
			placeDAO = fabrique.getPlaceDAO();
			historiqueDAO = fabrique.getHistoriqueDAO();
			personnelDAO = fabrique.getIVoitureDAO();
			stationnementDAO=fabrique.getStationnementDAO();
			
			canalHistorique=new ObservableSource();;
			canalPlace=new ObservableSource();;
			canalStationnement=new ObservableSource();;
			canalVoiture=new ObservableSource();;
		} catch (PersistanceException e) {
			// showErreur(e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * @return the categorieDAO
	 */
	public IPlaceDAO getPlaceDAO() {
		return placeDAO;
	}

	/**
	 * @return the fabrique
	 */
	public DAOFactory getFabrique() {
		return fabrique;
	}

	/**
	 * @return the serveurDAO
	 */
	public IStationnementDAO getStationnementDAO() {
		return stationnementDAO;
	}

	/**
	 * @return the articleDAO
	 */
	public IHistoriqueDAO getHistoriqueDAO() {
		return historiqueDAO;
	}
	public IVoitureDAO getPersonnel() {
		return personnelDAO;
	}
	
	/**
	 * @param o
	 * @see java.util.Observable#addObserver(java.util.Observer)
	 */
	public void addServeurObserver(Observer o) {
		canalHistorique.addObserver(o);
	}

	/**
	 * @param o
	 * @see java.util.Observable#deleteObserver(java.util.Observer)
	 */
	public void deleteServeurObserver(Observer o) {
		canalHistorique.deleteObserver(o);
	}

	/**
	 * @param arg permet d'envoyer un objet pour les écouteur
	 * @see java.util.Observable#notifyObservers(java.lang.Object)
	 */
	public void notifyServeurObservers(Object arg) {
		// indique un changement
		canalHistorique.setChanged();
		// averti les écouteurs
		canalHistorique.notifyObservers(arg);
	}

	/**
	 * Averti les écouteurs sans envoyer d'objet
	 * 
	 * @see java.util.Observable#notifyObservers()
	 */
	public void notifyServeurObservers() {
		// indique un changement
		canalHistorique.setChanged();
		// averti les écouteurs
		canalHistorique.notifyObservers();
	}
}
