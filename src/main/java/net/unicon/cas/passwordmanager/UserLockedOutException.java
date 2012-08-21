package net.unicon.cas.passwordmanager;

public class UserLockedOutException extends Exception {

	private static final long serialVersionUID = 1L;

	public UserLockedOutException() {
		super();
	}
	
	public UserLockedOutException(String s) { 
		super(s);
	}
	
	public UserLockedOutException(String s, Throwable t) {
		super(s, t);
	}
}
