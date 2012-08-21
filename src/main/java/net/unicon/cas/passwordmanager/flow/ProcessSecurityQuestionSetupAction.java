package net.unicon.cas.passwordmanager.flow;

import java.util.List;
import java.util.ArrayList;

import net.unicon.cas.passwordmanager.service.PasswordManagerService;
import net.unicon.cas.passwordmanager.flow.model.SecurityQuestionBean;
import org.springframework.binding.message.MessageContext;

/**
 * <p>Sets up the user's security questions.</p>
 */
public class ProcessSecurityQuestionSetupAction {
	
	private PasswordManagerService passwordManagerService; 

    public boolean setSecurityQuestion(String username, SecurityQuestionBean securityQuestion,
    		MessageContext context) throws Exception {
    	
    	if(username == null || username.isEmpty()) {
    		throw new IllegalStateException("Username is null.");
    	}
    	
    	List<SecurityQuestion> securityQuestions = new ArrayList<SecurityQuestion>();
    	securityQuestions.add(new SecurityQuestion(securityQuestion.getQuestionText(),securityQuestion.getResponseText()));
    	SecurityChallenge securityChallenge = new SecurityChallenge(username,securityQuestions);

    	passwordManagerService.setUserSecurityChallenge(username, securityChallenge);

    	return true;
    }

	public void setPasswordManagerService(
			PasswordManagerService passwordManagerService) {
		this.passwordManagerService = passwordManagerService;
	}
}
