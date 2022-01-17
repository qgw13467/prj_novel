package io.team.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.diagnostics.analyzer.BeanNotOfRequiredTypeFailureAnalyzer;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.team.domain.Board;
import io.team.domain.User;
import io.team.service.BoardService;

@RestController
public class BoardController {

	@Autowired
	private BoardService boardService;

	@GetMapping("/boards/?page={pagenum}")
	public @ResponseBody ArrayList<Board> getAllBoards(@PathVariable int pagenum) {
		ArrayList<Board> boards = boardService.getBoardList(pagenum);

		return boardService.getBoardList(20);
	}
	
	@GetMapping("/boards/{id}")
	public @ResponseBody Board read(@PathVariable int id) {
		return boardService.find(id);
	}
	
	@PostMapping("/boards")
	public void write(@RequestBody Board newBoard) {
		boardService.register(newBoard);
	}
	
	@PutMapping("/boards/{id}")
	public void update(@PathVariable int id,@RequestBody Board newBoard) {
		boardService.modify(id, newBoard);
	}
	
	@DeleteMapping("/boards/{id}")
	public void delete(@PathVariable int id) {
		boardService.remove(id);
	}
}
