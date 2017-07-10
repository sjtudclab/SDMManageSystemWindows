package org.dclab.model;

import java.util.List;

public class Model {

	private int elementID;
	private String EnglishName;
	private String ChineseName;
	private String description;
	private String fileID;
	private int smallClass;
	private int middleClass;
	private int bigClass;	
	private String creator;
	private String createTime;
	private int state;  //记录元素状态 0表示未审核，1表示通过，-1表示未通过
	private List<CR> crs;
	private int approvalNum;  //统计元素通过的票数
	private int disapprovalNum; //统计元素不通过的票数
	private int abstainNum;  //统计元素弃权的票数
	private String reason;  //表示审核员们给出的建议或理由
    private int vote;     //CCB的投票
    
	public int getVote() {
		return vote;
	}

	public void setVote(int vote) {
		this.vote = vote;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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


	public int getElementID() {
		return elementID;
	}

	public List<CR> getCrs() {
		return crs;
	}

	public void setCrs(List<CR> crs) {
		this.crs = crs;
	}

	public void setElementID(int elementID) {
		this.elementID = elementID;
	}

	public String getEnglishName() {
		return EnglishName;
	}

	public void setEnglishName(String englishName) {
		EnglishName = englishName;
	}

	public String getChineseName() {
		return ChineseName;
	}

	public void setChineseName(String chineseName) {
		ChineseName = chineseName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFileID() {
		return fileID;
	}

	public void setFileID(String fileID) {
		this.fileID = fileID;
	}

	public int getSmallClass() {
		return smallClass;
	}

	public void setSmallClass(int smallClass) {
		this.smallClass = smallClass;
	}

	public int getMiddleClass() {
		return middleClass;
	}

	public void setMiddleClass(int middleClass) {
		this.middleClass = middleClass;
	}

	public int getBigClass() {
		return bigClass;
	}

	public void setBigClass(int bigClass) {
		this.bigClass = bigClass;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Model() {
		// TODO Auto-generated constructor stub
		vote=2;
	}

}
