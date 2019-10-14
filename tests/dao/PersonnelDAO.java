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
public class PersonnelDAO {
	private DAOFactory factory;
	@Test
	public void getFromId() {
		Personnel c = factory.getIVoitureDAO().getFromID("AAA-111");
		assertNotNull(c);
		assertEquals(c.getImmatr().trim(), "AAA-111");
		assertNotNull(c.getNom().trim(),"de Block");
		assertEquals(c.getPrenom().trim(), "Anne");
	}
	@Test
	public void getList() {
		 List<Personnel> liste=factory.getIVoitureDAO().getListe("");
	      assertEquals(liste.size(),14); 
	}
	@Test
	public void testInsert() {
		String immatr="";
		try {
			immatr = factory.getIVoitureDAO().insert(new Personnel("UIP-890","Jean","Val"));
		} catch (Exception e) {
			// TODO: handle exception
		}
	    assertEquals(immatr,"UIP-890");
	}
	@Test
	public void testDelete() {
		boolean c=false;
		try{
			c = factory.getIVoitureDAO().delete(new Personnel("UIP-890","Jean","Val"));
		}catch(Exception e) {
			
		}
		assertEquals(c,true);
	}
	@Test
	public void testUpdate() {
		boolean c=false;
		try {
			c = factory.getIVoitureDAO().update(new Personnel("AAA-111","Marc","Val"));
		}catch(Exception e) {
			
		}
		assertEquals(c,true);
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
