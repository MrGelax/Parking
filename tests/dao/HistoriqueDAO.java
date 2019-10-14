package dao;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import dao.DAOFactory.TypePersistance;
import databases.ConnexionFromFile;
import databases.ConnexionSingleton;
import databases.Databases;
import databases.PersistanceException;
import modele.Historique;
import modele.Place;
public class HistoriqueDAO {
	private DAOFactory factory;
	@Test
	public void testGetListe() {
      List<Historique> liste=factory.getHistoriqueDAO().getListe("AAA-111");
      assertEquals(liste.size(),4); 
	}
	@BeforeClass
	public void beforeClass() throws PersistanceException {
		//get Info de connexion
		ConnexionSingleton.setInfoConnexion(
				new ConnexionFromFile("./ressources/connexion_parking_test.properties", Databases.FIREBIRD));
		//création d'une usine concrète
		factory = DAOFactory.getDAOFactory(TypePersistance.FIREBIRD, ConnexionSingleton.getConnexion());
	}

	@AfterClass
	public void afterClass() {
		ConnexionSingleton.liberationConnexion();
	}
}
