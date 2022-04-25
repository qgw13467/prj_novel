package io.team.controller;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.ExpiredJwtException;
import io.team.domain.Review;
import io.team.jwt.JwtManager;
import io.team.service.logic.novel.ReviewServiceLogic;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NvReviewController {

	private final ReviewServiceLogic reviewServiceLogic;

	private final JwtManager jwtManager;

	@PostMapping("/novels/review/{titleId}")
	public ResponseEntity<?> review(@PathVariable int titleId, @RequestBody Review review, HttpServletRequest req) {
		HashMap<String, Object> result = new HashMap<>();
		String token = req.getHeader("Authorization");

		try {
			int mem_id = jwtManager.getIdFromToken(token);
			int check = reviewServiceLogic.findPastReview(review.getNvId(), mem_id);

			if (check == 1) {
				result.put("msg", "reduplication");
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			review.setMemId(mem_id);
			check = reviewServiceLogic.register(review);
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

	/*
	 * @PutMapping("/novels/review/{titleId}") public @ResponseBody ResponseEntity
	 * modifyreview(@PathVariable int titleId, @RequestParam(value = "nv_id") int
	 * nv_id,
	 * 
	 * @RequestBody Review review, HttpServletRequest req) { HashMap<String, Object>
	 * result = new HashMap<>(); String token = req.getHeader("Authorization"); int
	 * check = 0; try { check = reviewServiceLogic.modify(review, token);
	 * result.put("msg", check); return new ResponseEntity<>(result, HttpStatus.OK);
	 * } catch (Exception e) { result.put("msg", "ERROR"); return new
	 * ResponseEntity<>(result, HttpStatus.OK); }
	 * 
	 * }
	 */
}
