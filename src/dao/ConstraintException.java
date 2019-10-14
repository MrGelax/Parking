package dao;

public class ConstraintException extends SQLException{
	private final String champ;

	public ConstraintException(String msg, int code, String champ) {
		super(msg, code);
		this.champ = champ;

	}

	/**
	 * @return the champ
	 */
	public String getChamp() {
		return champ;
	}

}
