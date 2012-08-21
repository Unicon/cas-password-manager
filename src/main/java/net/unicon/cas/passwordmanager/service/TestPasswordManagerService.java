package net.unicon.cas.passwordmanager.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.unicon.cas.passwordmanager.service.ChangePasswordResult.Result;
import net.unicon.cas.passwordmanager.flow.SecurityChallenge;
import net.unicon.cas.passwordmanager.flow.SecurityQuestion;

public class TestPasswordManagerService implements PasswordManagerService {

	private final Random random = new Random();

	@Override
	public SecurityChallenge getUserSecurityChallenge(String username) {
		
		SecurityQuestion q1 = new SecurityQuestion("Why?","Because");
		SecurityQuestion q2 = new SecurityQuestion("How old are you?","Too old");
		
		List<SecurityQuestion> securityQuestions = new ArrayList<SecurityQuestion>();
		
		securityQuestions.add(q1);
		securityQuestions.add(q2);
		
		SecurityChallenge securityChallenge = new SecurityChallenge(username, securityQuestions);
		
		return securityChallenge;		
	}
	
	@Override
	public SecurityChallenge getDefaultSecurityChallenge(String username) {
		
		SecurityQuestion q1 = new SecurityQuestion("When is your birthday? (mm/yy/dddd)","5/20/1980");
		SecurityQuestion q2 = new SecurityQuestion("What is your student/employee ID number?","12345");
		
		List<SecurityQuestion> securityQuestions = new ArrayList<SecurityQuestion>();
		
		securityQuestions.add(q1);
		securityQuestions.add(q2);
		
		SecurityChallenge securityChallenge = new SecurityChallenge(username, securityQuestions);
		
		return securityChallenge;	
	}

	@Override
	public void setUserPassword(String username, String password) {
		
		ChangePasswordResult changePasswordResult = new ChangePasswordResult();
		
		Float result = random.nextFloat();
		
		if(result < 0.5) {
			changePasswordResult.setResult(Result.SUCCESS);
			changePasswordResult.setDefaultMessage("Your password has been reset.");
			changePasswordResult.setMessageKey("setPassword.success");
		} else {
			changePasswordResult.setResult(Result.ERROR);
			changePasswordResult.setDefaultMessage("There was an error setting your new password. Please try again.");
			changePasswordResult.setMessageKey("setPassword.error");
		}
	}

	@Override
	public void changeUserPassword(String username,
			String oldPassword, String newPassword) {
		
		ChangePasswordResult changePasswordResult = new ChangePasswordResult();

		Float result = random.nextFloat();
		
		if(result < 0.33) {
			changePasswordResult.setResult(Result.SUCCESS);
			changePasswordResult.setDefaultMessage("Your password has been changed.");
			changePasswordResult.setMessageKey("changePassword.success");
		} else if(result < 0.66) {
			changePasswordResult.setResult(Result.FAILURE);
			changePasswordResult.setDefaultMessage("Your old password was incorrect.");
			changePasswordResult.setMessageKey("changePassword.incorrectOldPassword");
		} else {
			changePasswordResult.setResult(Result.ERROR);
			changePasswordResult.setDefaultMessage("There was an error changing your password. Please try again.");
			changePasswordResult.setMessageKey("changePassword.error");
		}
	}

	@Override
	public void setUserSecurityChallenge(String username,
			SecurityChallenge securityChallenge) {
		// nothing to see here
	}

}
