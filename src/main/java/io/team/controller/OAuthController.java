package io.team.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OAuthController {

//	@GetMapping("/oauth/redirect")
//	public ResponseEntity<?> getOAuthToken(@RequestParam(value = "Authorization") String token,
//			@RequestParam(value = "mem_nick") String mem_nick, @RequestParam(value = "mem_id") String mem_id,
//			@RequestParam(value = "mem_userid") String mem_userid, @RequestParam(value = "mem_icon") String mem_icon,
//			@RequestParam(value = "mem_lastlogin_datetime") String mem_lastlogin_datetime, 
//			@RequestParam(value = "mem_point") String mem_point, HttpServletRequest request, HttpServletResponse response) {
//		System.out.println(token);
//		HashMap<String, String> result = new HashMap<>();
//		result.put("Authorization", token);
//		result.put("mem_nick", mem_nick);
//		result.put("mem_id", mem_id);
//		result.put("mem_userid", mem_userid);
//		result.put("mem_icon", mem_icon);
//		result.put("mem_lastlogin_datetime", mem_lastlogin_datetime);
//		result.put("mem_point", mem_point);
//		response.addHeader("Authorization", token);
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}
	
	@GetMapping("/oauth/redirect")
	public ResponseEntity<?> getOAuthToken(HttpServletRequest request, HttpServletResponse response) {

		HashMap<String, Object> result = new HashMap<>();
		result.put("token", (String)request.getAttribute("Authorization"));
		result.put("mem_nick", (String)request.getAttribute("mem_nick"));
		result.put("mem_id", request.getAttribute("mem_id"));
		result.put("mem_userid", (String)request.getAttribute("mem_userid"));
		result.put("mem_icon", (String)request.getAttribute("mem_icon"));
		result.put("mem_lastlogin_datetime", (String)request.getAttribute("mem_lastlogin_datetime"));
		result.put("mem_point", request.getAttribute("mem_point"));
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	

}	
