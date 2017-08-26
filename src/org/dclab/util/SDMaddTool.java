package org.dclab.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.springframework.stereotype.Service;
@Service
public class SDMaddTool {
	public static String dir = System.getProperty("project.root") + "files"+File.separator+"SDMFile"+File.separator+"SDM.face";
	//public static String dir = "D://sdm.face";
	
	public static int addSDM(String content,String middleclass,String smallclass) throws DocumentException, IOException{
		 SAXReader reader = new SAXReader();           
	     Document   document = reader.read(new File(dir));
	     Element root = document.getRootElement();
	     if(!root.getName().equals("DataModel")){
	    	 System.out.println("根节点不对");
	    	 System.out.println("name:"+root.getName());
	    	 return 0;
	     }
	     for(Iterator it=root.elementIterator();it.hasNext();){
	    	 Element element = (Element) it.next();
	    	 
	    	 System.out.println("e name :"+element.attributeValue("name"));
	    	 if(element.attributeValue("name").equals(middleclass)){
	    		 for(Iterator iterator = element.elementIterator();iterator.hasNext();){
	    			 Element element2 = (Element) iterator.next();
	    			 if(element2.attributeValue("name").equals(smallclass)){
	    				 element2.addText(content);
	    				 Writer fiWriter = new FileWriter(dir);
	    				 /*OutputFormat format = OutputFormat.createPrettyPrint();
	    				 format.setEncoding("gb2312");*/
	    				 XMLWriter xmlWriter = new XMLWriter(fiWriter);
	    				 xmlWriter.write(document);
	    				 xmlWriter.flush();
	    				 xmlWriter.close();
	    				 System.out.println("add success");
	    			 }
	    		 }
	    	 }
	     }
	     return 1;
	}
	public static void main(String[] args) throws DocumentException, IOException {
		String content = "<element xmi:type=\"logical:Enumerated\" xmi:id=\"EAID_B5C091B5_8C32_4e6a_B9D9_9114CE1D748B\" name=\"Enumerated\" description=\"A type used to represent a value that is selected from distinct, predefined set of possible values\" standardReference=\"\"><label xmi:type=\"logical:EnumerationLabel\" xmi:id=\"EAID_2452D895_DC9D_4226_9D23_224053B492CB\" name=\"DEFAULT_ENUMERATION_LABEL\"/></element>";
		String middleclass = "Logical_Model";
		String smallclass = "Enumerations";
		addSDM(content, middleclass, smallclass);
	}
}
