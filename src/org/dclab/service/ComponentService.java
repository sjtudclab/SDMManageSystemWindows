package org.dclab.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.dclab.util.ZipTool;
import org.springframework.stereotype.Service;

@Service
public class ComponentService {

	public ComponentService() {
		// TODO Auto-generated constructor stub

	}
	public void getIntegration(String TSStype,String MessageType,String SourceComponent,String DestinationConponent,
			String SourceComponentType,String DestinationConponentType,String IOname,String IOtype,HttpServletResponse response) throws InterruptedException, IOException{	
		String dir = System.getProperty("project.root") + "files";
		String dir1 = dir + File.separator + "newDemo_TMP";
		File fileDir=new File(dir1);
		deleteDir(fileDir);
		String filename = "Demo.zip";
		response.setHeader("content-disposition", "attachment;filename="  
                + URLEncoder.encode(filename, "UTF-8"));
		ZipTool.unzip(dir+File.separator+"newDemo_TMP.zip","newDemo_TMP");
		fileDir=new File(dir1);
		Process process = null;
		String command1 = "python3 " + "cg.py "+ DestinationConponent+" "+SourceComponent+" "+MessageType+" "+TSStype+" "+IOname+" "+IOtype;
		System.out.println("command1:" + command1);
		process = Runtime.getRuntime().exec(command1, null, new File(dir1));
		process.waitFor();
		File file = new File(dir1);
		if (file.isDirectory()) {
			String src = dir1;
			String dst = dir + File.separator + "Demo.zip";
			ZipTool.compress(src, dst);
			try {
				// get your file as InputStream
				InputStream is = new FileInputStream(new File(dst));
				org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
				response.flushBuffer();
				is.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		deleteDir(fileDir);
	}

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    private static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

}
