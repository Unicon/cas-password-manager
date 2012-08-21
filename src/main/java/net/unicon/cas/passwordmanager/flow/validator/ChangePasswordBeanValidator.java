package net.unicon.cas.passwordmanager.flow.validator;

import net.unicon.cas.passwordmanager.flow.model.ChangePasswordBean;
import org.springframework.binding.message.MessageBuilder;
import org.springframework.binding.message.MessageContext;
import org.springframework.binding.validation.ValidationContext;

public class ChangePasswordBeanValidator {

	// default regex accepts any password
	private String passwordRegex = ".*";
	
	public void validateChangePasswordView(ChangePasswordBean changePasswordBean,
			ValidationContext context) {
		
		MessageContext messageContext = context.getMessageContext();
		String oldPassword = changePasswordBean.getOldPassword();
		String newPassword = changePasswordBean.getNewPassword();
		String confirmNewPassword = changePasswordBean.getConfirmNewPassword();

		// we'll validate the username in the action object
		
		if(oldPassword == null || oldPassword.isEmpty()) {
			messageContext.addMessage(new MessageBuilder().error().source("oldPassword")
					.code("cas.pm.oldpassword.empty")
					.defaultText("Please enter your current password")
					.build());
		}
		
		if(newPassword == null || newPassword.isEmpty()) {
			messageContext.addMessage(new MessageBuilder().error().source("newPassword")
					.code("cas.pm.newpassword.empty")
					.defaultText("Please enter a new password")
					.build());
		} else {
			if(newPassword.equals(oldPassword)) {
				messageContext.addMessage(new MessageBuilder().error().source("newPassword")
						.code("cas.pm.newpassword.same")
						.defaultText("The new password must be different")
						.build());
			}
			if(!newPassword.matches(passwordRegex)) {
				messageContext.addMessage(new MessageBuilder().error().source("newPassword")
						.code("cas.pm.newpassword.weak")
						.defaultText("The password is too weak")
						.build());
			} else if(confirmNewPassword == null || !confirmNewPassword.equals(newPassword)) {
				messageContext.addMessage(new MessageBuilder().error().source("confirmNewPassword")
						.code("cas.pm.newpassword.mismatch")
						.defaultText("The passwords do not match")
						.build());
			}
		}
	}

	public void validateSetPassword(ChangePasswordBean changePasswordBean,
			ValidationContext context) {
		
		MessageContext messageContext = context.getMessageContext();
		String newPassword = changePasswordBean.getNewPassword();
		String confirmNewPassword = changePasswordBean.getConfirmNewPassword();
		
		if(newPassword == null || newPassword.isEmpty()) {
			messageContext.addMessage(new MessageBuilder().error().source("newPassword")
					.code("cas.pm.newpassword.empty")
					.defaultText("Please enter a new password")
					.build());
		} else {
			if(!newPassword.matches(passwordRegex)) {
				messageContext.addMessage(new MessageBuilder().error().source("newPassword")
						.code("cas.pm.newpassword.weak")
						.defaultText("The password is too weak")
						.build());
			} else if(confirmNewPassword == null || !confirmNewPassword.equals(newPassword)) {
				messageContext.addMessage(new MessageBuilder().error().source("confirmNewPassword")
						.code("cas.pm.newpassword.mismatch")
						.defaultText("The passwords do not match")
						.build());
			}
		}
	}
	
	public void setPasswordRegex(String passwordRegex) {
		this.passwordRegex = passwordRegex;
	}
}
