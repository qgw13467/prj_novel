package io.team.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.annotations.Api;
import io.team.domain.User;
import io.team.domain.Enum.PointPurpose;
import io.team.jwt.JwtManager;
import io.team.service.UserService;
import io.team.service.logic.PointServiceLogic;
import io.team.service.logic.UserServicLogic;

@RestController
public class UserController {
	@Autowired
	private UserServicLogic userServicLogic;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	JwtManager jwtManager;
	
	@Autowired
	PointServiceLogic pointServiceLogic;
	
	
	
	@PostMapping("/login")
	@ResponseBody
	public HashMap find(@RequestBody User newUser, HttpServletResponse response) {
		HashMap<String,String> map = new HashMap<>();
		try {
			System.out.println("login");
			String token = userServicLogic.makeToken(newUser);
			map=userServicLogic.find(newUser);
			int attendance_point = 100;
			int check = pointServiceLogic.attend(Integer.parseInt(map.get("mem_id")), PointPurpose.ATTENDANCE, attendance_point, map.get("mem_lastlogin_datetime"));

			if(check == 1) {
				map.put("attendance point", ""+attendance_point);
				
			}
			else {
				map.put("attendance point", "0");
			}
			map.put("Authorization", token);
			response.setHeader("Authorization", token);
			return map;
		}catch (Exception e) {
			e.printStackTrace();
			map.put("msg", "ERROR");
			return map;
		}
		
	}
	
	@PostMapping("/join")
	public HashMap register(@RequestBody User newUser) {
		HashMap<String,String> map=new HashMap<String, String>();
		
		String pwd = newUser.getMem_password();
		String encPwd = bCryptPasswordEncoder.encode(pwd);
		newUser.setMem_password(encPwd);
		
		map.put("msg", userServicLogic.register(newUser));
		return map;
	}
	
	
	@PutMapping("/users")
	public Map<String, Object> modify(@RequestBody User newUser,HttpServletRequest req ) {
		
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		
		String pwd = newUser.getMem_password();
		String encPwd = bCryptPasswordEncoder.encode(pwd);
		newUser.setMem_password(encPwd);
		
		try {
			result.put("msg", userServicLogic.modify(newUser, token));
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
			result.put("msg", userServicLogic.remove(newUser, token));
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
