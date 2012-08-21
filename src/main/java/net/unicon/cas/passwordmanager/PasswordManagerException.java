package net.unicon.cas.passwordmanager;

public class PasswordManagerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PasswordManagerException() {
		super();
	}
	
	public PasswordManagerException(String s) {
		super(s);
	}
	
	public PasswordManagerException(String s, Throwable t) {
		super(s, t);
	}
}
