package com.backend.mvc.dao;



import com.backend.mvc.model.User;

public interface UserDao {
	User authenticate(User user);
	void updateUser(User user);
	User registerUser(User user);
}
