package net.unicon.cas.passwordmanager.flow.model;

import java.io.Serializable;

import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;

public class SecurityQuestionBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String questionText;
	private String responseText;
	
	public String getQuestionText() {
		return questionText;
	}
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}
	public String getResponseText() {
		return responseText;
	}
	public void setResponseText(String responseText) {
		this.responseText = responseText;
	}
	
	
	public void validateSetupSecurityQuestion(ValidationContext context) {
		
		MessageContext messageContext = context.getMessageContext();
		
		if(questionText == null || questionText.trim().isEmpty()) {
			messageContext.addMessage(new MessageBuilder().error().source("questionText")
					.code("pm.form.security-question.error.empty")
					.defaultText("Please enter a security question (default)")
					.build());
		}
	
		if(responseText == null || responseText.trim().isEmpty()) {
			messageContext.addMessage(new MessageBuilder().error().source("responseText")
					.code("pm.form.security-response.error.empty")
					.defaultText("Please enter a response (default)")
					.build());	
		}
	}
}
