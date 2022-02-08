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
import io.team.domain.Novel;
import io.team.service.logic.NovelServiceLogic;

@RestController
public class NovelController {

	@Autowired
	private NovelServiceLogic novelServiceLogic;

	@GetMapping("/novels")
	public @ResponseBody Map<String, Object> getAllNovels(@RequestParam(value = "page", required=false, defaultValue = "1") String pagenum) {
		
		ArrayList<Novel> boards = novelServiceLogic.getList(Integer.parseInt(pagenum));
		Map<String, Object> result = new HashMap<String, Object>();
		int page=novelServiceLogic.getPageNum();
		result.put("boards", boards);
		result.put("pagenum", page);
		return result;
	}

	@GetMapping("/novels/{id}")
	public @ResponseBody Novel read(@PathVariable int id) {
		return (Novel)novelServiceLogic.find(id);
	}

	@PostMapping("/novels")
	public @ResponseBody Map<String, Object> write(@RequestBody Novel newNovel, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", novelServiceLogic.register(newNovel, token));
			return result;
		}
		catch (Exception e){
			result.put("msg", "ERROR");
			return result;
		}
	}

	@PutMapping("/novels/{id}")
	public @ResponseBody Map<String, Object> update(@PathVariable int id, @RequestBody Novel newNovel, HttpServletRequest req) {
		newNovel.setNv_id(id);
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		System.out.println(novelServiceLogic.modify(id, newNovel, token));
		try {
			result.put("msg", novelServiceLogic.modify(id, newNovel, token));
			return result;
		}
		catch (Exception e){
			result.put("msg", "ERROR");
			return result;
		}
	}

	@DeleteMapping("/novels/{id}")
	public @ResponseBody Map<String, Object> delete(@PathVariable int id, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", novelServiceLogic.remove(id, token));
			return result;
		}
		catch (Exception e){
			result.put("msg", "ERROR");
			return result;
		}
	}
}
