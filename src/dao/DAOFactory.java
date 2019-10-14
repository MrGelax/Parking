package dao;

import java.sql.Connection;

import dao.DAOFactory.TypePersistance;

public abstract class DAOFactory {
	public enum TypePersistance {
		FIREBIRD
	}

	public abstract IHistoriqueDAO getHistoriqueDAO();
	public abstract IPlaceDAO getPlaceDAO();
	public abstract IStationnementDAO getStationnementDAO();
	public abstract IVoitureDAO getIVoitureDAO();
	
	/**
	 * Méthode statique pour générer une fabrique concrète
	 * 
	 * @param type      de persistance
	 * @param connexion une connexion SQL ou null si inutile pour du SQL
	 * @return une fabrique concrète pour le type de persistance
	 */
	public static DAOFactory getDAOFactory(TypePersistance type, Connection connexion) {
		switch (type) {
		case FIREBIRD:
			if (connexion != null)
				return new FBDAOFactory(connexion);// une fabrique concrète pour Firebird
		default:
			break;
		}
		return null;
	}
}
