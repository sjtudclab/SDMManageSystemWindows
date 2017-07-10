package org.dclab.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class OclValidate {
	public static final List<String> reservedWord=Arrays.asList("abstract","any","attribute","boolean","case","char"
			,"component","const","consumes","context","custom","default","double","emits","enum","eventtype","exception"
			,"factory","false","finder","fixed","float","getraises","home","import","in","inout","interface"
			,"local","long","manages","module","multiple","native","object","octet","oneway","out","primarykey"
			,"private","provides","public","publishes","raises","readonly","sequence","setraises","short","string"
			,"struct","supports","switch","true","truncatable","typedef","typeid","typeprefix","union","unsigned"
			,"uses","valuebase","valuetype","void","wchar","wstring");
	
	public static final List<String> IDLPrimitiveType = Arrays.asList("platform:String","platform:Double");
	public static final List<String> Real = Arrays.asList("Double");
	public Document read() throws DocumentException{
		SAXReader reader = new SAXReader();
		Document document = reader.read("D:\\pdm.face");
		return document;
	}
	public Document read(String fileName) throws DocumentException{
		SAXReader reader = new SAXReader();
		Document document = reader.read(fileName);
		return document;
	}
	
	public static String validateOcl(String fileName) throws DocumentException, IOException{
		OclValidate oclValidate = new OclValidate();
		SAXReader reader = new SAXReader();
		FileInputStream fileInputStream = new FileInputStream(new File(fileName));
		Document document = reader.read(fileInputStream);
		String returnCode = new String();
		returnCode += oclValidate.isUniqueName(document);
		returnCode += oclValidate.isBoundMatch(document);
		returnCode += oclValidate.isComNameConflict(document);
		returnCode += oclValidate.isHierarchyConsisten(document);
		fileInputStream.close();
		return returnCode;
	}
	
	/*
     * Every face::platform::Element in the FACE data model shall 
     * have a unique name
    */
	public String isUniqueName(Document document){
		Element root = document.getRootElement();
		String string = new String();
		List<Node> elements = document.selectNodes("DataModel/pdm//element");
		Iterator iterator = elements.iterator();
		Set<String> elementSet = new HashSet<>();
		while(iterator.hasNext()){
			Element element = (Element) iterator.next();
			if(!elementSet.add(element.attributeValue("name"))){
				string += "Element name is not unique at "+element.attributeValue("name")+";";
			}
		}
		return string;
	}
	
	/*
	 * Ensure that when an element realizes another element, the 
	 * upper and lower bounds of the realized entity match those 
	 * of the realizing entity. 
	*/
	public String isBoundMatch(Document document){
		Element root = document.getRootElement();
		String returnString = new String();
		List<Node> elements = document.selectNodes("DataModel/pdm//element[@xmi:type='platform:Entity']");
		Iterator iterator = elements.iterator();
		while (iterator.hasNext()) {
			Element element = (Element) iterator.next();
			if(element.attributeValue("realizes")!=null){
				Element element2 = (Element) document.selectSingleNode("DataModel/ldm//element[@xmi:id='"+element.attributeValue("realizes")+"']");
				List<Node> list = element.selectNodes("composition");
				List<Node> list2 = element2.selectNodes("composition");
				List<String> list3 = new ArrayList<>();
				List<String> list4 = new ArrayList<>();
				Iterator iterator2 = list.iterator();
				while(iterator2.hasNext()){
					Element element3 = (Element) iterator2.next();
					list3.add(element3.attributeValue("rolename"));
				}
				Iterator iterator3 = list2.iterator();
				while(iterator3.hasNext()){
					Element element3 = (Element) iterator3.next();
					list4.add(element3.attributeValue("rolename"));
				}
				for(String string : list3){
					if(!list4.contains(string)){
						returnString+="realized entity don't match those of the realizing entity at "+string+";";
					}
				}
			}
		}
		return returnString;
		
	}
	
	/* A platform entity composition hierarchy must be consistent 
	 * with the composition hierarchy of the logical entity 
	 * that it realizes. The platform value types must correspond
	 * with the logical measurements and information elements.
	*/
	
	public String isHierarchyConsisten(Document document){
		Element root = document.getRootElement();
		String returnString = new String();
		//face::platform::Entity
		List<Node> pEntity = document.selectNodes("DataModel/pdm//element[@xmi:type='platform:Entity']");
		Iterator iterator1 = pEntity.iterator();
		List<String> pEntityType = new ArrayList<>();
		while(iterator1.hasNext()){
			Element element = (Element) iterator1.next();
			pEntityType.add(element.attributeValue("id"));
		}
		//face::platform::IDLPrimitive
		List<String> IDLprimitive = new ArrayList<>();
		//face::platform::IDLStruct
		List<String> IDLStruct = new ArrayList<>();
		
		Node IDLNode = document.selectSingleNode("DataModel/pdm//pdm[@name='IDLPrimitives']");
		List<Node> IDLPrimitives = IDLNode.selectNodes("element");
		Iterator iterator2 = IDLPrimitives.iterator();
		String IDLStructType = "platform:IDLStruct";
		while(iterator2.hasNext()){
			Element element = (Element) iterator2.next();
			if(element.attributeValue("type").equals(IDLStructType)){
				System.out.println("here");
				IDLStruct.add(element.attributeValue("id"));
			}else {
				IDLprimitive.add(element.attributeValue("id"));
			}
		}
		
		List<Node> elements = document.selectNodes("DataModel/pdm//element[@xmi:type='platform:Entity']");
		Iterator iterator = elements.iterator();
		while(iterator.hasNext()){
			Element element = (Element) iterator.next();
			List<Node> comList = element.selectNodes("composition");
			if(comList.size()!=0){
				Iterator iterator3 = comList.iterator();
				while(iterator3.hasNext()){
					Element element1 = (Element) iterator3.next();
					List<Attribute> list = element1.attributes();
					String string = list.get(list.size()-1).getValue() ;
					if(string==null){
						returnString+="type is null at "+element1.attributeValue("rolename")+";";
					}else {
						if(pEntityType.contains(string)){
							
						}
						else if (IDLprimitive.contains(string)) {
							
						}else if (IDLStruct.contains(string)) {
							
						}else {
							returnString+="entity composition hierarchy is not consistent at "+element1.attributeValue("rolename")+";";
						}
					}
				}
			}
		}
		return returnString;
	}
	
	/*
	 * Ensure that composition rolename does not conflict with
	 * a reserved word in IDL or FACE supported programming
	 * language.
	*/
	public String isComNameConflict(Document document){
		String returnString = new String();
		List<Node> elements = document.selectNodes("DataModel/pdm//composition[@xmi:type='platform:Composition']");
		Iterator iterator = elements.iterator();
		while(iterator.hasNext()){
			Element element = (Element) iterator.next();
			if(reservedWord.contains(element.attributeValue("rolename").toLowerCase())){
				returnString+="composition rolename conflict with reserved word at "+element.attributeValue("rolename")+";";
			}
		}
		return returnString;
	}
	
	/* 
     * Every face::platform::Composition within the scope
     * of an Entity must have a unique name
    */
	public void isComNameUnique(Document document){
		List<Node> elements = document.selectNodes("DataModel/pdm//element[@xmi:type='platform:Entity']");
		Iterator iterator = elements.iterator();
		while(iterator.hasNext()){
			Element element = (Element) iterator.next();
			List<Node> composition = element.selectNodes("composition");
			if(composition.size()>1){
				Iterator iterator2 = composition.iterator();
				Set<String> set = new HashSet<>();
				while(iterator2.hasNext()){
					Element element1 = (Element) iterator2.next();
					if(!set.add(element1.attributeValue("rolename"))){
						System.out.println("composition of "+element.attributeValue("name")+" don't have a unique name at "+element1.attributeValue("rolename"));
					}
				}
			}
		}
	}
	
	/*
	 * Ensure that entity name does not conflict with
	 * a reserved word in IDL or FACE supported programming
	 * language.
	*/
	public void isEntityNameConflict(Document document){
		List<Node> elements = document.selectNodes("DataModel/pdm//element[@xmi:type='platform:Entity']");
		Iterator iterator = elements.iterator();
		while(iterator.hasNext()){
			Element element = (Element) iterator.next();
			if(reservedWord.contains(element.attributeValue("name").toLowerCase())){
				System.out.println("Entity name conflict with reserved word at "+element.attributeValue("name"));
			}
		}
	}
	
	/*
	 * Ensure that view name does not conflict with
	 * a reserved word in IDL or FACE supported programming
	 * language.
	*/
	public void isViewNameConflict(Document document){
		List<Node> elements = document.selectNodes("DataModel/pdm//element[@xmi:type='platform:View']");
		Iterator iterator = elements.iterator();
		while(iterator.hasNext()){
			Element element = (Element) iterator.next();
			if(reservedWord.contains(element.attributeValue("name").toLowerCase())){
				System.out.println("view name conflict with reserved word at "+element.attributeValue("name"));
			}
		}
	}
	
	/* 
     * CharacteristicProjection must have a valid path format.
    */
	public void isChaProValidPath(Document document){
		List<Node> elements = document.selectNodes("DataModel/pdm//characteristic[@xmi:type='platform:CharacteristicProjection']");
		Iterator iterator = elements.iterator();
		while(iterator.hasNext()){
			Element element = (Element) iterator.next();
			if(element.attributeValue("path")!=null){
				String pathTmp1 = element.attributeValue("path").replaceAll("\\.[_a-zA-Z0-9]+", "");
				String pathTmp2 = pathTmp1.replaceAll("->[_a-zA-Z0-9]+\\[[_a-zA-Z0-9]+\\]", "");
				if(pathTmp2.length()!=0){
					System.out.println("path format of ");
				}
			}
		}
	}
	/*
	 * Ensure that characteristic projection rolename does 
	 * not conflict with a reserved word in IDL or FACE 
	 * supported programming language.
	*/
	public void isChaProNameConflict(Document document) {
		List<Node> elements = document.selectNodes("DataModel/pdm//characteristic[@xmi:type='platform:CharacteristicProjection']");
		Iterator iterator = elements.iterator();
		while(iterator.hasNext()){
			Element element = (Element) iterator.next();
			if(reservedWord.contains(element.attributeValue("rolename").toLowerCase())){
				System.out.println("characteristic projection rolename conflict with reserved word at "+element.attributeValue("rolename"));
			}
		}
	}
	
	/* A platform idl struct composition hierarchy must be consistent 
	 * with the composition hierarchy of the logical element 
	 * that it realizes. The platform value types must correspond
	 * with the logical measurements and information elements.
	*/
	public void isIDLComConsistent(Document document) {
		Element IDLPrimitives = (Element) document.selectSingleNode("DataModel/pdm//pdm[@name='IDLPrimitives']");
		List<Node> elements = IDLPrimitives.selectNodes("element");
		Iterator iterator = elements.iterator();
		while(iterator.hasNext()){
			Element element = (Element) iterator.next();
			String realizes = element.attributeValue("realizes");
			Element realizedComposition = (Element) document.selectSingleNode("DataModel/ldm//element[@xmi:id='"+realizes+"']");
			String type = element.attributeValue("type");
			if(type==null){
				System.out.println("type of IDLPrimitives is absence at "+element.attributeValue("name"));
			}else{
				if(IDLPrimitiveType.contains(type)){
					String idlType = type.substring(type.indexOf(":")+1);
					if(Real.contains(idlType)){
						idlType = "Real";
					}
					if(realizedComposition.attributeValue("measurementAxis")!=null){
						String measurementAxisId = realizedComposition.attributeValue("measurementAxis");
						Element measurementAxis = (Element) realizedComposition.getParent().selectSingleNode("element[@xmi:id='"+measurementAxisId+"']");
						String measurementSystemAxisId = measurementAxis.attributeValue("measurementSystemAxis");
						Element measurementSystemAxis = (Element) measurementAxis.getParent().selectSingleNode("element[@xmi:id='"+measurementSystemAxisId+"']");
						String defaultValueTypeUnit = measurementSystemAxis.attributeValue("defaultValueTypeUnit");
						Element valueTypeUnit = (Element) measurementSystemAxis.getParent().selectSingleNode("element[@xmi:id='"+defaultValueTypeUnit+"']");
						String valueType = valueTypeUnit.attributeValue("valueType");
						Element valueElement = (Element) document.selectSingleNode("DataModel/ldm//element[@xmi:id='"+valueType+"']");
						String logicalType = valueElement.attributeValue("name");
						if(!idlType.equals(logicalType)){
							System.out.println("platform idl struct composition hierarchy is not consistent with logical at "+element.attributeValue("name"));
						}
					}else if (realizedComposition.attributeValue("measurementAxis")!=null) {
						
					}

				}
			}
		}
	}
	
	
	
	public static void main(String[] args) throws DocumentException {
		OclValidate oclValidate = new OclValidate();
		Document document = oclValidate.read();
		oclValidate.isIDLComConsistent(document);
	}
}
