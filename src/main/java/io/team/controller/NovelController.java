package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import io.team.domain.Novel;
import io.team.domain.NovelCover;
import io.team.mapper.NvMapper;
import io.team.service.logic.NvCoverServiceLogic;
import io.team.service.logic.NvServiceLogic;

@RestController
public class NovelController {

	@Autowired
	private NvServiceLogic nvServiceLogic;

	@Autowired
	private NvCoverServiceLogic nvCoverServiceLogic;

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

	@GetMapping("/novels/detail/{titleId}")
	public @ResponseBody Novel read(@PathVariable int titleId, @RequestParam(value = "nv_id") int nv_id) {
		nvServiceLogic.countCheck(nv_id);
		return nvServiceLogic.find(nv_id);
	}
	



	@PostMapping("/novels/detail/{titleId}")
	public @ResponseBody Map<String, Object> write(@PathVariable int titleId, @RequestBody HashMap<String, Object> map,
			HttpServletRequest req) {

		String token = req.getHeader("Authorization");
		int check = 0;

		HashMap<String, String> hashMap = (HashMap<String, String>) map.get("novel");
		Novel novel = new Novel(hashMap);
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			if (Integer.parseInt((String)map.get("parent"))==0) { // 1화를 처음작성하면 표지생성
				int nv_id = nvServiceLogic.register(novel, token);
				
				if(nv_id != 1) {
					throw new Exception();
				}
				NovelCover novelCover = nvCoverServiceLogic.find(titleId);
				novelCover.setNvid(nv_id);
				System.out.println(novelCover);
				check += nvCoverServiceLogic.modify(titleId, novelCover, token);
				result.put("msg", check);
				return result;
			} else { // 1화가 아니면 부모자식테이블, 모든에피소드테이블에 등록
				check += nvServiceLogic.register(novel, token, Integer.parseInt((String)map.get("parent")), titleId);
				result.put("msg", check);
				return result;
			}

		} catch (Exception e) {
			System.out.println(e);
			result.put("msg", "ERROR");
			return result;
		}

	}

	@PutMapping("/novels/detail/{titleId}")
	public @ResponseBody Map<String, Object> update(@PathVariable int titleId, @RequestParam(value = "nv_id") int nv_id, @RequestBody Novel newNovel,
			HttpServletRequest req) {
		newNovel.setNv_id(titleId);
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", nvServiceLogic.modify(titleId, newNovel, token));
			return result;
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return result;
		}
	}

	@DeleteMapping("/novels/detail/{titleId}")
	public @ResponseBody Map<String, Object> delete(@PathVariable int titleId, @RequestParam(value = "nv_id") int nv_id, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", nvServiceLogic.remove(titleId, token));
			return result;
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return result;
		}
	}

}
