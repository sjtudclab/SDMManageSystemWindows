package org.dclab.zk;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class GitLabService {

	public GitLabService() {
		// TODO Auto-generated constructor stub

	}
	public void upLoad(String path) throws InterruptedException{	
		int index=path.lastIndexOf("/");
		String Directory=path.substring(0, index);  //目录
		String fileName=path.substring(index+1, path.length());  //文件名
		String shellL="#!/bin/bash"+'\n'+
				"cd "+Directory+'\n'+
				"git pull "+'\n'+
				"git add "+fileName+'\n'+
				"git commit -m \"upload a "+fileName+" file\" "+fileName+'\n'+
				"git push origin master"+'\n'+
				"echo 'pwd'";
		String shPath=System.getProperty("project.root")+"script"+File.separator+"call_mkdir.sh";
		 try {
			 	FileOutputStream fos = new FileOutputStream(shPath);
				fos.write(shellL.getBytes()); //覆盖之前文件内容
				Process process=null;
				String command1="chmod 777 "+shPath;  //设置权限
				process = Runtime.getRuntime().exec(command1);
				process.waitFor();
				String command2="/bin/sh "+shPath;
	            process = Runtime.getRuntime().exec(command2);
	            process.waitFor();
	            InputStreamReader ir = new InputStreamReader(process.getInputStream());
	            LineNumberReader input = new LineNumberReader(ir);
	            String line;
	            while((line = input.readLine()) != null){
	                System.out.println(line);
	            }
	            input.close();
	            ir.close();
	            //fos.flush();
				fos.close();
	        } catch (IOException e) {
	            // TODO: handle exception
	            e.printStackTrace();
	        }
	}
	public void download(String fileName){
		 try {
			   System.out.println("start");
			   Process pr = Runtime.getRuntime().exec("python download.py");
			   BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			   String line;
			   while ((line = in.readLine()) != null) {
				   System.out.println(line);
			   }
			   in.close();
			   pr.waitFor();
			   System.out.println("end");
		} catch (Exception e) {
			   e.printStackTrace();
		}
	}
}
