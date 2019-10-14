package dao;

import java.sql.Connection;

public abstract class SQLDAOFactory extends DAOFactory{
	private Connection connexion;

	public SQLDAOFactory(Connection connexion) {
		super();
		this.connexion = connexion;
	}

	public Connection getConnexion() {
		return connexion;
	}
}
