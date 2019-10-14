package dao;

public class PKExecption extends SQLException{
	private final String champ;

	public PKExecption(String msg, int code, String champ) {
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
