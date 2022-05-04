package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.ExpiredJwtException;
import io.team.domain.BrdCmt;
import io.team.domain.BrdCmtReport;
import io.team.domain.BrdReport;
import io.team.jwt.JwtManager;
import io.team.service.logic.board.BrdCmtGoodService;
import io.team.service.logic.board.BrdCmtServiceLogic;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardCmtController {
	
	private final BrdCmtServiceLogic brdCmtServiceLogic;
	private final BrdCmtGoodService brdCmtGoodService;
	private final JwtManager jwtManager;

	
	@GetMapping("/boards/{id}/cmts")
	public @ResponseBody Map<String, Object> getCmts(@PathVariable int id,
			@RequestParam(value = "page", required = false, defaultValue = "1") String pagenum) {

		ArrayList<BrdCmt> cmts = brdCmtServiceLogic.getCmtList(id, Integer.parseInt(pagenum));
		Map<String, Object> result = new HashMap<String, Object>();
		int page = brdCmtServiceLogic.getPageNum(id);

		ArrayList<ArrayList<BrdCmt>> cmtsArray = new ArrayList<ArrayList<BrdCmt>>();

		for (BrdCmt cmt : cmts) {
			ArrayList<BrdCmt> tempArrayList = new ArrayList<BrdCmt>();
			tempArrayList.add(cmt);
			ArrayList<BrdCmt> replies = brdCmtServiceLogic.read_replies(cmt.getBrdCmtId());
			tempArrayList.addAll(replies);
			cmtsArray.add(tempArrayList);
		}

		result.put("comments", cmtsArray);
		result.put("pagenum", page);

		return result;
	}
	
	@Transactional
	@PostMapping("/boards/{id}/cmts")
	public @ResponseBody Map<String, Object> writeCmt(@PathVariable int id, @RequestBody BrdCmt newCmt,
			HttpServletRequest req) {
		String token = req.getHeader("Authorization");

		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			newCmt.setBrdId(id);
			newCmt.setMemId(mem_id);

			brdCmtServiceLogic.register(newCmt, token);
			result.put("msg", "OK");
			return result;
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return result;
		} catch (Exception e) {
			result.put("msg", "ERROR");
			e.printStackTrace();
			return result;
		}
	}
	
	@Transactional
	@PutMapping("/boards/{id}/cmts/{brd_cmt_id}")
	public @ResponseBody Map<String, Object> updateCmt(@PathVariable int brd_cmt_id, @RequestBody BrdCmt newCmt,
			HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			newCmt.setMemId(mem_id);
			brdCmtServiceLogic.modify(brd_cmt_id, newCmt, token);
			result.put("msg", "OK");
			return result;
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
			return result;
		}
	}
	
	@Transactional
	@DeleteMapping("/boards/{id}/cmts/{brd_cmt_id}")
	public @ResponseBody Map<String, Object> deleteCmt(@PathVariable int brd_cmt_id, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			brdCmtServiceLogic.remove(brd_cmt_id, token);
			result.put("msg", "OK");
			return result;
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
			return result;
		}
	}
	
	// 추천, 비추천
	@Transactional
	@PostMapping("/boards/{id1}/cmts/{id2}/like")
	public ResponseEntity<?> writeBrdLike(@PathVariable int id1, @PathVariable int id2, HttpServletRequest req) {

		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			
			int check = brdCmtGoodService.assessBrdCmtGood(id2, mem_id);
			if(check == -1) {
				result.put("msg", "cancel");
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			result.put("msg", "OK");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ExpiredJwtException e) {
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {
			result.put("msg", "ERROR");
			e.printStackTrace();
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}
	
	@Transactional
	@PostMapping("/boards/{id1}/cmts/{id2}/dislike")
	public ResponseEntity<?> writeBrdDislike(@PathVariable int id1, @PathVariable int id2, HttpServletRequest req) {

		String token = req.getHeader("Authorization");

		Map<String, Object> result = new HashMap<String, Object>();
		try {

			int mem_id = jwtManager.getIdFromToken(token);
			int check = brdCmtGoodService.assessBrdCmtBad(id2, mem_id);
			if(check == -1) {
				result.put("msg", "cancel");
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			result.put("msg", "OK");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ExpiredJwtException e) {
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {
			result.put("msg", "ERROR");
			e.printStackTrace();
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}
	
	@Transactional
	@PostMapping("/boards/{id1}/cmts/{id2}/report")
	public ResponseEntity<?> writeReport(@PathVariable int id1, @PathVariable int id2, HttpServletRequest req,
			@RequestBody BrdCmtReport brdCmtReport) {

		String token = req.getHeader("Authorization");

		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			brdCmtReport.setMemId(mem_id);
			brdCmtReport.setBrdCmtId(id2);
			Optional<BrdCmtReport> optbrdCmtReport = brdCmtServiceLogic.findCmtReport(id2, mem_id);
			if (!optbrdCmtReport.isPresent()) {
				brdCmtServiceLogic.report(brdCmtReport);
				result.put("msg", "OK");
				return new ResponseEntity<>(result, HttpStatus.OK);

			} else {
				result.put("msg", "reduplication");
				return new ResponseEntity<>(result, HttpStatus.OK);
			}

		} catch (ExpiredJwtException e) {
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {
			result.put("msg", "ERROR");
			e.printStackTrace();
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}
}
