package net.unicon.cas.passwordmanager.service;

import java.io.Serializable;

/**
 * <p>Bean for passing around password expiration information.</p>
 * @author Drew Mazurek &lt;dmazurek@unicon.net&gt;
 *
 */
public class PasswordWarningInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	private final long pwdAgeSeconds;
	private final boolean warn;
	
	public PasswordWarningInfo(long pwdAgeSeconds, boolean warn) {
		this.pwdAgeSeconds = pwdAgeSeconds;
		this.warn = warn;
	}

	public long getPwdAgeSeconds() {
		return pwdAgeSeconds;
	}

	public boolean isWarn() {
		return warn;
	}
}
