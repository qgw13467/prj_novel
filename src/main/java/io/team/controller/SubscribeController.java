package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.ExpiredJwtException;
import io.team.domain.NovelCover;
import io.team.domain.SubscribeNovel;
import io.team.jwt.JwtManager;
import io.team.service.UserService;
import io.team.service.logic.SubscribeNvService;
import io.team.service.logic.user.UserServicLogic;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SubscribeController {

	private final SubscribeNvService subscribeNvService;
	private final JwtManager jwtManager;
	private final UserServicLogic userServicLogic;

	@GetMapping("/nvc")
	public ResponseEntity<?> getSubsribeList(HttpServletRequest req) {

		HashMap<String, Object> result = new HashMap<>();
		String token = req.getHeader("Authorization");

		try {
			int mem_id = jwtManager.getIdFromToken(token);
			ArrayList<NovelCover> novelCovers = new ArrayList<>();

			novelCovers = subscribeNvService.getSubList(mem_id);

			result.put("novelCovers", novelCovers);
			return new ResponseEntity<>(result, HttpStatus.OK);

		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@PostMapping("/nvc")
	public ResponseEntity<?> review(@RequestBody HashMap<String, String> map, HttpServletRequest req) {

		HashMap<String, Object> result = new HashMap<>();
		String token = req.getHeader("Authorization");
		try {
			int memId = jwtManager.getIdFromToken(token);

			subscribeNvService.subscribeNv(memId, Integer.parseInt(map.get("nvcId")));
			userServicLogic.updateToken(map.get("token"), memId);

			result.put("msg", "OK");
			return new ResponseEntity<>(result, HttpStatus.OK);

//			if (mem_id == (int)subscribeNovel.get("memid")) {
//				System.out.println("check1");
//
//			} else {
//				result.put("msg", "mem_id is mismatch");
//				return new ResponseEntity<>(result, HttpStatus.OK);
//			}

		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@DeleteMapping("/nvc")
	public ResponseEntity<?> deleteSubsribe(@RequestBody SubscribeNovel subscribeNovel, HttpServletRequest req) {

		HashMap<String, Object> result = new HashMap<>();
		String token = req.getHeader("Authorization");

		try {
			int mem_id = jwtManager.getIdFromToken(token);
			subscribeNvService.deleteSubscribe(mem_id, subscribeNovel.getNvcId());
			result.put("msg", "OK");
			return new ResponseEntity<>(result, HttpStatus.OK);

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
