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

		int mem_id = (int) request.getAttribute("memId");
		int mem_point = (int) request.getAttribute("memPoint");
		HashMap<String, Object> result = new HashMap<>();
		result.put("RefreshToken", (String)request.getAttribute("RefreshToken"));
		result.put("token", (String)request.getAttribute("Authorization"));
		result.put("memNick", (String)request.getAttribute("memNick"));
		result.put("memId", Integer.toString(mem_id));
		result.put("memUserId", (String)request.getAttribute("memUserId"));
		result.put("memIcon", (String)request.getAttribute("memIcon"));
		result.put("memLastloginDatetime", (String)request.getAttribute("memLastloginDatetime"));
		result.put("memPoint", Integer.toString(mem_point));
		return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	

}	
