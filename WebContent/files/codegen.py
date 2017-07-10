import xml.etree.ElementTree as ET
import os

tree = ET.parse('face.xml')
root = tree.getroot()

includeTmp = '#include "name.hpp"'
typedefTmp = '        typedef FACE::vtype vname;'
defTmp = '            FACE::DM::vtype vname;'
command = 'mkdir DM'				
os.system(command)
for pdm in root:
	if pdm.tag == 'pdm':
		for element in pdm.iter('element'):
			if len(list(element)) != 0:
				flag = False
				name = element.attrib['name'];				
				attr = {}
				for comp in element.iter('composition'):
					attrName = comp.attrib['rolename']
					attrId = comp.attrib['type'];
					for ele in pdm.iter('element'):
						if ele.attrib['{http://www.omg.org/XMI}id'] == attrId:
							attrType = ele.attrib['name'];
					attr[attrName] = attrType;
				
				fileName = name + '.hpp'
				upperFileName = name.upper()
				tmpFile = open('structTemp.hpp', 'r')
				dstFile = open('DM/' + fileName, 'w')

				for line in tmpFile.readlines():
					wline = line
					if line.find('hname') > 0:
						wline = line.replace('hname', upperFileName)						
					if line.find('sname') > 0:
						wline = line.replace('sname', name)

					dstFile.write(wline)
					if wline.find('define') > 0:
						dstFile.write('\n')
						for key in attr:
							iline = includeTmp.replace('name', attr[key])
							dstFile.write(iline + '\n')
					if flag:
						for key in attr:
							dline = defTmp.replace('vtype', attr[key]).replace('vname', key)
							dstFile.write(dline + '\n')
						flag = False
					if wline.find('struct') > 0:
						flag = True

				tmpFile.close()
				dstFile.close()
			else:
				flag = False
				name = element.attrib['name'];
				attrType = element.attrib['{http://www.omg.org/XMI}type']
				fileName = name + '.hpp'
				upperFileName = name.upper()
				tmpFile = open('typedefTemp.hpp', 'r')
				dstFile = open('DM/' + fileName, 'w')

				for line in tmpFile.readlines():
					wline = line
					if line.find('hname') > 0:
						wline = line.replace('hname', upperFileName)						

					dstFile.write(wline)
					if wline.find('define') > 0:
						dstFile.write('\n')							
						if attrType == 'platform:String':
							dstFile.write('#include "String_var.hpp"')
					if flag:
						if attrType == 'platform:String':
							vtype = 'String_var'
						if attrType == 'platform:Double':
							vtype = 'Double'	
						dline = typedefTmp.replace('vtype', vtype).replace('vname', name)
						dstFile.write(dline + '\n')
						flag = False
					if wline.find('namespace DM') > 0:
						flag = True					
				tmpFile.close()
				dstFile.close()

command = 'tar -zcvf DM.tar.gz DM'				
os.system(command)
command = 'rm -rf DM'				
os.system(command)

