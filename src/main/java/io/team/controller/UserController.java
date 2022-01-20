package io.team.controller;


import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.annotations.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.team.domain.User;
import io.team.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	

	
	@GetMapping("/login")
	public void find(@RequestBody User newUser, HttpServletResponse response) {
		response.setHeader("Authorize", userService.find(newUser));
	}
	
	@PostMapping("/join")
	public String register(@RequestBody User newUser) {
		
		return userService.register(newUser);
	}
	
	
	@PutMapping("/users/{id}")
	public void modify(@PathVariable int id,@RequestBody User newUser) {
		userService.modify(id, newUser);
	}
	
	
	@DeleteMapping("/users/{id}")
	public void remove(@PathVariable int id) {
		userService.remove(id);
	}
}
