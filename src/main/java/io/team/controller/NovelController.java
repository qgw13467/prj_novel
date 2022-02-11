package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
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

import com.google.gson.Gson;

import io.team.domain.Novel;
import io.team.mapper.NvMapper;
import io.team.service.logic.NvServiceLogic;

@RestController
public class NovelController {

	@Autowired
	private NvServiceLogic nvServiceLogic;

	@GetMapping("/novels/detail")
	public @ResponseBody Map<String, Object> getAllNovels(
			@RequestParam(value = "page", required = false, defaultValue = "1") String pagenum) {

		ArrayList<Novel> boards = nvServiceLogic.getList(Integer.parseInt(pagenum));
		Map<String, Object> result = new HashMap<String, Object>();
		int page = nvServiceLogic.getPageNum();
		result.put("boards", boards);
		result.put("pagenum", page);
		return result;
	}

	@GetMapping("/novels/detail/{id}")
	public @ResponseBody Novel read(@PathVariable int id) {
		return nvServiceLogic.find(id);
	}
	
	/////////////////////////////
	@Autowired
	NvMapper novelMapper;
	@PostMapping("/test")
	public @ResponseBody int read(@RequestBody HashMap<String, Object> map, HttpServletRequest req) {
		
		String token = req.getHeader("Authorization");
		LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>) map.get("novel");
		Novel novel=new Novel(linkedHashMap);
		novel.setImg_id(1);
		System.out.println(novel);
		Novel tempNovel=new Novel();
		
		try {
			int test = 0;
			
			int temp = novelMapper.create(novel);
			
			System.out.println(novel);
			return test;
		}
		catch (Exception e) {
			System.out.println(e);
			return tempNovel.getNv_id();
		}
		
	}
	//////////////////////////////////
	@PostMapping("/novels/detail")
	public @ResponseBody Map<String, Object> write(@RequestBody HashMap<String, Object> map, HttpServletRequest req) {
		
		String token = req.getHeader("Authorization");
		int check=0;
		
		LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>) map.get("novel");
		Novel novel=new Novel(linkedHashMap);
		Map<String, Object> result=new HashMap<String, Object>();

		try {
			if(Integer.parseInt((String)map.get("parent")) == 0) {	//1화를 처음작성하면 표지생성
				check += nvServiceLogic.register(novel,token);
				
				//태그작업해야함
				
				
				result.put("msg", check);
				return result; 
			}
			else {	//1화가 아니면 부모자식테이블, 모든에피소드테이블에 등록
				check += nvServiceLogic.register(novel,token,(int)map.get("parnet"));
				result.put("msg", check);
				return result;
			}
			
		} catch (Exception e) {
			System.out.println(e);
			result.put("msg", "ERROR");
			return result;
		}

	}

	@PutMapping("/novels/detail/{id}")
	public @ResponseBody Map<String, Object> update(@PathVariable int id, @RequestBody Novel newNovel,
			HttpServletRequest req) {
		newNovel.setNv_id(id);
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", nvServiceLogic.modify(id, newNovel, token));
			return result;
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return result;
		}
	}

	@DeleteMapping("/novels/detail/{id}")
	public @ResponseBody Map<String, Object> delete(@PathVariable int id, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", nvServiceLogic.remove(id, token));
			return result;
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return result;
		}
	}

}
