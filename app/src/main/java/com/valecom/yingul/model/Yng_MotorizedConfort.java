package com.valecom.yingul.model;

import java.io.Serializable;

public class Yng_MotorizedConfort implements Serializable {
    private long motorizedConfortId;
    private Yng_Motorized motorized;
    private Yng_Confort confort;

	public Yng_MotorizedConfort() {
		super();
	}

	public long getMotorizedConfortId() {
		return motorizedConfortId;
	}

	public void setMotorizedConfortId(long motorizedConfortId) {
		this.motorizedConfortId = motorizedConfortId;
	}

	public Yng_Motorized getMotorized() {
		return motorized;
	}

	public void setMotorized(Yng_Motorized motorized) {
		this.motorized = motorized;
	}

	public Yng_Confort getConfort() {
		return confort;
	}

	public void setConfort(Yng_Confort confort) {
		this.confort = confort;
	}
}
