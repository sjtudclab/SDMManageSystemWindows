package org.dclab.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dclab.mapping.CRMapperI;
import org.dclab.mapping.ClassMapperI;
import org.dclab.mapping.ModelMapperI;
import org.dclab.mapping.NameContentMapperI;
import org.dclab.mapping.UserMapperI;
import org.dclab.model.CR;
import org.dclab.model.Model;
import org.dclab.model.SDMclass;
import org.dclab.util.SDMaddTool;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CRService {
	@Autowired
	private ModelMapperI modelMapperI;
	@Autowired
    private CRMapperI crMapperI;
	@Autowired
	private UserMapperI userMapperI;
	@Autowired
	private SDMaddTool SDMaddTools;
	@Autowired
	private ClassMapperI classMapperI;
	@Autowired
	private NameContentMapperI nameContentMapperI;
	private HashMap<Integer,String> nameMap=new HashMap<Integer,String>();
	
	public void setCrMapperI(CRMapperI crMapperI) {
		this.crMapperI = crMapperI;
	}
    
	public void setModelMapperI(ModelMapperI modelMapperI) {
		this.modelMapperI = modelMapperI;
	}

	public List<Model> checkCR(String userName){
		int authorityID=userMapperI.getAuthorityByUserName(userName);
		List<Model> models=new ArrayList<Model>();
		if(authorityID==0||authorityID==1||authorityID==2){
			models = modelMapperI.getModelByUserID(userName);
			if(models!=null){
				System.out.println(models.size());
				for(int i=0;i<models.size();i++){
					String reason="";
					int eleID=models.get(i).getElementID();
					List<CR> crs=crMapperI.getCRByEleID(eleID);
					for(int j=0;j<crs.size();j++)
						if(crs.get(j).getVote() == -1)
						    reason+=crs.get(j).getVotor()+":"+crs.get(j).getReason()+"\n";
					models.get(i).setCrs(crMapperI.getCRByEleID(eleID));
					models.get(i).setApprovalNum(crMapperI.getApprovalNum(eleID));
					models.get(i).setDisapprovalNum(crMapperI.getDisApprovalNum(eleID));
					models.get(i).setAbstainNum(crMapperI.getAbstainNum(eleID));
					models.get(i).setReason(reason);
				}
				return models;
			}
			return null;
		}	
		return null;
	}
	public List<Model> checkCRCCB(String userName){
		int authorityID=userMapperI.getAuthorityByUserName(userName);
		List<Model> models=new ArrayList<Model>();
		if(authorityID==1||authorityID==2){
			models = modelMapperI.getModelByBigClass(authorityID);
			if(models!=null){
				for(int i=0;i<models.size();i++){
					int eleID=models.get(i).getElementID();
					List<CR> crs=crMapperI.getCRByEleID(eleID);
					for(int j=0;j<crs.size();j++){
						//System.out.println(crs.get(j).getVotor()==userName);
						//System.out.println(crs.get(j).getEleID()==eleID);
						//System.out.println(crs.get(j).getVotor());
						//System.out.println(crs.get(j).getVote());
						if(crs.get(j).getVotor().equals(userName)&&crs.get(j).getEleID()==eleID){
							//System.out.println("aaa");
							models.get(i).setVote(crs.get(j).getVote());
						}
					}		
					models.get(i).setCrs(crMapperI.getCRByEleID(eleID));
					models.get(i).setApprovalNum(crMapperI.getApprovalNum(eleID));
					models.get(i).setDisapprovalNum(crMapperI.getDisApprovalNum(eleID));
					models.get(i).setAbstainNum(crMapperI.getAbstainNum(eleID));
				}
				return models;
			}
			return null;
		}
		else 
			return null;
	}
	
	public Model checkCRByElementID(int elementID){
		Model model = modelMapperI.getModelByEleID(elementID);
		if(model!=null){
			model.setCrs(crMapperI.getCRByEleID(elementID));
			model.setCrs(crMapperI.getCRByEleID(elementID));
			model.setApprovalNum(crMapperI.getApprovalNum(elementID));
			model.setDisapprovalNum(crMapperI.getDisApprovalNum(elementID));
			model.setAbstainNum(crMapperI.getAbstainNum(elementID));
			return model;
			}
		else
			return null;
	}
	public int vote(CR cr) throws InterruptedException, DocumentException, IOException{
		int elementID=cr.getEleID();
		if(crMapperI.getUserState(elementID,cr.getVotor())==0){
			crMapperI.insertCR(cr);
			int authorityID=userMapperI.getAuthorityByUserName(cr.getVotor());
			if(crMapperI.getCheckNum(elementID)==userMapperI.getUserNum(authorityID)){
				int state=crMapperI.getResult(elementID);
				if(state>0){ //表示审核通过
					//TODU 调用zookeeper通知用户审核情况
					getSDMclass();
					Model model=modelMapperI.getModelByEleID(elementID);
					String content=nameContentMapperI.getContentByName(model.getEnglishName());
					modelMapperI.updateContent(elementID,content);
					model.setContent(content);
					System.out.println(nameMap.get(model.getMiddleClass()));
					System.out.println(nameMap.get(model.getSmallClass()));
					SDMaddTools.addSDM(model.getContent(), nameMap.get(model.getMiddleClass()), nameMap.get(model.getSmallClass()));
					return modelMapperI.updateState(elementID,1);
				}
				else
					return modelMapperI.updateState(elementID,-1);
			}
			return -1;
		}
			
		else 
			return -1;
	}
	public void getSDMclass(){
		List<SDMclass> sdmclasses=classMapperI.getSDMClass();
		for(int i=0;i<sdmclasses.size();i++){
			this.nameMap.put(sdmclasses.get(i).getClassID(), sdmclasses.get(i).getEnglishName());
		}
	}
}
