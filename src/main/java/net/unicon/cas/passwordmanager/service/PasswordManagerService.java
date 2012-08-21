package net.unicon.cas.passwordmanager.service;

import net.unicon.cas.passwordmanager.UserLockedOutException;
import net.unicon.cas.passwordmanager.flow.SecurityChallenge;

/**
 * <p>Password manager service interface.</p>
 * @author Drew Mazurek &lt;dmazurek@unicon.net&gt;
 */
public interface PasswordManagerService {
	
	/**
	 * <p>Gets the given user's security challenge question(s).</p>
	 * @param username Username to retrieve security challenge questions for
	 * @return SecurityChallenge object containing the security questions
	 * and answers
	 */
	public SecurityChallenge getUserSecurityChallenge(String username);
	
	/**
	 * <p>Method for setting up the user's security challenge question(s).</p>
	 * @param username Username whose questions we're setting up
	 * @param securityChallenge SecurityChallenge object containing their security
	 * challenge questions
	 */
	public void setUserSecurityChallenge(String username, SecurityChallenge securityChallenge);
	
	/**
	 * <p>Gets the default security challenge and the user's personalized
	 * responses.</p>
	 * @param username Username to retrieve security challenge questions for
	 * @return SecurityChallenge object containing the default security
	 * questions and answers
	 */
	public SecurityChallenge getDefaultSecurityChallenge(String username);
	
	/**
	 * <p>Sets the user's password.</p> 
	 * @param username Username of the password to set
	 * @param password The user's unencrypted password. Implementations of this
	 * method must implement any hashing or encryption necessary.
	 * @return ChangePasswordResult containing the status of the attempted 
	 * password change
	 */
	public void setUserPassword(String username, 
			String password);
	
	/**
	 * <p>Changes the user's password, verifying the old password first. This
	 * method must not change the password if the old password is invalid.</p>
	 * @param username Username of the password to change
	 * @param oldPassword The user's old unencrypted password
	 * @param newPassword The user's new unencrypted password
	 * @return ChangePasswordResult containing the status of the attempted
	 * password change
	 */
	public void changeUserPassword(String username, String oldPassword, String newPassword)
	 	throws UserLockedOutException;
}
