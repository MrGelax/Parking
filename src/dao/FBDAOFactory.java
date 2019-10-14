package dao;

import java.sql.Connection;

public class FBDAOFactory extends SQLDAOFactory {
	
	public FBDAOFactory(Connection connexion) {
		super(connexion);
	}

	/**
	 * retourne une implémentation DAOCategorie pour Firebird
	 */
	@Override
	public IHistoriqueDAO getHistoriqueDAO() {
		return new FDHistoriqueDAO(this);
	}
	public IPlaceDAO getPlaceDAO() {
		return new FDPlaceDAO(this);
	}
	public IStationnementDAO getStationnementDAO() {
		return new FDStationnementDAO(this);
	}
	public IVoitureDAO getIVoitureDAO() {
		return new FDPersonnelDAO(this);
	}
	
}
