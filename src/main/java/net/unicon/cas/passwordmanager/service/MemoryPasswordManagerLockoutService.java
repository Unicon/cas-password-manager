package net.unicon.cas.passwordmanager.service;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.unicon.cas.passwordmanager.UserLockedOutException;

public class MemoryPasswordManagerLockoutService implements
		PasswordManagerLockoutService {
	
	private final Log logger = LogFactory.getLog(this.getClass());
	private final ConcurrentHashMap<String,UserLockoutStatus> lockoutMap = new ConcurrentHashMap<String,UserLockoutStatus>();
	private int allowedIncorrectAttempts = Integer.MAX_VALUE;
	private int secondsUntilNextAllowedAttempt = 0;
	
	@Override
	public void registerIncorrectAttempt(String username) throws UserLockedOutException {
		
		logger.debug("Registering incorrect attempt for " + username);
		
		UserLockoutStatus status = lockoutMap.get(username);
		if(status == null) {
			status = new UserLockoutStatus(username);
			lockoutMap.put(username, status);
		}
		
		int attempts = status.incrementIncorrectAttempts();
		
		if(attempts < allowedIncorrectAttempts) {
			return;
		}
		
		// set the lockout time
		Date d = new Date();
		d.setTime(d.getTime() + secondsUntilNextAllowedAttempt * 1000);
		logger.debug("Locking out " + username + " from changes until " + d);
		status.setNextAttemptAllowed(d);
		
		throw new UserLockedOutException("User " + username + " locked out.");
	}

	@Override
	public void allowAttempt(String username) throws UserLockedOutException {
		
		if(username == null) {
			logger.debug("Null username.");
			return;
		}
		
		UserLockoutStatus status = lockoutMap.get(username);
		
		if(status == null) {
			logger.debug("No lockout status found for " + username);
			return;
		}
		
		if(status.getIncorrectAttempts() < allowedIncorrectAttempts) {
			logger.debug("User " + username + " tried " 
					+ status.getIncorrectAttempts() + "/" 
					+ allowedIncorrectAttempts + " allowed attempts.");
			return;
		}
		
		Date now = new Date();
		
		if(now.after(status.getNextAttemptAllowed())) {
			logger.debug("Timer expired, user " + username + " can attempt changes again.");
			lockoutMap.remove(username);
			return;
		}

		logger.info("User " + username + " used all their attempts. Locked out.");
		if(logger.isDebugEnabled()) {
			Date next = status.getNextAttemptAllowed();
			long diff = (next.getTime() - now.getTime()) / 1000;
			logger.debug("--> next change allowed in " + diff + " seconds.");
		}

		throw new UserLockedOutException("User " + username + " locked out.");
	}

	@Override
	public void clearIncorrectAttempts(String username) {
		lockoutMap.remove(username);		
	}

	public void setAllowedIncorrectAttempts(int allowedIncorrectAttempts) {
		this.allowedIncorrectAttempts = allowedIncorrectAttempts;
	}

	public void setSecondsUntilNextAllowedAttempt(int secondsUntilNextAllowedAttempt) {
		this.secondsUntilNextAllowedAttempt = secondsUntilNextAllowedAttempt;
	}
}
