package services;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

//Importation des modèles
import modele.Historique;
import modele.Personnel;
import modele.Place;
import modele.Stationnement;
public class TestEcouteur {
	class Ecouteur implements PropertyChangeListener {
		Double ancValeur;
		Double nouvelleValeur;
		int cptAppel;

		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			nouvelleValeur = (Double) evt.getNewValue();
			ancValeur = (Double) evt.getOldValue();
			cptAppel++;
		}

		public void reset() {
			ancValeur = null;
			nouvelleValeur = null;
			cptAppel = 0;
		}

	}
}
