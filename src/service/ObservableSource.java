package service;

import java.util.Observable;

public class ObservableSource extends Observable{
	/**
	 * rend la méthode setChanged public pour permettre d'utiliser la délégation à
	 * la place de héritage
	 */
	public void setChanged() {
		super.setChanged();
	}
}
