package net.unicon.cas.passwordmanager.ldap;

import java.util.ArrayList;
import java.util.List;

import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.authentication.handler.NoOpPrincipalNameTransformer;
import org.jasig.cas.authentication.handler.PrincipalNameTransformer;
import net.unicon.cas.passwordmanager.flow.SecurityChallenge;
import net.unicon.cas.passwordmanager.flow.SecurityQuestion;
import org.jasig.cas.util.LdapUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.ldap.NameNotFoundException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.NameClassPairCallbackHandler;
import org.springframework.ldap.core.ObjectRetrievalException;
import org.springframework.ldap.core.SearchExecutor;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;

public abstract class AbstractLdapServer implements LdapServer, InitializingBean {

	private final Log logger = LogFactory.getLog(this.getClass());
	protected LdapTemplate ldapTemplate;
	protected LdapContextSource ldapContextSource;
	protected List<String> securityQuestionAttrs;
	protected List<String> securityResponseAttrs;
	protected List<String> defaultQuestions;
	protected List<String> defaultResponseAttrs;
	protected String usernameAttr;
	protected String passwordAttr;
	protected String description;
	protected String searchBase;
	protected boolean ignorePartialResultException = false;
	

    /** The default maximum number of results to return. */
    private static final int DEFAULT_MAX_NUMBER_OF_RESULTS = 1000;

    /** The default timeout. */
    private static final int DEFAULT_TIMEOUT = 1000;

    /** The scope. */
    @Min(0)
    @Max(2)
    private int scope = SearchControls.SUBTREE_SCOPE;

    /** The maximum number of results to return. */
    private int maxNumberResults = DEFAULT_MAX_NUMBER_OF_RESULTS;

    /** The amount of time to wait. */
    private int timeout = DEFAULT_TIMEOUT;
    
    /** The filter path to the uid of the user. */
    @NotNull
    private String filter;
    
    @NotNull
    private PrincipalNameTransformer principalNameTransformer = new NoOpPrincipalNameTransformer();
	
	@Override
	public void ldapModify(String username, ModificationItem[] modificationItems) {
		DistinguishedName dn = searchForDn(username);
		logger.debug("ldapModify for dn " + dn + "," + ldapContextSource.getBaseLdapPathAsString());
		ldapTemplate.modifyAttributes(dn, modificationItems);
	}
	
	@Override
	public SecurityChallenge getUserSecurityChallenge(String username) {
		logger.debug("Getting user security challenge for user " + username);
		return (SecurityChallenge) ldapLookup(username, new SecurityChallengeAttributesMapper(username));
	}
	
	@Override
	public void setUserSecurityChallenge(String username, SecurityChallenge securityChallenge) {
		
		// need to modify a question attribute and an answer attribute for each
		// security question, hence 2 * securityQuestionAttrs.size().
		ModificationItem[] modificationItems = new ModificationItem[2 * securityQuestionAttrs.size()];
		List<SecurityQuestion> securityQuestions = securityChallenge.getQuestions();
		
		for(int i=0;i<securityQuestionAttrs.size();i++) {
			String securityQuestionAttr = securityQuestionAttrs.get(i);
			String securityResponseAttr = securityResponseAttrs.get(i);
			SecurityQuestion securityQuestion = securityQuestions.get(i);
			
			Attribute question = new BasicAttribute(securityQuestionAttr, securityQuestion.getQuestionText());
			Attribute response = new BasicAttribute(securityResponseAttr, securityQuestion.getResponseText());
			
			ModificationItem questionItem = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, question);
			ModificationItem responseItem = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, response);
			
