package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import modele.Place;

public abstract class SQLPlaceDAO implements IPlaceDAO {

	private static final String sqlFromId = "Select * from TPlace where CODE_PLA=?";
	private static final String sqlListeSer = "Select * from TPlace";
	private static final String sqlListeDispo="Select CODE_PLA from TPlace where LIBRE_PLA=true";
	private static final String sqlInsert = "INSERT INTO TPlace (CODE_PLA,TAILLE_PLA,LIBRE_PLA) VALUES (?,?,?)";
	// Logger
		private static final Logger logger = LoggerFactory.getLogger(SQLPersonnelDAO.class);

		// La factory pour avoir la connexion
		private final SQLDAOFactory factory;

		/**
		 * Construction pour avoir l'accès à la factory et ainsi obtenir la connexion
		 * 
		 * @param factory
		 */
		public SQLPlaceDAO(SQLDAOFactory factory) {
			this.factory = factory;
		}
		/**
		 * selectionne une place selon un id de place
		 * @return une place
		 * @param l'id d'une place
		 */
		public Place getFromID(String id) {

			Place ser = null;
			if (id != null)
				id = id.trim();
			try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlFromId);) {
				ResultSet rs;
				// préparation d'un query

				// associe une valeur au paramètre (code_art)
				query.setString(1, id);
				// exécution
				rs = query.executeQuery();
				// parcourt du ResultSet
				if (rs.next()) {
					ser = new Place(id, rs.getInt(2),rs.getBoolean(3));
				}
			} catch (SQLException e) {
				logger.error("Erreur SQL ", e);
						
			}
			return ser;
		}

		/**
		 * retourne un liste de place selon un critère
		 * @param un string reprensentant le critère de selection des places
		 * @return une liste de place
		 */
		public List<Place> getListe(String regExpr) {
			List<Place> liste = new ArrayList<>();
			try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlListeSer)) {
				Place per;
				ResultSet rs;
				rs = query.executeQuery();
				while (rs.next()) {
					per = new Place(rs.getString(1), rs.getInt(2), rs.getBoolean(3));
					liste.add(per);
				}
			} catch (SQLException e) {
				logger.error("Erreur lors du chargement des Serveurs", e);
			}
			return liste;
		}
		/**
		 * retourne une liste des place libres
		 */
		public List<String> getListeDispo() {
			List<String> liste = new ArrayList<>();
			try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlListeDispo)) {
				ResultSet rs;
				rs = query.executeQuery();
				while (rs.next()) {
					liste.add(rs.getString(1));
				}
			} catch (SQLException e) {
				logger.error("Erreur lors du chargement des Serveurs", e);
			}
			return liste;
		}
		/**
		 * insert un plce dans la DB
		 * @param La place que l'on veut inserer
		 */
		public String insert(Place p) throws Exception {
			if (p == null)
				return null;
			try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlInsert)) {
				query.setString(1, p.getCodse());
				query.setInt(2, p.getTaille());
				query.setBoolean(3, p.getLibre());
				query.executeUpdate();
				query.getConnection().commit();
			} catch (SQLException e) {
				this.factory.getConnexion().rollback();
				dispatchSpecificException(e);
			}
			return p.getCodse();
		}

		private static void dispatchSpecificException(SQLException e) throws dao.SQLException{
			switch (e.getErrorCode()) {
			case 335544665:// PK
				throw new PKExecption(e.getMessage(),e.getErrorCode(),extractField(e.getMessage()));
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
