package com.dvn.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import com.dvn.restservice.controller.UserRestController;

public class FileUtil {
	
	private static Logger log = LoggerFactory.getLogger(FileUtil.class);
	//private static String workingDir = System.getProperty("user.dir");//D:\Softwares\IDE\eclipse-jee-neon-RC3-win32-x86_64\eclipse
	private static String LOCAL_PATH="http://localhost:8083/";
	
	public final static String SERVER_PATH ="http://localhost:8083/";
	private static String SERVER_IMAGE_PATH="/restUploads/data";
	//public static String temp="C:\Program Files\Apache Software Foundation\Tomcat 8.0\RestServices";
	public static final String ABS_PATH="D:\\restUploads\\";
	public static final String data_dir="data\\";
	public static final String json_dir="jsonDir\\";
	private static String setFileNameAtServer="";
	private static String realFileName="";
	//private static final String ABS_PATH=SERVER_PATH+"RestServices/resources/images/";
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-ms");
	//@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public static JSONObject upload(String path,MultipartFile file, String fileName,String fileType,Object mContext,String user_id) {
		
		if (!file.isEmpty()) {
			try {
				
				UserRestController context=(UserRestController)mContext;
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				String serverTime=sdf.format(timestamp);
				setFileNameAtServer=serverTime+"."+fileType;
				
			byte[] bytes = file.getBytes();

			
			File dir = new File(path);
			if (!dir.exists())
				dir.mkdirs(); //Make/create directory
			
			
			File serverFile = new File(dir.getAbsolutePath()
					+ File.separator + setFileNameAtServer);
			
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(serverFile));
			stream.write(bytes);
			stream.close();

			log.info("Server File Location="
					+ serverFile.getAbsolutePath());
			
			JSONObject obj =context.setUserDataInfo(user_id,fileName,setFileNameAtServer,serverTime);
				return obj;
			} catch (Exception e) {
			e.printStackTrace();
		}
			
		}
		return null;
		
		

	}
	
public static JSONObject uploadJson(String path,MultipartFile file, String fileName,String fileType,Object mContext,String user_id) {
		
		if (!file.isEmpty()) {
			try {
				
				UserRestController context=(UserRestController)mContext;
				
			byte[] bytes = file.getBytes();

			
			File dir = new File(path);
			if (!dir.exists())
				dir.mkdirs(); //Make/create directory
			
			
			File serverFile = new File(dir.getAbsolutePath()
					+ File.separator + fileName);
			
			BufferedOutputStream stream = new BufferedOutputStream(
					new FileOutputStream(serverFile));
			stream.write(bytes);
			stream.close();

			log.info("Server File Location="
					+ serverFile.getAbsolutePath());
			
			JSONObject obj =context.setUserJsonInfo(user_id);
				return obj;
			} catch (Exception e) {
			e.printStackTrace();
		}
			
		}
		return null;
		
		

	}
	public static String getFileExtention(String fullName){
		String filename=new File(fullName).getName();
		int dotIndex=filename.lastIndexOf('.');
		return (dotIndex== -1)?"":filename.substring(dotIndex+1);
	}
	public static void uploadFile(HttpServletRequest request, MultipartFile file, String code) {
		
		log.debug("Starting of the method upload");
		LOCAL_PATH=request.getSession().getServletContext().getRealPath("/resources/images/");
		log.info("REAL_PATH");
		//log.debug("Current Path :" + Paths.get("").toFile()	);
		
		
		if(!new File(ABS_PATH).exists())
		{
			new File(ABS_PATH).mkdirs();
		}
		
		if(!new File(LOCAL_PATH).exists())
		{
			new File(LOCAL_PATH).mkdirs();
		}
		
		try{
			file.transferTo(new File("REAL_PATH" + file ));
			file.transferTo(new File("ABS_PATH" + file ));
		}
		catch(IOException ex){
			
		}
		log.debug("Ending of the method upload");
	}


	
	
	public static boolean removeFile(String fileName)
	{
		try{

    		File file = new File(ABS_PATH+data_dir+fileName);
    			if(file.exists())
    			{		
    		if(file.delete()){
    			System.out.println(file.getName() + " is deleted!");
    			return true;
    		}else{
    			System.out.println("Delete operation is failed.");
    			//return false;
    		}
    			}

    	}catch(Exception e){

    		e.printStackTrace();
    		

    	}
		return false;
		
	}
	
	
	
	
	
	
	
	
	
	//using nio
	public void fileCopy(String src, String dest)
	{
		
	Path sourcePath=	  Paths.get(src);
	Path destinationPath = Paths.get(dest);
	
	
	try {
		Files.copy(sourcePath, destinationPath);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
			
			
			
	}
	
	
}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
