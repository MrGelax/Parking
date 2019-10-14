package modele;

import java.sql.Timestamp;

public class Stationnement {
	
	private String fkPersonne;
	private String fkPlace;
	private Timestamp momentA;
	public Stationnement(String per,String pla,Timestamp momentA){
		super();
		this.fkPersonne=per;
		this.fkPlace=pla;
		this.momentA=momentA;
	}
	public String getFKPersonne() {
		return this.fkPersonne;
	}
	public String getFKPlace() {
		return this.fkPlace;
	}
	public Timestamp getMomentA(){
		return this.momentA;
	}
	public void setFKPersonne(String per) {
		this.fkPersonne=per;
	}
	public void setFKPlace(String pla) {
		this.fkPlace=pla;
	}
	public void setMomentA(Timestamp arrive){
		this.momentA=arrive;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fkPersonne == null) ? 0 : fkPersonne.hashCode());
		result = prime * result + ((fkPlace == null) ? 0 : fkPlace.hashCode());
		result = prime * result + ((momentA == null) ? 0 : momentA.hashCode());
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
		Stationnement other=(Stationnement)obj;
		if((this.fkPersonne==other.fkPersonne)&&(this.fkPlace==other.fkPlace)&&
				(this.momentA==other.momentA)) {
			return true;
		}else {
			return false;
		} 
	}
	@Override
	public String toString() {
		return "Immatriculation : "+this.fkPersonne+"\nPlace : "+this.fkPlace+"\nMoment Arrivée : "+this.momentA;
	}
}
