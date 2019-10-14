package modele;

public class Personnel {
	public Personnel(String immatr, String nom, String prenom) {
		super();
		this.immatr = immatr;
		this.nom = nom;
		this.prenom = prenom;
	}

	private String immatr;
	private String nom;
	private String prenom;

	/**
	 * @return the immatr
	 */
	public String getImmatr() {
		return immatr;
	}

	/**
	 * @param immatr the immatr to set
	 */
	public void setImmatr(String immatr) {
		this.immatr = immatr;
	}

	/**
	 * @return the nom
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * @param nom the nom to set
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * @return the prenom
	 */
	public String getPrenom() {
		return prenom;
	}

	/**
	 * @param prenom the prenom to set
	 */
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((immatr == null) ? 0 : immatr.hashCode());
		result = prime * result + ((nom == null) ? 0 : nom.hashCode());
		result = prime * result + ((prenom == null) ? 0 : prenom.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Personnel other = (Personnel) obj;
		if (immatr == null) {
			if (other.immatr != null)
				return false;
		} else if (!immatr.equals(other.immatr))
			return false;
		if (nom == null) {
			if (other.nom != null)
				return false;
		} else if (!nom.equals(other.nom))
			return false;
		if (prenom == null) {
			if (other.prenom != null)
				return false;
		} else if (!prenom.equals(other.prenom))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Personnel [immatr=" + immatr + ", nom=" + nom + ", prenom=" + prenom + "]";
	}

}
