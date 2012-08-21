package net.unicon.cas.passwordmanager.service;

import java.util.Date;

public class UserLockoutStatus {

	private String username;
	private int incorrectAttempts = 0;
	private Date nextAttemptAllowed;
	
	public UserLockoutStatus() { }
	
	public UserLockoutStatus(String username) {
		this.username = username;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	/**
	 * <p>Gets the number of bad security question/password change attempts.</p>
	 */
	public int getIncorrectAttempts() {
		return incorrectAttempts;
	}
	
	public void setIncorrectAttempts(int incorrectAttempts) {
		this.incorrectAttempts = incorrectAttempts;
	}
	
	/**
	 * <p>Increments the number of bad security question/password change attempts.</p>
	 * @return The new value (after incrementing) of bad attempts.
	 */
	public int incrementIncorrectAttempts() {
		return ++this.incorrectAttempts;
	}
	
	/**
	 * <p>Gets the time that the user will be allowed to attempt to answer
	 * their security questions or change their password again.
	 */
	public Date getNextAttemptAllowed() {
		return nextAttemptAllowed;
	}
	
	public void setNextAttemptAllowed(
			Date nextAttemptAllowed) {
		this.nextAttemptAllowed = nextAttemptAllowed;
	}
}
