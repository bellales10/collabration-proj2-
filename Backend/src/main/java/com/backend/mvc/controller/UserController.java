package com.backend.mvc.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.backend.mvc.model.Error;
import com.backend.mvc.dao.UserDao;
import com.backend.mvc.model.User;

@RestController
public class UserController {
Logger logger=LoggerFactory.getLogger(this.getClass());

@Autowired
private UserDao userDao;

@RequestMapping("/home")
public String home(){
    return "home";
}

@RequestMapping(value="/login",method=RequestMethod.POST)
public ResponseEntity<?> login(@RequestBody User user,HttpSession session){
	logger.debug("Entering USERCONTROLLER : LOGIN");
	User validUser=userDao.authenticate(user);
	if(validUser==null){
		logger.debug("validUser is null");
		Error error=new Error(1,"Username and password doesnt exists...");
		return new ResponseEntity<Error>(error,HttpStatus.UNAUTHORIZED); //401
	}
	else{
		session.setAttribute("user",validUser);
		validUser.setOnline(true);
		userDao.updateUser(validUser); // to update online status in db
		logger.debug("validUser is not null");
		return new ResponseEntity<User>(validUser,HttpStatus.OK);//200
	}
}

//'?'  - Any Type [User,Error] 
//ENDPOINT : http://localhost:8098/Backend/register 
@RequestMapping(value="/register",method=RequestMethod.POST)
public ResponseEntity<?> registerUser(@RequestBody User user){
	//client will send only UserName, password, email, role 
	
	logger.debug("USERCONTROLLER=>REGISTER " + user);
	user.setStatus(true);
	user.setOnline(false);
	User savedUser=userDao.registerUser(user);
	logger.debug("User Id generated is " + savedUser.getId());

	if(savedUser.getId()==0){
		Error error=new Error(2,"Couldnt insert user details ");
		return new ResponseEntity<Error>(error , HttpStatus.CONFLICT);
	}
	else
	{
		return new ResponseEntity<User>(savedUser,HttpStatus.OK);
     }
	/*}
      catch(Exception e) {
    	 e.printStackTrace();
    	 Error error=new Error(2,"Couldn't insert user details. Cannot have null/duplicate value" + e.getMessage());
    	 return new ResponseEntity<Error>(error , HttpStatus.INTERNAL_SERVER_ERROR);
     }*/
  }   
     
@RequestMapping(value="/logout",method=RequestMethod.PUT)
public ResponseEntity<?> logout(HttpSession session){
	User user=(User)session.getAttribute("user");
	if(user!=null)
	{
		user.setOnline(false);
		userDao.updateUser(user);
	}
	session.removeAttribute("user");
	session.invalidate();
	return new ResponseEntity<Void>(HttpStatus.OK);
    
     }
}