package dao;

public class SQLException extends Exception {
	private final int code;
    
	public SQLException(String msg,int code) {
		super(msg);
		this.code=code;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

}
