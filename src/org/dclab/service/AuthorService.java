package org.dclab.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dclab.mapping.AuthorMapperI;
import org.dclab.model.ModifyReceive;
import org.dclab.model.UserAuthor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorService {
	
	@Autowired
	private AuthorMapperI authorMapperI;
	
	
	
	public Object initial(){
		
		List<String> aList = new ArrayList<>();
		aList.add("createModel");
		aList.add("viewModel");
		aList.add("verifyModel");
		aList.add("manage");
		aList.add("vote");
		
		List<UserAuthor> list = new ArrayList<>();
		List<String> role = authorMapperI.getRole();
		for(String string : role){
			int rid = authorMapperI.getRidByRoleName(string);
			List<String> userAuthor = authorMapperI.getUserAuthor(rid);
			System.out.println("asd"+userAuthor.toString());
			UserAuthor userAuthor2 = new UserAuthor();
			userAuthor2.setRoleId(rid);
			userAuthor2.setRoleName(string);
			Map<String, Integer> map2 = new HashMap<>();
			for(String string2 : userAuthor){
				if (aList.contains(string2)) {
					map2.put(string2, 1);
				}
			}
			userAuthor2.setAuthority(map2);
			list.add(userAuthor2);
		}
		return list;
	}
	
	public Object role() {
		List<String> list = authorMapperI.getRole();
		return list;
	}
	
	public void authorModify(ModifyReceive mReceive){
		authorMapperI.deleteauthor(mReceive.getRoleId());
		String author;
		if(mReceive.getCreateModel()==1){
			author = "createModel";
			authorMapperI.insert(author, mReceive.getRoleId());
		}
		if(mReceive.getManage()==1){
			author = "manage";
			authorMapperI.insert(author, mReceive.getRoleId());
		}
		if(mReceive.getVerifyModel()==1){
			author = "verifyModel";
			authorMapperI.insert(author, mReceive.getRoleId());
		}
		if(mReceive.getViewModel()==1){
			author = "viewModel";
			authorMapperI.insert(author, mReceive.getRoleId());
		}
		if(mReceive.getVote()==1){
			author = "vote";
			authorMapperI.insert(author, mReceive.getRoleId());
		}
	}
}
