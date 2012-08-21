package net.unicon.cas.passwordmanager.ldap;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

public class OpenLdapLdapServer extends AbstractLdapServer implements
		LdapServer, InitializingBean {

	private final Log logger = LogFactory.getLog(this.getClass());
	private String encryptionAlgorithm;
	
	@Override
	public void setPassword(String username, String password) {
		
		logger.debug("Setting password for " + username);
		
		String passwordText = null;
		
		if(encryptionAlgorithm != null && !encryptionAlgorithm.isEmpty()) {
			String encryptedPassword = encrypt(password);
			passwordText = "{" + encryptionAlgorithm + "}" + encryptedPassword;
		} else {
			logger.debug("Setting unencrypted password for " + username +"! Consider setting the "
					+ "encryptionAlgorithm property of this bean in passwordManagerContext.xml!");
			passwordText = password;
		}
		Attribute passwordAttribute = new BasicAttribute(passwordAttr, passwordText);
		
		ModificationItem[] modificationItems = new ModificationItem[1];
		modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, passwordAttribute);
		
		ldapModify(username, modificationItems);
	}
	
	private String encrypt(String plainText) {
		
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(encryptionAlgorithm);
			md.update(plainText.getBytes("UTF-8"));
		} catch(NoSuchAlgorithmException ex) {
			logger.error("No such algorithm: " + encryptionAlgorithm);
			throw new RuntimeException("No such algorithm: " + encryptionAlgorithm,ex);
		} catch(UnsupportedEncodingException ex) {
			logger.error("Unsupported encoding: UTF-8");
			throw new RuntimeException("Unsupported encoding: UTF-8",ex);
		}
		byte bytes[] = md.digest();
		return Base64.encodeBase64String(bytes);
	}

	public void setEncryptionAlgorithm(String encryptionAlgorithm) {
		this.encryptionAlgorithm = encryptionAlgorithm;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
	}
}
