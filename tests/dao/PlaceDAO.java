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
import modele.Personnel;
import modele.Place;
public class PlaceDAO {
	private DAOFactory factory;
	@Test
	public void getFromID() {
		 
		Place c = factory.getPlaceDAO().getFromID("P01");
		System.out.println(c.getCodse()+" "+c.getTaille()+" "+c.getLibre());
		assertEquals(c.getCodse(), "P01");
		assertEquals(c.getLibre(),true);
		assertEquals(c.getTaille(),10);
	}
	@Test
	public void testGetListe() {
      List<Place> liste=factory.getPlaceDAO().getListe("");
      assertEquals(liste.size(),11);
	}
	@Test
	public void testInsert(){
		String code="";
		try {
			code = factory.getPlaceDAO().insert(new Place("P12",10,true));
		} catch (Exception e) {
			// TODO: handle exception
		}
	    assertEquals(code,"P12");
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
