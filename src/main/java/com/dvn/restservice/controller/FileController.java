


/*package com.dvn.restservice.controller;

import java.io.File;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dvn.util.FileUtil;
@RestController
public class FileController {
	
	  private String path   ="C://Users//DVN//Desktop//";
		
	  private String fileName="";
	  private String fileType="";
	//@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public String FileUpload( @RequestParam("file") MultipartFile file)
	{		if(file!=null)
	{
		fileName=file.getOriginalFilename();
		fileType=FileUtil.getFileExtention(fileName);
		//FileUtil.upload(path, file, ""+fileName,"",this);
		return "Sucess";
	}
	else{
		return "ERROR IN FILE";
	}
	}
	
	
	
			 

}
*/