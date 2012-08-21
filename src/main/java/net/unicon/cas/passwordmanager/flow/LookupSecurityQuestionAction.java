package net.unicon.cas.passwordmanager.flow;

import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import net.unicon.cas.passwordmanager.PasswordManagerException;
import net.unicon.cas.passwordmanager.service.PasswordManagerService;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.core.collection.MutableAttributeMap;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * <p>Checks if the user has set up their security questions and puts them
 * in the flow scope.</p>
 */
public class LookupSecurityQuestionAction extends AbstractAction {
    
    public static final String SECURITY_CHALLENGE_ATTRIBUTE = "securityChallenge";
	private PasswordManagerService passwordManagerService;
	private boolean customSecurityQuestionRequired = false;

    @Override
    protected Event doExecute(RequestContext requestContext) throws Exception {
    	
    	// If we don't want users to set their own security questions,
    	// bypass this action.
    	if(!customSecurityQuestionRequired) {
    		return success();
    	}
    	
    	MutableAttributeMap flowScope = requestContext.getFlowScope();
    	Credentials creds = (Credentials)flowScope.get("credentials");
    	String username = null;
    	
    	if(!(creds instanceof UsernamePasswordCredentials)) {
    		// see if the credentials are in the username flow scope object
    		if(flowScope.getString("username") != null) {
    			username = flowScope.getString("username");
    		} else {
    			throw new PasswordManagerException("No username found trying to look up security questions.");
    		}
    	} else {
    		UsernamePasswordCredentials upCreds = (UsernamePasswordCredentials) creds;
    		username = upCreds.getUsername();
    		flowScope.put("username", username);
    	}

    	SecurityChallenge securityChallenge = passwordManagerService.getUserSecurityChallenge(username);
    	
    	if(securityChallenge == null) {
    		// user doesn't have security questions set up
    		logger.debug("No security questions for " + username);
    		return error();
    	} else {
    		logger.debug("Putting security questions in flow scope for " + username);
            flowScope.put(SECURITY_CHALLENGE_ATTRIBUTE, securityChallenge);
    	}
    	
        // user has set up their security questions
        return success();  	
    }

    /**
     * <p>Determines if users are required to set custom security questions.</p>
     * @param customSecurityQuestionRequired
     */
	public void setCustomSecurityQuestionRequired(
			boolean customSecurityQuestionRequired) {
		this.customSecurityQuestionRequired = customSecurityQuestionRequired;
	}

	public void setPasswordManagerService(
			PasswordManagerService passwordManagerService) {
		this.passwordManagerService = passwordManagerService;
	}

}
