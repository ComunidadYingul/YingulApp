package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_ResetPassword implements Serializable {
    private Long resetpasswordId;
	private int codeResetPassword;
    private Yng_User user;
	
    public Yng_ResetPassword() {
    	super();
    }

	public Long getResetpasswordId() {
		return resetpasswordId;
	}

	public void setResetpasswordId(Long resetpasswordId) {
		this.resetpasswordId = resetpasswordId;
	}

	public int getCodeResetPassword() {
		return codeResetPassword;
	}

	public void setCodeResetPassword(int codeResetPassword) {
		this.codeResetPassword = codeResetPassword;
	}

	public Yng_User getUser() {
		return user;
	}

	public void setUser(Yng_User user) {
		this.user = user;
	}
}
