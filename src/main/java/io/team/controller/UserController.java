package io.team.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.team.domain.User;
import io.team.jwt.JwtManager;
import io.team.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	JwtManager jwtManager;

	
	@GetMapping("/login")
	@ResponseBody
	public String find(@RequestBody User newUser, HttpServletResponse response) {
		String token = userService.find(newUser);
		response.setHeader("Authorization", token);
		return token;
	}
	
	@PostMapping("/join")
	public String register(@RequestBody User newUser) {
		
		return userService.register(newUser);
	}
	
	
	@PutMapping("/users")
	public void modify(@RequestBody User newUser,HttpServletRequest req ) {
		
		userService.modify(newUser);
	}
	
	
	@DeleteMapping("/users")
	public void remove(@RequestBody User newUser) {
		userService.remove(newUser);
	}
	
	@GetMapping("/login/{token}")
	@ResponseBody
	public Claims test(@PathVariable String token, HttpServletResponse response) {
		return jwtManager.getClaims(token);
	}
}