			modificationItems[2*i] = questionItem;
			modificationItems[2*i+1] = responseItem;
		}
		
		ldapModify(username, modificationItems);
	}
	
	@Override
	public SecurityChallenge getDefaultSecurityChallenge(String username) {
		logger.debug("Getting default security challenge for " + username);
		return (SecurityChallenge) ldapLookup(username, new DefaultSecurityChallengeAttributesMapper(username));
	}
	
	@Override
	public abstract void setPassword(String username, String password);
	
	protected Object ldapLookup(String username, AttributesMapper mapper) {
		
		@SuppressWarnings("unchecked")
		List<Object> results = ldapTemplate.search(searchBase, usernameAttr + "=" + username, mapper);
		
		if(results.size() == 0) {
			throw new NameNotFoundException("Couldn't find " + username + " in " 
					+ ldapContextSource.getBaseLdapPathAsString() 
					+ " with base " + searchBase);
		} else if(results.size() > 1) {
			logger.warn("Multiple results found for " + username + " under " 
					+ ldapContextSource.getBaseLdapPathAsString() + " with base "
					+ searchBase);
			throw new ObjectRetrievalException("Multiple results found for " 
					+ username + " in " + ldapContextSource.getBaseLdapPathAsString()
					+ " with base " + searchBase);
		}
		
		logger.debug("Found result for " + username + " under base " 
				+ ldapContextSource.getBaseLdapPathAsString() + " with "
				+ "searchBase " + searchBase);
		return results.get(0);
	}
	
	protected DistinguishedName searchForDn(String username) {
		logger.debug("Searching for DN for " + usernameAttr + "=" + username);
		
		final List<String> cns = new ArrayList<String>();
        
        final SearchControls searchControls = getSearchControls();
        
        final String base = this.searchBase;
        final String transformedUsername = getPrincipalNameTransformer().transform(username);
        final String filter = LdapUtils.getFilterWithValues(getFilter(), transformedUsername);
        
        this.getLdapTemplate().search(
            new SearchExecutor() {
                @SuppressWarnings("rawtypes")
				public NamingEnumeration executeSearch(final DirContext context) throws NamingException {
                    return context.search(base, filter, searchControls);
                }
            },
            new NameClassPairCallbackHandler(){
                public void handleNameClassPair(final NameClassPair nameClassPair) {
                    cns.add(nameClassPair.getNameInNamespace());
                }
            });
        
        if (cns.isEmpty()) {
            logger.info("Search for " + filter + " returned 0 results.");
			throw new NameNotFoundException("Couldn't find " + username + " in " + ldapContextSource.getBaseLdapPathAsString());
        }
        if (cns.size() > 1) {
            logger.warn("Search for " + filter + " returned multiple results, which is not allowed.");
			throw new ObjectRetrievalException("Multiple results found for " + username + " in " + ldapContextSource.getBaseLdapPathAsString());
        }
		
		logger.debug("Found name: " + cns.get(0));
		return new DistinguishedName(cns.get(0));
	}

	protected Filter createUserFilter(String username) {
		Filter filter = new EqualsFilter(usernameAttr,username);
		return filter;
	}
	
	protected class DistinguishedNameContextMapper implements ContextMapper {

		@Override
		public Object mapFromContext(Object ctx) {
			
			DirContextAdapter ctxAdapter = (DirContextAdapter) ctx;			
			return ctxAdapter.getDn();
		}
		
	}

	protected class SecurityChallengeAttributesMapper implements AttributesMapper {

		private final Log logger = LogFactory.getLog(this.getClass());
		private String username;
		
		public SecurityChallengeAttributesMapper(String username) {
			this.username = username;
		}
		
		@Override
		public Object mapFromAttributes(Attributes attrs) throws NamingException {
			
			logger.debug("Mapping the security questions.");
			
			List<SecurityQuestion> securityQuestions = new ArrayList<SecurityQuestion>();
			
			for(int i=0;i<securityQuestionAttrs.size();i++) {
				String securityQuestionAttrName = securityQuestionAttrs.get(i);
				String securityResponseAttrName = securityResponseAttrs.get(i);
				
				Attribute securityQuestionAttr = attrs.get(securityQuestionAttrName);
				Attribute securityResponseAttr = attrs.get(securityResponseAttrName);
				
				if(securityQuestionAttr == null || securityResponseAttr == null) {
					return null;
				}
				
				String securityQuestionText = (String) securityQuestionAttr.get();
				String securityResponseText = (String) securityResponseAttr.get();
				
				SecurityQuestion securityQuestion = new SecurityQuestion(securityQuestionText,
						securityResponseText);
				
				securityQuestions.add(securityQuestion);
			}
			
			logger.debug("Found " + securityQuestions.size() 
					+ " security questions for " + username);
			return new SecurityChallenge(username, securityQuestions);
		}
	}

	protected class DefaultSecurityChallengeAttributesMapper implements AttributesMapper {

		private final Log logger = LogFactory.getLog(this.getClass());
		private String username;
		
		public DefaultSecurityChallengeAttributesMapper(String username) {
			this.username = username;
		}
		
		@Override
		public Object mapFromAttributes(Attributes attrs) throws NamingException {
			
			logger.debug("Mapping attributes for " + username);
			
			List<SecurityQuestion> securityQuestions = new ArrayList<SecurityQuestion>();
			
			for(int i=0;i<defaultQuestions.size();i++) {
				String securityQuestionText = defaultQuestions.get(i);
				String securityResponseAttrName = defaultResponseAttrs.get(i);
				
				Attribute securityResponseAttr = attrs.get(securityResponseAttrName);
				
				if(securityResponseAttr == null) {
					logger.warn("Default response attribute "
							+ securityResponseAttrName + " null for " 
							+ username);
					return null;
				}
				
				String securityResponseText = (String) securityResponseAttr.get();
				
				SecurityQuestion securityQuestion = new SecurityQuestion(securityQuestionText,
						securityResponseText);
				
				securityQuestions.add(securityQuestion);
			}
			
			logger.debug("Found " + securityQuestions.size() 
					+ " default security questions for " + username);
			return new SecurityChallenge(username, securityQuestions);
		}
	}

	@Override
	public boolean verifyPassword(String username, String password) {
	
		DistinguishedName dn = searchForDn(username);
		
		try {
			logger.debug("Authenticating as " + dn.encode());
			ldapContextSource.getContext(dn.encode(), password);
			return true;
		} catch(org.springframework.ldap.NamingException ex) {
			logger.debug("NamingException verifying password",ex);
			return false;
		}
	}

	public void setLdapContextSource(LdapContextSource ldapContextSource) {
		this.ldapContextSource = ldapContextSource;
	}

	public List<String> getSecurityQuestionAttrs() {
		return securityQuestionAttrs;
	}

	public void setSecurityQuestionAttrs(List<String> securityQuestionAttrs) {
		this.securityQuestionAttrs = securityQuestionAttrs;
	}

	public List<String> getSecurityResponseAttrs() {
		return securityResponseAttrs;
	}

	public void setSecurityResponseAttrs(List<String> securityResponseAttrs) {
		this.securityResponseAttrs = securityResponseAttrs;
	}

	public List<String> getDefaultQuestions() {
		return defaultQuestions;
	}

	public void setDefaultQuestions(List<String> defaultQuestions) {
		this.defaultQuestions = defaultQuestions;
	}

	public List<String> getDefaultResponseAttrs() {
		return defaultResponseAttrs;
	}

	public void setDefaultResponseAttrs(List<String> defaultResponseAttrs) {
		this.defaultResponseAttrs = defaultResponseAttrs;
	}

	public String getUsernameAttr() {
		return usernameAttr;
	}

	public void setUsernameAttr(String usernameAttr) {
		this.usernameAttr = usernameAttr;
	}

	public String getPasswordAttr() {
		return passwordAttr;
	}

	public void setPasswordAttr(String passwordAttr) {
		this.passwordAttr = passwordAttr;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isIgnorePartialResultException() {
		return ignorePartialResultException;
	}

	public void setIgnorePartialResultException(boolean ignorePartialResultException) {
		this.ignorePartialResultException = ignorePartialResultException;
	}

	public String getSearchBase() {
		return searchBase;
	}

	public void setSearchBase(String searchBase) {
		this.searchBase = searchBase;
	}
	
    private SearchControls getSearchControls() {
        final SearchControls constraints = new SearchControls();
        constraints.setSearchScope(this.scope);
        constraints.setReturningAttributes(new String[0]);
        constraints.setTimeLimit(this.timeout);
        constraints.setCountLimit(this.maxNumberResults);

        return constraints;
    }

    /**
     * Method to return the max number of results allowed.
     * @return the maximum number of results.
     */
    protected int getMaxNumberResults() {
        return this.maxNumberResults;
    }

    /**
     * Method to return the scope.
     * @return the scope
     */
    protected int getScope() {
        return this.scope;
    }

    /**
     * Method to return the timeout. 
     * @return the timeout.
     */
    protected int getTimeout() {
        return this.timeout;
    }

    public final void setScope(final int scope) {
        this.scope = scope;
    }

    /**
     * @param maxNumberResults The maxNumberResults to set.
     */
    public final void setMaxNumberResults(final int maxNumberResults) {
        this.maxNumberResults = maxNumberResults;
    }

    /**
     * @param timeout The timeout to set.
     */
    public final void setTimeout(final int timeout) {
        this.timeout = timeout;
    }
    
    public final LdapTemplate getLdapTemplate() {
    	return this.ldapTemplate;
    }

    protected final String getFilter() {
        return this.filter;
    }
    
    public final void setFilter(String filter) {
    	this.filter = filter;
    }
    
    protected final PrincipalNameTransformer getPrincipalNameTransformer() {
        return this.principalNameTransformer;
    }

	public void setPrincipalNameTransformer(
			PrincipalNameTransformer principalNameTransformer) {
		this.principalNameTransformer = principalNameTransformer;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ldapTemplate = new LdapTemplate(ldapContextSource);
		ldapTemplate.setIgnorePartialResultException(ignorePartialResultException);
	}
}
