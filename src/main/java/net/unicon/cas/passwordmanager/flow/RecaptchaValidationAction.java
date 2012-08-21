package net.unicon.cas.passwordmanager.flow;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

public class RecaptchaValidationAction {

	private final Log logger = LogFactory.getLog(this.getClass());
	private String recaptchaPrivateKey;
	private String recaptchaPublicKey;
	
	public boolean validateCaptcha(RequestContext context) {

		ServletExternalContext externalContext = (ServletExternalContext) context.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) externalContext.getNativeRequest();
	    String remoteAddr = request.getRemoteAddr();
	    ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
	    reCaptcha.setPrivateKey(recaptchaPrivateKey);

	    String challenge = request.getParameter("recaptcha_challenge_field");
	    String uresponse = request.getParameter("recaptcha_response_field");
	    ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

	    boolean result = reCaptchaResponse.isValid();
	    
	    logger.debug("reCaptcha response validity: " + result);
	    
	    return result;
	}
	
	public void setRecaptchaPublicKey(String recaptchaPublicKey) {
		this.recaptchaPublicKey = recaptchaPublicKey;
	}

	public String getRecaptchaPublicKey() {
		return recaptchaPublicKey;
	}

	public void setRecaptchaPrivateKey(String recaptchaPrivateKey) {
		this.recaptchaPrivateKey = recaptchaPrivateKey;
	}

	public String getRecaptchaPrivateKey() {
		return recaptchaPrivateKey;
	}
}
