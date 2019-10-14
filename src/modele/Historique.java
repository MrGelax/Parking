package modele;

import java.sql.Timestamp;
import java.util.Date;

public class Historique {
	private Timestamp momentS;
	private Timestamp momentA;
	private String fKPersonne;
	private String fkPlace;
	private int duree;
	public Historique(Timestamp momentA,Timestamp momentS,String fkPersonne,String fkPlace,int dur){
		super();
		this.fKPersonne=fkPersonne;
		this.fkPlace=fkPlace;
		this.momentA=momentA;
		this.momentS=momentS;
		this.duree=dur;
	}
	public String getFKPersonne() {
		return this.fKPersonne;
	}
	public String getFKPlace() {
		return this.fkPlace;
	}
	public Timestamp getMomentA(){
		return this.momentA;
	}
	public Timestamp getMomentS(){
		return this.momentS;
	}
	public int getDuree(){
		return this.duree;
	}
	public void setDuree(int d) {
		this.duree=d;
	}
	public void setFKPersonne(String per) {
		this.fKPersonne=per;
	}
	public void setFKPlace(String pla) {
		this.fkPlace=pla;
	}
	public void setMomentA(Timestamp arrive){
		this.momentA=arrive;
	}
	public void setMomentS(Timestamp sortie){
		this.momentS=sortie;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((momentA == null) ? 0 : momentA.hashCode());
		result = prime * result + ((momentA == null) ? 0 : momentS.hashCode());
		result = prime * result + ((fKPersonne == null) ? 0 : fKPersonne.hashCode());
		result = prime * result + ((fkPlace == null) ? 0 : fkPlace.hashCode());
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
		Historique other=(Historique)obj;
		if((this.fKPersonne==other.fKPersonne)&&(this.fkPlace==other.fkPlace)&&
				(this.momentA==other.momentA)&&(this.momentS==other.momentS)) {
			return true;
		}else {
			return false;
		} 
	}
	@Override
	public String toString() {
		return "Immatriculation : "+this.fKPersonne+"\nPlace : "+this.fkPlace+"\nMoment Arrivée : "+
					this.momentA+"\nMoment Sortie : "+this.momentS;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
