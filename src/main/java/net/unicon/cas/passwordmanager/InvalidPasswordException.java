package net.unicon.cas.passwordmanager;

public class InvalidPasswordException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public InvalidPasswordException(String s) {
		super(s);
	}
	
	public InvalidPasswordException(String s, Throwable t) {
		super(s, t);
	}
	
	public InvalidPasswordException(Throwable t) {
		super (t);
	}
}
