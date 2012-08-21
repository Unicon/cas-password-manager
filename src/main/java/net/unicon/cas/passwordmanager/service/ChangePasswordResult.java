package net.unicon.cas.passwordmanager.service;

import java.io.Serializable;

/**
 * <p>Bean for holding password set/change results.</p>
 * @author Drew Mazurek &lt;dmazurek@unicon.net&gt;
 */
public class ChangePasswordResult implements Serializable {

	private static final long serialVersionUID = 1L;
	private Result result;
	private String defaultMessage;
	private String messageKey;

	/**
	 * <p>Gets the result of the password change</p>
	 * @return Result enum
	 */
	public Result getResult() {
		return result;
	}

	public void setResult(Result result) {
		this.result = result;
	}
	
	/**
	 * <p>Gets the default plaintext message of the result of the attempted
	 * password change</p>
	 * @return Default message of password change
	 */
	public String getDefaultMessage() {
		return defaultMessage;
	}

	public void setDefaultMessage(String defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	/**
	 * <p>Gets the i18n message key for the attempted password change</p>
	 * @return Message key for attempted password change
	 */
	public String getMessageKey() {
		return messageKey;
	}

	public void setMessageKey(String messageKey) {
		this.messageKey = messageKey;
	}
	
	/**
	 * <p>Enum summary of the attempted password change. More detailed
	 * information can be set in the (hopefully user-friendly) messages
	 * supported by ChangePasswordResult.</p>
	 * @author Drew Mazurek &lt;dmazurek@unicon.net&gt;
	 */
	public enum Result { SUCCESS, FAILURE, ERROR };
}
