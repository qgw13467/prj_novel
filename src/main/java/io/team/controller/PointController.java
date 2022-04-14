package io.team.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.team.domain.Review;
import io.team.service.logic.PointServiceLogic;

@RestController
public class PointController {
	
	@Autowired
	PointServiceLogic pointServiceLogic;
	
	@PostMapping("/review/{titleId}")
	public ResponseEntity<?> review(@PathVariable int titleId, @RequestParam(value = "nv_id") int nv_id,
			@RequestBody Review review, HttpServletRequest req) {
		HashMap<String, Object> result = new HashMap<>();
		String token = req.getHeader("Authorization");

		try {
			
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}
	
	
}
