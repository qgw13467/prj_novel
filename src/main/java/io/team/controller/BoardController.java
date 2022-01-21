package io.team.controller;

import java.util.ArrayList;

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
import io.team.service.BoardService;

@RestController
public class BoardController {

	@Autowired
	private BoardService boardService;

	@GetMapping("/boards")
	public @ResponseBody ArrayList<Board> getAllBoards(@RequestParam(value = "page") String pagenum) {

		ArrayList<Board> boards = boardService.getBoardList(Integer.parseInt(pagenum));
		return boards;
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
}
