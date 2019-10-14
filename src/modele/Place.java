package modele;

import java.sql.Timestamp;

public class Place {
	private String code;
	private int taille;
	private boolean libre;
	
	public Place(String c,int t,boolean l) {
		super();
		this.code=c;
		this.taille=t;
		this.libre=l;
	}
	
	public String getCodse() {
		return this.code;
	}
	public boolean getLibre() {
		return this.libre;
	}
	public int getTaille(){
		return this.taille;
	}
	public void setFKPersonne(String c) {
		this.code=c;
	}
	public void setFKPlace(Boolean l) {
		this.libre=l;
	}
	public void setMomentA(int t){
		this.taille=t;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + (((Integer)taille == null) ? 0 : ((Integer)taille).hashCode());
		result = prime * result + (((Boolean)libre == null) ? 0 : ((Boolean)libre).hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass()!=obj.getClass())
			return false;
		Place other=(Place)obj;
		if((this.code==other.code)&&(this.libre==other.libre)&&
				(this.taille==other.taille)) {
			return true;
		}else {
			return false;
		} 
	}
	@Override
	public String toString() {
		return "Place : "+this.code+"\nTaille : "+this.taille+"\nLibre : "+this.libre;
	}
}
