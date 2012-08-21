package net.unicon.cas.passwordmanager.flow.model;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;

public class NetIdBean implements Serializable {

    private final Log logger = LogFactory.getLog(this.getClass());
	private static final long serialVersionUID = 1L;
	private String netId;

	public String getNetId() {
		return netId;
	}

	public void setNetId(String netId) {
		this.netId = netId;
	}
	
	public void validateForgotPassword(ValidationContext context) {
		MessageContext messageContext = context.getMessageContext();
		
    	if(netId == null || netId.trim().isEmpty()) {
			messageContext.addMessage(new MessageBuilder().error().source("netId")
					.code("pm.form.netid.error.empty")
					.defaultText("Please enter your NetID (default)")
					.build());
			logger.error("NetID was null or empty: " + netId);
    	}
	}
}
