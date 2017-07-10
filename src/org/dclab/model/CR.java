package org.dclab.model;


public class CR {
	private int CRID;
	private int eleID;
	private String state;
	private int vote;
	private String votor;
	private int approvalNum;
	private int disapprovalNum;
	private int abstainNum;
	private String reason;
	
	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getVote() {
		return vote;
	}


	public void setVote(int vote) {
		this.vote = vote;
	}

	public String getVotor() {
		return votor;
	}

	public void setVotor(String votor) {
		this.votor = votor;
	}

	public int getCRID() {
		return CRID;
	}

	public void setCRID(int cRID) {
		CRID = cRID;
	}

	public int getEleID() {
		return eleID;
	}

	public void setEleID(int eleID) {
		this.eleID = eleID;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getApprovalNum() {
		return approvalNum;
	}

	public void setApprovalNum(int approvalNum) {
		this.approvalNum = approvalNum;
	}

	public int getDisapprovalNum() {
		return disapprovalNum;
	}

	public void setDisapprovalNum(int disapprovalNum) {
		this.disapprovalNum = disapprovalNum;
	}

	public int getAbstainNum() {
		return abstainNum;
	}

	public void setAbstainNum(int abstainNum) {
		this.abstainNum = abstainNum;
	}

	public CR() {
		// TODO Auto-generated constructor stub
		this.approvalNum=0;
		this.abstainNum=0;
		this.disapprovalNum=0;
	}

}
