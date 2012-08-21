package net.unicon.cas.passwordmanager.flow.validator;

import net.unicon.cas.passwordmanager.flow.SecurityQuestion;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class SecurityQuestionValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		
		return SecurityQuestion.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		
		SecurityQuestion securityQuestion = (SecurityQuestion) target;

		String question = securityQuestion.getQuestionText();
		
		if(question == null || question.trim().isEmpty()) {
			errors.rejectValue(question,"pm.form.security-question.error.empty","Please enter a security question (default).");
		}
		
		String response = securityQuestion.getResponseText();
		
		if(response == null || response.trim().isEmpty()) {
			errors.rejectValue(response,"pm.form.security-response.error.empty","Please enter a response (default).");
		}
	}
}
