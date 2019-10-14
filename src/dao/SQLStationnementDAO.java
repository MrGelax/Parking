package dao;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLType;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import modele.Personnel;
import modele.Place;
import modele.Stationnement;
/*
 * @author Da Silva Pedro
 */
public class SQLStationnementDAO implements IStationnementDAO {
	
	String SQL01 = "CALL ARRIVEE_VOIT(?,?,?,?)";
	String sql0="CALL SORTIE_VOIT(?) ";
	String SQL02 = "Delete from TSTATIONNEMENT WHERE FKPERSONNE_STA=?";
	private static final String sqlListe = "Select * from TStationnement";

	
	private static final Logger logger = LoggerFactory.getLogger(SQLPersonnelDAO.class);
	private final SQLDAOFactory factory;

	/**
	 * Construction pour avoir l'accès à la factory et ainsi obtenir la connexion
	 * 
	 * @param factory
	 */
	public SQLStationnementDAO(SQLDAOFactory factory) {
		this.factory = factory;
	}
	/**
	 * Insert dans la DB un stationnement passé en paramètre
	 * @param le stationnement que l'on veut inserer
	 * @return La place où la voiture est stationné
	 * 
	 */
	public String insert(Stationnement s) throws Exception{
		String res="";
		try (CallableStatement q01 = factory.getConnexion().prepareCall(SQL01)){
			// Associer le Param d'entrée
			q01.setString(1,s.getFKPersonne());
			if(s.getFKPlace()!=null) {
				q01.setString(2,s.getFKPlace());
			}else {
				q01.setString(2,null);
			}
			// Associer le type du param de retour
			q01.registerOutParameter(3,Types.VARCHAR);
			q01.registerOutParameter(4,Types.TIMESTAMP);
			// exécution du query
			ResultSet rs = q01.executeQuery();
			rs.next();
			res=rs.getString(1);
			factory.getConnexion().commit();
		} catch (SQLException e) {
			logger.error("Erreur lors l'insertion du stationnement", e);
			this.factory.getConnexion().rollback();
			dispatchSpecificException(e);
		}
		return res;
	}
	
	/**
	 * Suprime un stationnement
	 * @return retourne la durée en minute du stationnement suprimmé
	 * @param Le Stationnement à supprimer du parking  
	 * @see dao.IDAO#deleteInt(java.lang.Object)
	 */
	public int deleteInt(Stationnement s) throws Exception {
		int rp=0;
		try (CallableStatement querySupp = factory.getConnexion().prepareCall(sql0)) {
			querySupp.setString(1,s.getFKPersonne().trim());
			ResultSet rs=querySupp.executeQuery();
			rs.next();
			rp=rs.getInt(2);
			logger.debug("Un Stationnement a été supprimé:",rs.getString(1));
			factory.getConnexion().commit();
		} catch (SQLException e) {
			factory.getConnexion().rollback();
			logger.error("Erreur de suppression du Stationnement " + s.toString(), e);
			dispatchSpecificException(e);
		}
		return rp;
	}
	/**
	 * Charge et retourne la liste courrante des voitures dans le parking
	 * @return Une liste contenant les stationnement dans le parking
	 * @param La plaque d'immatriculation 
	 */
	public List<Stationnement> getListe(String regExpr) {
		List<Stationnement> liste = new ArrayList<>();
		try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlListe)) {
			Stationnement sta;
			ResultSet rs;
			rs = query.executeQuery();
			while (rs.next()) {
				sta=new Stationnement(rs.getString(1),rs.getString(2),rs.getTimestamp(3));
				liste.add(sta);
			}
		} catch (SQLException e) {
			logger.error("Erreur lors du chargement des Stationnements", e);
		}
		return liste;
	}
	private static void dispatchSpecificException(SQLException e) throws dao.SQLException{
		
		switch (e.getErrorCode()) {
		case 335544665:// PK
			throw new PKExecption(e.getMessage(), e.getErrorCode(), "CODE");
		case 335544347:// Contrainte unicité, check
			throw new ConstraintException(e.getMessage(), e.getErrorCode(), extractField(e.getMessage()));
		default:
			throw new dao.SQLException(e.getMessage(), e.getErrorCode());
		}
	}
	
	private static String extractField(String msg) {
		return msg.substring(msg.indexOf('.')+2, msg.indexOf(',')-5);	
	}
}
