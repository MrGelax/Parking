package dao;

import static org.testng.Assert.fail;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import javax.naming.OperationNotSupportedException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import modele.Historique;
import modele.Personnel;
import modele.Stationnement;;

public abstract class SQLHistoriqueDAO implements IHistoriqueDAO {
	String SQL01 = "CALL Historique(?)";
	
	private static final Logger logger = LoggerFactory.getLogger(SQLPersonnelDAO.class);
	private final SQLDAOFactory factory;
	
	public SQLHistoriqueDAO(SQLDAOFactory factory) {
		this.factory = factory;
	}
	/**
	 * Selectionne et retourne l'intégralité de l'historique d'une voiture
	 * @return L'historique complet d'une voiture sous forme de liste
	 * @param Reçoit l'immatricule d'une voiture dont on veut l'historique
	 */
	public List<Historique> getListe(String immatr) {
		List<Historique> liste=new ArrayList<Historique>();
		Historique histo;
		try(CallableStatement q01 = factory.getConnexion().prepareCall(SQL01)) {
			ResultSet rs;
			q01.setString(1,immatr);
		
			//Il ne fait pas l'association
			// Associer le type du param de retour
			q01.registerOutParameter(1,Types.TIMESTAMP);
			// exécution du query
			rs = q01.executeQuery();
			while (rs.next()) {
				histo=new Historique(rs.getTimestamp(1),rs.getTimestamp(2),immatr,rs.getString(4),rs.getInt(3));
				liste.add(histo);
				System.out.println(histo);
			}
		} catch (Exception e) {
			logger.error("Erreur lors du chargement de l'historique de la voiture "+immatr, e);
		}
		return liste;
	}
}
