/*package com.dvn.restservice.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.dvn.backend.model.UploadFile;
import com.dvn.backend.model.User;
import com.dvn.util.FileUtil;

@RestController
public class HOJAController {

	@Autowired
	private HOJAController UploadDAO;
        
	private User user;
	
	private UploadFile uploadfile;
	InputStream inStream = null;
	@ResponseBody
	//@RequestMapping(value = "/upload", method = RequestMethod.POST)
	 public String handleFileUpload(HttpServletRequest request,
	            @RequestParam MultipartFile[] fileUpload) throws Exception {
	          System.out.println("Coming");
	        if (fileUpload != null && fileUpload.length > 0) {
	            for (MultipartFile aFile : fileUpload){
	                  
	                System.out.println("Saving file: " + aFile.getOriginalFilename());
	                System.out.println("HHHHHHHH: " + getFileExtention(aFile.getOriginalFilename()));
	                 
//	                UploadFile uploadFile = new UploadFile();
//	                uploadFile.setFileName(aFile.getOriginalFilename());
//	                uploadFile.setData(aFile.getBytes());
//	                fileUpload.save(uploadFile);
	                File file1=new File(aFile.getOriginalFilename());
	                file1.createNewFile();
	                FileOutputStream fos = new FileOutputStream(file1);
	                fos.write(aFile.getBytes());
	                fos.close();
	                File file = new File(FileUtil.ABS_PATH +"/"+file1);
	                String fileName = FileUtil.ABS_PATH +  aFile.getOriginalFilename();
	                writeFile(aFile.getBytes(), fileName);
	              //  System.out.println("File PAth"+file.getAbsolutePath());
	               // inStream = new FileInputStream(file);
	            }
	        }
	        	
	                return "Success";
	    }  
	

    
 
    // Utility method
    private void writeFile(byte[] content, String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            System.out.println("not exist> " + file.getAbsolutePath());
            file.createNewFile();
        }
        FileOutputStream fop = new FileOutputStream(file);
        fop.write(content);
        System.out.println("ARAHA H");
        fop.flush();
        fop.close();
    }
	

public static String getFileExtention(String fullName){
	String filename=new File(fullName).getName();
	int dotIndex=filename.lastIndexOf('.');
	return (dotIndex== -1)?"":filename.substring(dotIndex+1);
}
}*/