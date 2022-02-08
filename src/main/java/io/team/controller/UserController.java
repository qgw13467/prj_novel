package io.team.controller;

import java.util.HashMap;
import java.util.Map;

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
import io.team.domain.User;
import io.team.jwt.JwtManager;
import io.team.service.UserService;

@RestController
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	JwtManager jwtManager;

	
	@PostMapping("/login")
	@ResponseBody
	public HashMap find(@RequestBody User newUser, HttpServletResponse response) {
		String token = userService.makeToken(newUser);
		HashMap<String,String> map=userService.find(newUser);
		if(map==null) {
			HashMap<String,String> map2=new HashMap<String,String>();
			map2.put("msg", "ERROR");
			return map2;
		}
		
		map.put("Authorization", token);
		response.setHeader("Authorization", token);
		return map;
	}
	
	@PostMapping("/join")
	public HashMap register(@RequestBody User newUser) {
		HashMap<String,String> map=new HashMap<String, String>();
		map.put("msg", userService.register(newUser));
		return map;
	}
	
	
	@PutMapping("/users")
	public Map<String, Object> modify(@RequestBody User newUser,HttpServletRequest req ) {
		
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			result.put("msg", userService.modify(newUser, token));
			return result;
		}
		catch (Exception e){
			result.put("msg", "ERROR");
			return result;
		}
		
	}
	
	
	@DeleteMapping("/users")
	public Map<String, Object> remove(@RequestBody User newUser,HttpServletRequest req) {

		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", userService.remove(newUser, token));
			return result;
		}
		catch (Exception e){
			result.put("msg", "ERROR");
			return result;
		}
		
	}
	
	@GetMapping("/login/{token}")
	@ResponseBody
	public Claims test(@PathVariable String token, HttpServletResponse response) {
		return jwtManager.getClaims(token);
	}
}
