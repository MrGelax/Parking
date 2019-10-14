package dao;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.sql.Timestamp;
import java.util.List;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import dao.DAOFactory.TypePersistance;
import databases.ConnexionFromFile;
import databases.ConnexionSingleton;
import databases.Databases;
import databases.PersistanceException;
import modele.Personnel;
import modele.Place;
import modele.Stationnement;
public class StationnementDAO {
	private DAOFactory factory;
	
	public void testgetListe() {
		List<Stationnement> sta=factory.getStationnementDAO().getListe("");
	    assertEquals(sta.size(),1); 
	}

	
	public void testDelete() {
		boolean c=false;
		try{
		}catch(Exception e) {
			
		}
		assertEquals(c,true);
	}
	@Test
	public void testInsert() {
		String immatr="";
		try {
			immatr = factory.getStationnementDAO().insert(new Stationnement("JEA-780","P01",new Timestamp(System.currentTimeMillis())));
		} catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println(immatr);
	    assertEquals(immatr,"P01");
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
