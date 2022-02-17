package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;
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

import io.swagger.annotations.Api;
import io.team.domain.Board;
import io.team.domain.BrdCmt;
import io.team.jwt.JwtManager;
import io.team.service.WriteService;
import io.team.service.logic.BoardServiceLogic;
import io.team.service.logic.BrdCmtServiceLogic;



@RestController
public class BoardController {
	
	@Autowired
	private BrdCmtServiceLogic cmtService;

	@Autowired
	private BoardServiceLogic boardService;

	@GetMapping("/boards")
	public @ResponseBody Map<String, Object> getAllBoards(@RequestParam(value = "page", required=false, defaultValue = "1") String pagenum) {
		
		ArrayList<Board> boards = boardService.getList(Integer.parseInt(pagenum));
		
		Map<String, Object> result = new HashMap<String, Object>();
		int page=boardService.getPageNum();
		result.put("boards", boards);
		result.put("pagenum", page);
		return result;
	}

	@GetMapping("/boards/{id}")
	public @ResponseBody Board read(@PathVariable int id) {
		return (Board)boardService.find(id);
	}

	@PostMapping("/boards")
	public @ResponseBody Map<String, Object> write(@RequestBody Board newBoard, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", boardService.register(newBoard, token));
			return result;
		}
		catch (Exception e){
			result.put("msg", "ERROR");
			return result;
		}
	}

	@PutMapping("/boards/{id}")
	public @ResponseBody Map<String, Object> update(@PathVariable int id, @RequestBody Board newBoard, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", boardService.modify(id, newBoard, token));
			return result;
		}
		catch (Exception e){
			result.put("msg", "ERROR");
			return result;
		}
	}

	@DeleteMapping("/boards/{id}")
	public @ResponseBody Map<String, Object> delete(@PathVariable int id, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", boardService.remove(id, token));
			return result;
		}
		catch (Exception e){
			result.put("msg", "ERROR");
			return result;
		}
	}

	// 댓글작성
	
	@GetMapping("/boards/{id}/cmts")
	public @ResponseBody Map<String, Object> getCmts(@PathVariable int id,
			@RequestParam(value = "page", required = false, defaultValue = "1") String pagenum) {

		ArrayList<BrdCmt> cmts = cmtService.getCmtList(id, Integer.parseInt(pagenum));
		
		System.out.println(pagenum);
		System.out.println(cmts);
		Map<String, Object> result = new HashMap<String, Object>();
		int page = cmtService.getPageNum(id);
		
		ArrayList<ArrayList<BrdCmt>> cmtsArray=new ArrayList<ArrayList<BrdCmt>>();
		
		for(BrdCmt cmt:cmts) {
			ArrayList<BrdCmt> tempArrayList=new ArrayList<BrdCmt>();
			tempArrayList.add(cmt);
			ArrayList<BrdCmt> replies = cmtService.read_replies(cmt.getBrd_cmt_id());
			tempArrayList.addAll(replies);
			cmtsArray.add(tempArrayList);
		}
		
		result.put("comments", cmtsArray);	
		result.put("pagenum", page);
		
		return result;
	}

	@PostMapping("/boards/{id}/cmts")
	public @ResponseBody Map<String, Object> writeCmt(@PathVariable int id, @RequestBody BrdCmt newCmt, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			
			newCmt.setBrd_id(id);
			result.put("msg", cmtService.register(newCmt, token));
			return result;
		}
		catch (Exception e){
			result.put("msg", "ERROR");
			System.out.println(e);
			return result;
		}
	}

	@PutMapping("/boards/{id}/cmts/{brd_cmt_id}")
	public @ResponseBody Map<String, Object> updateCmt(@PathVariable int brd_cmt_id, @RequestBody BrdCmt newCmt,
			HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", cmtService.modify(brd_cmt_id, newCmt, token));
			return result;
		}
		catch (Exception e){
			result.put("msg", "ERROR");
			return result;
		}
	}

	@DeleteMapping("/boards/{id}/cmts/{brd_cmt_id}")
	public @ResponseBody Map<String, Object> deleteCmt(@PathVariable int brd_cmt_id, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", cmtService.remove(brd_cmt_id, token));
			return result;
		}
		catch (Exception e){
			result.put("msg", "ERROR");
			return result;
		}
	}
	

}
