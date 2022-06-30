package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import io.team.domain.BrdCmtReport;
import io.team.domain.NovelCmt;
import io.team.domain.NovelCmtReport;
import io.team.jwt.JwtManager;
import io.team.service.logic.novel.NvCmtGoodServiceLogic;
import io.team.service.logic.novel.NvCmtServiceLogic;
import io.team.service.logic.novel.NvServiceLogic;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NvCmtController {

	private final JwtManager jwtManager;
	private final NvCmtServiceLogic nvCmtServiceLogic;
	private final NvCmtGoodServiceLogic nvCmtGoodServiceLogic;
	private final NvServiceLogic nvServiceLogics;
	
	@GetMapping("/novels/detail/{titleId}/cmts")
	public @ResponseBody Map<String, Object> getCmts(@PathVariable int titleId, @RequestParam(value = "nv-id") int nvId,
			@RequestParam(value = "page", required = false, defaultValue = "1") String pagenum) {

		ArrayList<NovelCmt> cmts = nvCmtServiceLogic.getCmtList(nvId, Integer.parseInt(pagenum));

		Map<String, Object> result = new HashMap<String, Object>();
		int page = nvCmtServiceLogic.getPageNum(nvId);

		ArrayList<ArrayList<NovelCmt>> cmtsArray = new ArrayList<ArrayList<NovelCmt>>();

		for (NovelCmt cmt : cmts) {
			ArrayList<NovelCmt> tempArrayList = new ArrayList<NovelCmt>();
			tempArrayList.add(cmt);
			ArrayList<NovelCmt> replies = nvCmtServiceLogic.read_replies(cmt.getNvCmtId());
			tempArrayList.addAll(replies);
			cmtsArray.add(tempArrayList);
		}

		result.put("comments", cmtsArray);
		result.put("pagenum", page);

		return result;
	}

	@PostMapping("/novels/detail/{titleId}/cmts")
	@Transactional
	public @ResponseBody Map<String, Object> writeCmt(@PathVariable int titleId, @RequestBody NovelCmt newCmt,
			HttpServletRequest req) {
		String token = req.getHeader("Authorization");

		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int memId = jwtManager.getIdFromToken(token);
			newCmt.setMemId(memId);
			newCmt.setNvId(titleId);
			nvCmtServiceLogic.register(newCmt, token);
			nvServiceLogics.plusCmtCount(newCmt.getNvId());
			
			result.put("msg", "OK");
			return result;
		} catch (ExpiredJwtException e) {
			result.put("msg", "JWT expiration");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
			return result;
		}
	}

	@PutMapping("/novels/detail/{titleId}/cmts")
	public @ResponseBody Map<String, Object> updateCmt(@RequestBody NovelCmt newCmt, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int memId = jwtManager.getIdFromToken(token);
			newCmt.setMemId(memId);
			nvCmtServiceLogic.modify(newCmt.getNvCmtId(), newCmt, token);
			result.put("msg", "OK");
			return result;
		} catch (ExpiredJwtException e) {
			result.put("msg", "JWT expiration");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
			return result;
		}
	}

	@DeleteMapping("/novels/detail/{titleId}/cmts")
	public @ResponseBody Map<String, Object> deleteCmt( @PathVariable int titleId,
			HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			nvCmtServiceLogic.remove(titleId, token);
			result.put("msg", "OK");
			return result;
		} catch (ExpiredJwtException e) {
			result.put("msg", "JWT expiration");
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
			return result;
		}
	}
	
	
	
	// 추천, 비추천
	@PostMapping("/novels/{id1}/cmts/{id2}/like")
	public ResponseEntity<?> writeBrdLike(@PathVariable int id1, @PathVariable int id2, HttpServletRequest req) {

		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			int check = nvCmtGoodServiceLogic.assessNvCmtGood(id2, mem_id);
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

	@PostMapping("/novels/{id1}/cmts/{id2}/dislike")
	public ResponseEntity<?> writeBrdDislike(@PathVariable int id1, @PathVariable int id2, HttpServletRequest req) {

		String token = req.getHeader("Authorization");

		Map<String, Object> result = new HashMap<String, Object>();
		try {

			int mem_id = jwtManager.getIdFromToken(token);
			int check = nvCmtGoodServiceLogic.assessNvCmtBad(id2, mem_id);
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
	
	@PostMapping("/novels/{id1}/cmts/{id2}/report")
	public ResponseEntity<?> writeReport(@PathVariable int id1, @PathVariable int id2, HttpServletRequest req,
			@RequestBody NovelCmtReport nvCmtReport) {

		String token = req.getHeader("Authorization");

		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			nvCmtReport.setMemId(mem_id);
			nvCmtReport.setNvCmtId(id2);
			Optional<NovelCmtReport> optNvCmtReport = nvCmtServiceLogic.findCmtReport(id2, mem_id);
			if (!optNvCmtReport.isPresent()) {
				nvCmtServiceLogic.report(nvCmtReport);
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
