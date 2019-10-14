package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import modele.Personnel;

public abstract class SQLPersonnelDAO implements IVoitureDAO {

	private static final String sqlFromId = "Select * from TPersonnel where IMMATR_PER=?";
	private static final String sqlListePerso = "Select * from TPersonnel";
	private static final String sqlListeDispo="SELECT r.IMMATR_PER FROM TPERSONNEL r left Join TSTATIONNEMENT s on r.IMMATR_PER=s.FKPERSONNE_STA where s.FKPERSONNE_STA is null";
	private static final String sqlInsert = "INSERT INTO TPersonnel (IMMATR_PER,NOM_PER,PRENOM_PER) VALUES (?,?,?)";
	private static final String sqlDelete = "Delete from TPersonnel WHERE IMMATR_PER=?";
	private static final String SQL_UPDATE = "update TPersonnel set NOM_PER = ?,PRENOM_PER = ? where trim(IMMATR_PER) = ?";
	
	// Logger
		private static final Logger logger = LoggerFactory.getLogger(SQLPersonnelDAO.class);

		// La factory pour avoir la connexion
		private final SQLDAOFactory factory;

		/**
		 * Construction pour avoir l'accès à la factory et ainsi obtenir la connexion
		 * 
		 * @param factory
		 */
		public SQLPersonnelDAO(SQLDAOFactory factory) {
			this.factory = factory;
		}

		/**
		 * 
		 */
		public Personnel getFromID(String id) {

			Personnel ser = null;
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
					ser = new Personnel(id, rs.getString(2), rs.getString(3));
				}
			} catch (SQLException e) {
				logger.error("Erreur SQL ", e);
						
			}
			return ser;
		}
		/**
		 * Crée un liste de voitur
		 * @param Reçoit en param un string
		 * @return Retourne la liste des voiture autorisés dans le parking
		 */
		public List<Personnel> getListe(String regExpr) {
			List<Personnel> liste = new ArrayList<>();
			try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlListePerso)) {
				Personnel per;
				ResultSet rs;
				rs = query.executeQuery();
				while (rs.next()) {
					per = new Personnel(rs.getString(1), rs.getString(2), rs.getString(3));
					liste.add(per);
				}
			} catch (SQLException e) {
				logger.error("Erreur lors du chargement des Serveurs", e);
			}
			return liste;
		}
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
		 * Insert une personne
		 * @return L'immatriculation de la voiture inseré
		 * @param La voiture à inserer
		 */
		public String insert(Personnel p) throws Exception {
			if (p == null)
				return null;
			try (PreparedStatement query = factory.getConnexion().prepareStatement(sqlInsert)) {
				query.setString(1, p.getImmatr());
				query.setString(2, p.getNom());
				query.setString(3, p.getPrenom());
				query.executeUpdate();
				query.getConnection().commit();
			} catch (SQLException e) {
				this.factory.getConnexion().rollback();
				dispatchSpecificException(e);
			}
			return p.getImmatr();
		}

		/**
		 * Supprime une Voiture
		 * @return renvoie true si ça c'ets bien passé et false sinon
		 * @param La voiture à supprimer
		 */

		public boolean delete(Personnel p) throws Exception {
			boolean ok = false;
			if (p == null)
				return false;
			int cpt;
			String code = p.getImmatr().trim();
			try (PreparedStatement querySupp = factory.getConnexion().prepareStatement(sqlDelete)) {
				querySupp.setString(1, code);
				cpt = querySupp.executeUpdate();
				ok = (cpt != 0);
				logger.debug("Un Serveur a été supprimé:", code);
				factory.getConnexion().commit();
				ok=true;
			} catch (SQLException e) {
				factory.getConnexion().rollback();
				logger.error("Erreur de suppression du serveur " + p.toString(), e);
				dispatchSpecificException(e);
			}

			return ok;
		}
		/**
		 * Met à jour une voiture
		 * @return renvoie true si ça c'ets bien passé et false sinon
		 * @param la voiture à mettre à jour
		 */
		@Override
		public boolean update(Personnel p) throws Exception {
			if (p == null)
				return false;

			try (PreparedStatement query = this.factory.getConnexion().prepareStatement(SQL_UPDATE)) {
				query.setString(1, p.getNom());
				query.setString(2, p.getPrenom());
				query.setString(3, p.getImmatr());
				query.execute();
				query.getConnection().commit();
			} catch (SQLException e) {
				logger.error("Erreur de mise à jour de la voiture " + p.toString(), e);
				this.factory.getConnexion().rollback();
				dispatchSpecificException(e);
			}

			return true;
		}
		/**
		 * 
		 * @param e
		 * @throws dao.SQLException
		 */
		private static void dispatchSpecificException(SQLException e) throws dao.SQLException{
			
			switch (e.getErrorCode()) {
			case 335544665:// PK
				throw new PKExecption(e.getMessage(), e.getErrorCode(), "Code");
			case 335544347:// Contrainte unicité, check
				throw new ConstraintException(e.getMessage(), e.getErrorCode(), extractField(e.getMessage()));
			default:
				throw new dao.SQLException(e.getMessage(), e.getErrorCode());
			}
		}
		/**
		 * 
		 * @param msg
		 * @return
		 */
		private static String extractField(String msg) {
			return msg.substring(msg.indexOf('.')+2, msg.indexOf(',')-5);	
		}
}
