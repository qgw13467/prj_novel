package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.team.domain.Board;
import io.team.domain.BrdCmt;
import io.team.service.BoardService;
import io.team.service.logic.BrdCmtServiceLogic;

@RestController
public class BoardController {

	@Autowired
	private	BrdCmtServiceLogic cmtService;
	
	@Autowired
	private BoardService boardService;

	@GetMapping("/boards")
	public @ResponseBody Map<String, Object> getAllBoards(@RequestParam(value = "page") String pagenum) {
		
		ArrayList<Board> boards = boardService.getBoardList(Integer.parseInt(pagenum));
		Map<String, Object> result = new HashMap<String, Object>();
		int page=boardService.getPageNum();
		result.put("boards", boards);
		result.put("pagenum", page);
		return result;
	}

	@GetMapping("/boards/{id}")
	public @ResponseBody Board read(@PathVariable int id) {
		return boardService.find(id);
	}

	@PostMapping("/boards")
	public @ResponseBody int write(@RequestBody Board newBoard, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		return boardService.register(newBoard, token);
	}

	@PutMapping("/boards/{id}")
	public @ResponseBody int update(@PathVariable int id, @RequestBody Board newBoard, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		return boardService.modify(id, newBoard, token);
	}

	@DeleteMapping("/boards/{id}")
	public @ResponseBody int delete(@PathVariable int id, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		return boardService.remove(id, token);
	}
	
	//댓글작성 
	
	@GetMapping("/boards/{id}/cmts")
	public @ResponseBody Map<String, Object> getCmts(@PathVariable int id, @RequestParam(value = "page") String pagenum) {
		
		ArrayList<BrdCmt> cmts = cmtService.getCmtList(id, Integer.parseInt(pagenum));
		ArrayList<BrdCmt> replies = cmtService.getReplyList(cmts);
		
		Map<String, Object> result = new HashMap<String, Object>();
		int page=cmtService.getPageNum(id);
		result.put("comments", cmts);
		result.put("replies", replies);
		result.put("pagenum", page);
		return result;
	}
	
	@PostMapping("/boards/{id}/cmts")
	public @ResponseBody int writeCmt(@RequestBody BrdCmt newCmt, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		return cmtService.register(newCmt, token);
	}

	@PutMapping("/boards/{id}/cmts/{brd_cmt_id}")
	public @ResponseBody int updateCmt(@PathVariable int id, @RequestBody BrdCmt newCmt, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		return cmtService.modify(id, newCmt, token);
	}

	@DeleteMapping("/boards/{id}/cmts/{brd_cmt_id}")
	public @ResponseBody int deleteCmt(@PathVariable int id, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		return cmtService.remove(id, token);
	}
	
}
