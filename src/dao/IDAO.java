package dao;

import java.util.List;

import javax.swing.DefaultListCellRenderer;

/**
 * 
 * @author Didier
 *
 * @param <T> type des objets DAO exemple Client,...
 * @param <K> type de l'id des objets
 */
public interface IDAO<T, K> {
	/**
	 * 
	 * @param id L'id de l'objet
	 * @return l'objet crée et ses dépendances éventuelles
	 */
	default T getFromID(K id) {
		throw new UnsupportedOperationException();
	};

	/**
	 * 
	 * @param Expression régulière à appliquer sur l'ID. null: pas de condition
	 * @return une liste avec les objets
	 */
	default List<T> getListe(String regExpr) {
		throw new UnsupportedOperationException();
	};
	default List<String> getListeDispo(){
		throw new UnsupportedOperationException();
	}

	/**
	 * rend l'objet persistant
	 * 
	 * @param objet
	 * @return son id
	 */
	default K insert(T objet) throws Exception {
		throw new UnsupportedOperationException();
	};

	/**
	 * supprime l'objet de la persistance
	 * 
	 * @param object
	 * @return true si la suppression est ok
	 */
	default boolean delete(T object) throws Exception {
		throw new UnsupportedOperationException();
	};
	
	default int deleteInt(T object) throws Exception {
		throw new UnsupportedOperationException();
	};
	
	/**
	 * met la persistance à jour pour cet objet
	 * 
	 * @param object à mettre à jour
	 * @return true si ok
	 */
	default boolean update(T object) throws Exception {
		throw new UnsupportedOperationException();
	};
}