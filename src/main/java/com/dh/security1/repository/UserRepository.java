package com.dh.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dh.security1.model.User;

public interface UserRepository extends JpaRepository<User, Integer>{

	//findBy 규칙 -> Username 문법
	//select * from user where username=1?
	public User findUserByUsername(String username);	//jpa query method 찾아봐

}
