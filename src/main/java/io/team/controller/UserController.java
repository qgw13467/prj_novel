package io.team.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.team.domain.User;
import io.team.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	
	@GetMapping("/users")
	public ArrayList<User> getAllUsers() {
		System.out.println("controller");
		return userService.selctAllUsers();
	}
}
