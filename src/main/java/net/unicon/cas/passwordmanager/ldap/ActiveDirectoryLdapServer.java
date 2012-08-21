package net.unicon.cas.passwordmanager.ldap;

import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.unicon.cas.passwordmanager.PasswordManagerException;
import org.springframework.beans.factory.InitializingBean;

public class ActiveDirectoryLdapServer extends AbstractLdapServer implements
	LdapServer, InitializingBean {

	@SuppressWarnings("unused")
	private final Log logger = LogFactory.getLog(this.getClass());
	public static final long ONE_HUNDRED_NANOSECOND_DIVISOR = 10000000L;
	public static final long JAVA_TO_WIN_TIME_CONVERSION = 11644473600000L;
	
	@Override
	public void setPassword(String username, String password) {

		byte[] encodedPassword = encodePassword(password);
		ModificationItem[] modificationItems = new ModificationItem[1];
		Attribute passwordAttribute = new BasicAttribute(passwordAttr,encodedPassword);
		modificationItems[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
				passwordAttribute);

		ldapModify(username, modificationItems);
	}
	
	protected long getCurrentWin32Time() {
		Date now = new Date();
		long nowInWin32 = (now.getTime() + JAVA_TO_WIN_TIME_CONVERSION) * 10000L;
		return nowInWin32;
	}
	
	protected byte[] encodePassword(String password) {
		String quotedPassword = "\"" + password + "\"";
		try {
			return quotedPassword.getBytes("UTF-16LE");
		} catch(UnsupportedEncodingException ex) {
			throw new PasswordManagerException("UnsupportedEncodingException changing password.",ex);
		}
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
	}
}
