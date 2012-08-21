package net.unicon.cas.passwordmanager.service;

import net.unicon.cas.passwordmanager.UserLockedOutException;

/**
 * <p>Service for managing password change/security question lockouts due to 
 * too many incorrect attempts.</p>
 * @author Drew Mazurek &lt;dmazurek@unicon.net&gt;
 */
public interface PasswordManagerLockoutService {

	/**
	 * <p>Registers an incorrect attempt to answer security questions or
	 * enter a bad password.</p>
	 * @param username user who made the bad attempt
	 * @throws UserLockedOutException if the user is not allowed to make
	 * changes
	 */
	public void registerIncorrectAttempt(String username) throws UserLockedOutException;
	
	/**
	 * <p>Checks if the user is allowed to attempt their security questions
	 * or password change again.</p>
	 * @param username user to test
	 * @throws UserLockedOutException if the user is not allowed to make
	 * changes
	 */
	public void allowAttempt(String username) throws UserLockedOutException;
	
	/**
	 * <p>Resets the given user's security question/password change attempt 
	 * status. This allows them to answer questions or change their password 
	 * again. Should be called when user successfully answers the questions.</p>
	 * @param username user to reset attempt status
	 */
	public void clearIncorrectAttempts(String username);

}
