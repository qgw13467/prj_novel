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


	@GetMapping("/oauth/redirect")
	public ResponseEntity<?> getOAuthToken(HttpServletRequest request, HttpServletResponse response) {
		int mem_id = (int) request.getAttribute("mem_id");
		int mem_point = (int) request.getAttribute("mem_point");
		HashMap<String, Object> result = new HashMap<>();
		result.put("token", (String)request.getAttribute("Authorization"));
		result.put("mem_nick", (String)request.getAttribute("mem_nick"));
		result.put("mem_id", Integer.toString(mem_id));
		result.put("mem_userid", (String)request.getAttribute("mem_userid"));
		result.put("mem_icon", (String)request.getAttribute("mem_icon"));
		result.put("mem_lastlogin_datetime", (String)request.getAttribute("mem_lastlogin_datetime"));
		result.put("mem_point", Integer.toString(mem_point));
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	

}	
