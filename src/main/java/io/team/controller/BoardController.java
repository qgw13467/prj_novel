package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
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
import io.team.domain.Board;
import io.team.domain.BrdCmt;
import io.team.domain.BrdReport;
import io.team.domain.dto.BoardDTO;
import io.team.domain.dto.UserInfoDTO;
import io.team.jwt.JwtManager;
import io.team.service.logic.board.BoardGoodServiceLogic;
import io.team.service.logic.board.BoardServiceLogic;
import io.team.service.logic.board.BrdCmtServiceLogic;
import io.team.service.logic.user.UserServicLogic;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardController {

	private final BoardServiceLogic boardService;
	private final UserServicLogic userServicLogic;
	private final BoardGoodServiceLogic boardGoodServiceLogic;

	private final JwtManager jwtManager;
	
	//게시판 목록 가져오기
	@GetMapping("/boards")
	public @ResponseBody Map<String, Object> getAllBoards(
			@RequestParam(value = "page", required = false, defaultValue = "1") String pagenum,
			@RequestParam(value = "pagecount", required = false, defaultValue = "10") String pagecount) {

		ArrayList<Board> boards = boardService.getList(Integer.parseInt(pagenum), Integer.parseInt(pagecount));

		Map<String, Object> result = new HashMap<String, Object>();
		int page = boardService.getPageNum();
		result.put("boards", boards);
		result.put("pagenum", page);
		return result;
	}
	
	//게시물 하나 보기
	@GetMapping("/boards/{id}")
	public ResponseEntity<?> read(@PathVariable int id) {
		Map<String, Object> result = new HashMap<>();
		BoardDTO boardDTO =	boardService.findDTO(id);
		
		UserInfoDTO userInfoDTO = UserInfoDTO.userInfoDTOfromUser(userServicLogic.findByMemId(boardDTO.getMemId()));
		
		result.put("board", boardDTO);
		result.put("user", userInfoDTO);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	//게시물 작성
	@PostMapping("/boards")
	public @ResponseBody Map<String, Object> write(@RequestBody Board newBoard, HttpServletRequest req) {
		String token = req.getHeader("Authorization");

		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			newBoard.setMemId(mem_id);
			boardService.register(newBoard, token);
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
	
	//게시물 수정
	@PutMapping("/boards/{id}")
	public @ResponseBody Map<String, Object> update(@PathVariable int id, @RequestBody Board newBoard,
			HttpServletRequest req) {
		String token = req.getHeader("Authorization");

		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			int check = boardService.modify(id, newBoard, token);
			if (check == 1) {
				result.put("msg", "OK");
				return result;
			}
			else {
				result.put("msg", "No permission");
				return result;
			}
				
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

	//게시물 삭제
	@DeleteMapping("/boards/{id}")
	public @ResponseBody Map<String, Object> delete(@PathVariable int id, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", boardService.remove(id, token));
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

	

	// 검색
	@GetMapping("/boards/search")
	public @ResponseBody Map<String, Object> findByTitleConrain(
			@RequestParam(value = "srctype", required = false, defaultValue = "title") String srctype,
			@RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@RequestParam(value = "page", required = false, defaultValue = "1") String pagenum,
			@RequestParam(value = "rownum", required = false, defaultValue = "10") String rownum) {

		if (srctype.equals("title")) {
			ArrayList<Board> boards = boardService.findByTitleContain(keyword, Integer.parseInt(pagenum),
					Integer.parseInt(rownum));
			Map<String, Object> result = new HashMap<String, Object>();
			int page = boardService.getTitleContainNum(keyword);
			result.put("boards", boards);
			result.put("pagenum", page);
			return result;
		} else {
			ArrayList<Board> boards = boardService.findByContentsContain(keyword, Integer.parseInt(pagenum),
					Integer.parseInt(rownum));
			Map<String, Object> result = new HashMap<String, Object>();
			int page = boardService.getContentsContainNum(keyword);
			result.put("boards", boards);
			result.put("pagenum", page);
			return result;
		}

	}

	// 추천, 비추천
	@PostMapping("/boards/{id}/like")
	public ResponseEntity<?> writeBrdLike(@PathVariable int id, HttpServletRequest req) {

		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			int check = boardGoodServiceLogic.assessBrdGood(id, mem_id);
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

	@PostMapping("/boards/{id}/dislike")
	public ResponseEntity<?> writeBrdDislike(@PathVariable int id, HttpServletRequest req) {

		String token = req.getHeader("Authorization");

		Map<String, Object> result = new HashMap<String, Object>();
		try {

			int mem_id = jwtManager.getIdFromToken(token);
			int check =  boardGoodServiceLogic.assessBrdBad(id, mem_id);
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

	@PostMapping("/boards/{id}/report")
	public ResponseEntity<?> writeReport(@PathVariable int id, HttpServletRequest req,
			@RequestBody BrdReport brdReport) {

		String token = req.getHeader("Authorization");

		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			brdReport.setMemId(mem_id);
			brdReport.setBrdId(id);
			Optional<BrdReport> optbrdReport = boardService.findReport(id, mem_id);
			if (!optbrdReport.isPresent()) {
				boardService.report(brdReport);
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
