package io.team.controller;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.ExpiredJwtException;
import io.team.domain.SubscribeNovel;
import io.team.jwt.JwtManager;
import io.team.service.logic.SubscribeNvService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SubscribeController {

	private final SubscribeNvService subscribeNvService;
	private final JwtManager jwtManager;

	@PostMapping("/nvc")
	public ResponseEntity<?> review(@RequestBody SubscribeNovel subscribeNovel, HttpServletRequest req) {

		HashMap<String, Object> result = new HashMap<>();
		String token = req.getHeader("Authorization");

		try {
			int mem_id = jwtManager.getIdFromToken(token);
			if (mem_id == subscribeNovel.getMemid()) {
				subscribeNvService.subscribeNv(subscribeNovel.getMemid(), subscribeNovel.getNvcid(),
						subscribeNovel.getToken());
				result.put("msg", "OK");
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				result.put("msg", "mem_id is mismatch");
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}



}
