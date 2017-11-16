

package com.dvn.restservice.controller;

import java.io.File;
import java.util.List;
import com.dvn.util.FileUtil;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.dvn.backend.dao.UserDAO;
import com.dvn.backend.model.BaseDomain;
import com.dvn.backend.model.User;
import com.dvn.util.FileUtil;

@RestController
public class UserRestController {

	
		
	  public String fileName="";
	  public String fileType="";
	@Autowired
	private User user;

	@Autowired
	private UserDAO userDAO;
	
	

	@Autowired
	private HttpSession session;
	
	private Logger log = LoggerFactory.getLogger(UserRestController.class);

	@GetMapping("/hello")
	public String sayHello()
	{
		return "  Hello from User rest service Modifed message";
	}

	@GetMapping("/user/logout")
	public void logoutUser(){
		session.invalidate();
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUser() {
		List<User> userList = userDAO.list();
		user = (User) session.getAttribute("user");
		//System.out.println("given id is "+user.getId());
		return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
	}
/*
	@GetMapping("/user/{id}")
	public ResponseEntity<User> getUser(@PathVariable("id") String id)
	{
		log.debug("**************Starting of the method getUserByID");
		log.info("***************Trying to get userdetails of the id " + id);
		user = userDAO.get(id);
		
		if(user==null)
		{
			user = new User();
			user.setErrorCode("404");
			user.setErrorMessage("User does not exist with the id :" + id);
		}
		else
		{
			user.setErrorCode("200");
			user.setErrorMessage("success");
		}
		
	//log.info("**************** Name of teh user is " + user.getName());
		log.debug("**************Ending of the method getUserByID");
	  return	new ResponseEntity<User>(user , HttpStatus.OK);
	}
	*/

	@PutMapping(value = "/user/update/")
	public ResponseEntity<User> updateUser(@RequestBody User user) {

		if (userDAO.get(user.getId()) == null) {

			user = new User(); // ?
			user.setErrorCode("404");
			user.setErrorMessage("User does not exist with id " + user.getId());
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}

		userDAO.update(user);

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}


	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> FileUpload(@RequestParam ("userId") String user_id,@RequestParam("file") MultipartFile file){
	JSONObject jsnObj=null;
	String search="_json.txt";
			if(file!=null)
	{
		System.out.println("UserID"+   user_id);
		fileName=file.getOriginalFilename();
		fileType=FileUtil.getFileExtention(fileName);
		
		
		if(fileName.toLowerCase().indexOf(search.toLowerCase())!=-1)
		{
			System.out.println("Coming in json");
			jsnObj=FileUtil.uploadJson(FileUtil.ABS_PATH+FileUtil.json_dir, file,fileName,fileType,this,user_id);
		}
		else
		{
			jsnObj=FileUtil.upload(FileUtil.ABS_PATH+FileUtil.data_dir, file,fileName,fileType,this,user_id);
			
		}
		System.out.println(""+jsnObj);
		return new ResponseEntity<JSONObject> (jsnObj,HttpStatus.OK);
	}
	else{
		jsnObj =new JSONObject();
		jsnObj.put("error", "didnt upload");
		jsnObj.put("errorCode","404");
		jsnObj.put("errorMessage","Error in Upload");
		if(fileName.toLowerCase().indexOf(search.toLowerCase())!=-1)
		{
			jsnObj.put("fileType","json");
		}
		else{
			jsnObj.put("fileType","other");
		}
		
		return new ResponseEntity<JSONObject> (jsnObj, HttpStatus.OK);
	}
	}
	
	@RequestMapping(value = "/removeFile", method = RequestMethod.POST)
	public ResponseEntity<JSONObject> removeUserFile(@RequestParam("serverFileName") String fileName)
	{		System.out.println("Remove"+fileName);

		JSONObject jsnObj =new JSONObject();

		if(FileUtil.removeFile(fileName))
		{
			jsnObj.put("error", "File Deleted");
			jsnObj.put("errorCode","200");
			jsnObj.put("errorMessage","File Deleted sucessfully");	
		}
		else{
			jsnObj.put("error", "File not found");
			jsnObj.put("errorCode","404");
			jsnObj.put("errorMessage","File not found");
		}
		return new ResponseEntity<JSONObject> (jsnObj, HttpStatus.OK);
	}
	

//@PostMapping("/validate/{login}")
	@PostMapping("/validate")
	public ResponseEntity<User> getUser(@RequestBody User user1)
	{
		//{"request":"login","id":"","password":""}
		log.debug("ID in JSON :"+ user1.getRequest());
		String req=user1.getRequest().toString();
		
		if(req.indexOf("login")>-1)
		{
			log.debug("ID in JSON :"+ user1.getId());
			log.debug("ID in JSON :"+ user1.getPassword());
		
			 user = userDAO.isValidCredentials(user1.getId(), user1.getPassword());
				if(user!=null)
				 {
					 user.setErrorCode("200");
					 user.setErrorMessage("Valid credentials");
					 return	new ResponseEntity<User>(user , HttpStatus.OK);
				 }	 else
				 {	
					 user=new User();
					 user.setId("");
					 user.setPassword("");
					 user.setErrorCode("404");
					 user.setErrorMessage("Invalid credentials");
					 return	new ResponseEntity<User>(user , HttpStatus.OK);
				 }
			
		}
		
		else if(req.indexOf("upload")>-1)
		{
			
		}
		else if(req.indexOf("getfiles")>-1)
		{
			/*user=userDAO.get(user1.getId());
			 File file = new File("RestServices/resources/images/");
		        File[] files = file.listFiles();
		        for(File f: files){
		            System.out.println(f.getName()+user1.getId());
		        }*/
		}
		else if(req.indexOf("getuserdetails")>-1)
		{
			{
				log.debug("**************Starting of the method getUserByID");
				log.info("***************Trying to get userdetails of the id " + user1.getId());
				user = userDAO.get(user1.getId());
				
				if(user==null)
				{
					user = new User();
					user.setErrorCode("404");
					user.setErrorMessage("User does not exist with the id :" + user1.getId());
				}
				else
				{
					user.setErrorCode("200");
					user.setErrorMessage("success");
				}
				
			//log.info("**************** Name of teh user is " + user.getName());
				log.debug("**************Ending of the method getUserByID");
			  return	new ResponseEntity<User>(user , HttpStatus.OK);
			}
		}
		
			 return null;
			
	}
	public JSONObject setUserDataInfo(String user_id,String realFileName,String serverFileName,String currentTime)
	{
		try
		{
		JSONObject jsonObj=new JSONObject();
		jsonObj.put("id", user_id);
		jsonObj.put("realFileName", realFileName);
		jsonObj.put("serverFileName", serverFileName);
		jsonObj.put("currentTime", currentTime);
		jsonObj.put("errorCode","200");
		jsonObj.put("errorMessage","Upload Successfully");
		jsonObj.put("fileType","other");
		// user.setErrorMessage("Invalid credentials");
		return	jsonObj;
		}
		catch(Exception ex)
		{
		
		
		}
		return null;
	}
	
	public JSONObject setUserJsonInfo(String user_id)
	{
		try
		{
		JSONObject jsonObj=new JSONObject();
		jsonObj.put("id", user_id);
		jsonObj.put("errorCode","200");
		jsonObj.put("errorMessage","Upload Successfully");
		jsonObj.put("fileType","json");
		// user.setErrorMessage("Invalid credentials");
		return	jsonObj;
		}
		catch(Exception ex)
		{
		
		
		}
		return null;
	}
}

	


/*@PostMapping("/validate")
	public ResponseEntity<User> getUser(@RequestBody User user1)
	{
		log.debug("**************Starting of the method getUserByID");
		log.info("***************Trying to get userdetails of the id " + user1.getId());
		user = userDAO.validate(user1.getId(), user1.getPassword());
		
		if(user!=null)
		 {
			//user =  userDAO.get(id);
			 user.setErrorCode("200");
			 user.setErrorMessage("Valid credentials");
			 return	new ResponseEntity<User>(user , HttpStatus.OK);
		 }
		 else
		 {	
			 user=new User();
			 user.setId("");
			 user.setPassword("");
			 user.setErrorCode("404");
			 user.setErrorMessage("Invalid credentials");
			 return	new ResponseEntity<User>(user , HttpStatus.OK);
		 }
		 

		
	  
	}*/


/*

package com.dvn.restservice.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dvn.backend.dao.UserDAO;
import com.dvn.backend.model.User;

@RestController
public class UserRestController {

	@Autowired
	private User user;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private HttpSession session;
	
	private Logger log = LoggerFactory.getLogger(UserRestController.class);

	@GetMapping("/hello")
	public String sayHello()
	{
		return "  Hello from User rest service Modifed message";
	}

	@GetMapping("/user/logout")
	public void logoutUser(){
		session.invalidate();
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUser() {
		List<User> userList = userDAO.list();
		user = (User) session.getAttribute("user");
		//System.out.println("given id is "+user.getId());
		return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
	}

	
	@GetMapping("/user/{id}")
	public ResponseEntity<User> getUser(@PathVariable("id") String id)
	{
		log.debug("**************Starting of the method getUserByID");
		log.info("***************Trying to get userdetails of the id " + id);
		user = userDAO.get(id);
		
		if(user==null)
		{
			user = new User();
			user.setErrorCode("404");
			user.setErrorMessage("User does not exist with the id :" + id);
		}
		else
		{
			user.setErrorCode("200");
			user.setErrorMessage("success");
		}
		
		log.info("**************** Name of teh user is " + user.getId());
		log.debug("**************Ending of the method getUserByID");
	  return	new ResponseEntity<User>(user , HttpStatus.OK);
	}
	

	@PutMapping(value = "/user/update/")
	public ResponseEntity<User> updateUser(@RequestBody User user) {

		if (userDAO.get(user.getId()) == null) {

			user = new User(); // ?
			user.setErrorCode("404");
			user.setErrorMessage("User does not exist with id " + user.getId());
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}

		userDAO.update(user);

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/validate/", method = RequestMethod.POST)
	public ResponseEntity<User> validateUser(@RequestBody User user) {
		user = userDAO.isValidCredentials(user.getId(), user.getPassword());
		if (user != null) {
			user.setErrorCode("200");
			user.setErrorMessage("You have successfully logged in.");
			session.setAttribute("user", user);
		} else {
			user = new User(); // Do wee need to create new user?
			user.setErrorCode("404");
			user.setErrorMessage("Invalid Credentials.  Please enter valid credentials");
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

@GetMapping("/validate/{id}/{password}")
	public ResponseEntity<User> getUser(@PathVariable("id") String id,@PathVariable("password") String password)
	{
		log.debug("**************Starting of the method getUserByID");
		log.info("***************Trying to get userdetails of the id " + id);
		user = userDAO.validate(id, password);
		
		if(user!=null)
		 {
			user =  userDAO.get(id);
			 user.setErrorCode("200");
			 user.setErrorMessage("Valid credentials");
		 }
		 else
		 {
			 user.setErrorCode("404");
			 user.setErrorMessage("Invalid credentials");
		 }
		 
	
		
	  return	new ResponseEntity<User>(user , HttpStatus.OK);
	}
	


	@RequestMapping(value = "/user/", method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody User user) {

		if (userDAO.get(user.getId()) == null) {

			if (userDAO.save(user) == true) {
				user.setErrorCode("200");
				user.setErrorMessage(
						" You have successfully Created as " + user.getId());
			} else {
				user.setErrorCode("404");
				user.setErrorMessage("Could not complete the operatin please contact Admin");

			}

			return new ResponseEntity<User>(user, HttpStatus.OK);
		}

		user.setErrorCode("404");
		user.setErrorMessage("User already exist with id : " + user.getId());
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}


}*/
/*package com.dvn.restservice.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dvn.backend.dao.UserDAO;
import com.dvn.backend.model.User;

@RestController
public class UserRestController {

	@Autowired
	private User user;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private HttpSession session;
	
	private Logger log = LoggerFactory.getLogger(UserRestController.class);

	@GetMapping("/hello")
	public String sayHello()
	{
		return "  Hello from User rest service Modifed message";
	}

	@GetMapping("/user/logout")
	public void logoutUser(){
		session.invalidate();
	}

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public ResponseEntity<List<User>> getAllUser() {
		List<User> userList = userDAO.list();
		user = (User) session.getAttribute("user");
		System.out.println("given id is "+user.getId());
		return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
	}

	
	
	

	@PutMapping(value = "/user/update/")
	public ResponseEntity<User> updateUser(@RequestBody User user) {

		if (userDAO.get(user.getId()) == null) {

			user = new User(); // ?
			user.setErrorCode("404");
			user.setErrorMessage("User does not exist with id " + user.getId());
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}

		userDAO.update(user);

		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	@RequestMapping(value = "/user/validate/", method = RequestMethod.GET)
	public ResponseEntity<User> validateUser(@RequestBody User user) {
		user = userDAO.isValidCredentials(user.getId(), user.getPassword());
		if (user != null) {
			user.setErrorCode("200");
			user.setErrorMessage("You have successfully logged in.");
			session.setAttribute("user", user);
		} else {
			user = new User(); // Do wee need to create new user?
			user.setErrorCode("404");
			user.setErrorMessage("Invalid Credentials.  Please enter valid credentials");
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

}
*/