package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;
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
import io.team.domain.Novel;
import io.team.domain.NovelCmt;
import io.team.domain.NovelCover;
import io.team.domain.NovelLink;
import io.team.domain.Enum.PointPurpose;
import io.team.jwt.JwtManager;
import io.team.service.logic.PointServiceLogic;
import io.team.service.logic.SubscribeNvService;
import io.team.service.logic.novel.NvCmtServiceLogic;
import io.team.service.logic.novel.NvCoverServiceLogic;
import io.team.service.logic.novel.NvServiceLogic;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NovelController {

	private final NvServiceLogic nvServiceLogic;

	private final NvCoverServiceLogic nvCoverServiceLogic;

	private final NvCmtServiceLogic nvCmtServiceLogic;

	private final PointServiceLogic pointServiceLogic;

	private final SubscribeNvService subscribeNvService;

	private final JwtManager jwtManager;

	@GetMapping("/novels/detail")
	public @ResponseBody Map<String, Object> getAllNovels(
			@RequestParam(value = "page", required = false, defaultValue = "1") String pagenum) {

		ArrayList<Novel> boards = nvServiceLogic.getList(Integer.parseInt(pagenum));
		int page = nvServiceLogic.getPageNum();

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("boards", boards);
		result.put("pagenum", page);

		return result;
	}

	@GetMapping("/novels/detail/{titleId}")
	public ResponseEntity read(@PathVariable int titleId, @RequestParam(value = "nv_id") int nv_id,
			HttpServletRequest req) {

		String token = req.getHeader("Authorization");
		NovelCover novelCover = nvCoverServiceLogic.find(titleId);

		int hitcount = novelCover.getNvc_hit();
		novelCover.setNvc_hit(hitcount + 1);

		nvCoverServiceLogic.modify(titleId, novelCover, null);
		nvServiceLogic.countCheck(nv_id);
		Novel novel = new Novel();

		try {

			Map<String, Object> result = new HashMap<String, Object>();

			novel = nvServiceLogic.find(nv_id);
			int checkMem_id = jwtManager.getIdFromToken(token);
			
			int check = pointServiceLogic.readNovel(PointPurpose.READNOVEL, novel.getNv_point(), nv_id,
					novel.getMem_id(), checkMem_id);

			switch (check) {
			case -1:
				result.put("msg", "point lack");
				return new ResponseEntity<>(result, HttpStatus.OK);
			case -2:
				throw new Exception();
			default:
				break;
			}

			return new ResponseEntity<>(novel, HttpStatus.OK);

		} catch (ExpiredJwtException e) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@SuppressWarnings("finally")
	@PostMapping("/novels/detail/{titleId}")
	public ResponseEntity write(@PathVariable int titleId, @RequestBody HashMap<String, Object> map,
			HttpServletRequest req) {

		String token = req.getHeader("Authorization");
		int check = 0;

		HashMap<String, String> hashMap = (HashMap<String, String>) map.get("novel");
		if (!hashMap.containsKey("nv_point")) {
			hashMap.put("nv_point", "10");
		}
		Novel novel = new Novel(hashMap);

		Map<String, Object> result = new HashMap<String, Object>();

		try {
			if (Integer.parseInt((String) map.get("parent")) == 0) { // 1화를 처음작성하면 표지생성
				int nv_id = nvServiceLogic.register(novel, token);

				if (nv_id != 1) {
					throw new Exception();
				}
				NovelCover novelCover = nvCoverServiceLogic.find(titleId);
				novelCover.setNvid(nv_id);
				check += nvCoverServiceLogic.modify(titleId, novelCover, token);
				result.put("msg", check);

			} else { // 1화가 아니면 부모자식테이블, 모든에피소드테이블에 등록
				check += nvServiceLogic.register(novel, token, Integer.parseInt((String) map.get("parent")), titleId);
				result.put("msg", check);
			}

		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} finally {

			//
			// subscribeNvService.pushSubscribeNv(check);
			//
			pointServiceLogic.writeNovel(novel.getMem_id(), PointPurpose.WRITENOVEL, 50, token);
			return new ResponseEntity<>(result, HttpStatus.OK);

		}
	}

	@PutMapping("/novels/detail/{titleId}")
	public @ResponseBody Map<String, Object> update(@PathVariable int titleId, @RequestParam(value = "nv_id") int nv_id,
			@RequestBody Novel newNovel, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", nvServiceLogic.modify(nv_id, newNovel, token));
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

	@DeleteMapping("/novels/detail/{titleId}")
	public @ResponseBody Map<String, Object> delete(@PathVariable int titleId, @RequestParam(value = "nv_id") int nv_id,
			HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", nvServiceLogic.remove(titleId, token));
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

	// 댓글작성

	@GetMapping("/novels/detail/{titleId}/cmts")
	public @ResponseBody Map<String, Object> getCmts(@PathVariable int titleId,
			@RequestParam(value = "nv_id") int nv_id,
			@RequestParam(value = "page", required = false, defaultValue = "1") String pagenum) {

		ArrayList<NovelCmt> cmts = nvCmtServiceLogic.getCmtList(nv_id, Integer.parseInt(pagenum));

		Map<String, Object> result = new HashMap<String, Object>();
		int page = nvCmtServiceLogic.getPageNum(nv_id);

		ArrayList<ArrayList<NovelCmt>> cmtsArray = new ArrayList<ArrayList<NovelCmt>>();

		for (NovelCmt cmt : cmts) {
			ArrayList<NovelCmt> tempArrayList = new ArrayList<NovelCmt>();
			tempArrayList.add(cmt);
			ArrayList<NovelCmt> replies = nvCmtServiceLogic.read_replies(cmt.getNv_cmt_id());
			tempArrayList.addAll(replies);
			cmtsArray.add(tempArrayList);
		}

		result.put("comments", cmtsArray);
		result.put("pagenum", page);

		return result;
	}

	@PostMapping("/novels/detail/{titleId}/cmts")
	public @ResponseBody Map<String, Object> writeCmt(@PathVariable int titleId,
			@RequestParam(value = "nv_id") int nv_id, @RequestBody NovelCmt newCmt, HttpServletRequest req) {
		String token = req.getHeader("Authorization");

		Map<String, Object> result = new HashMap<String, Object>();
		try {

			newCmt.setNv_id(nv_id);
			result.put("msg", nvCmtServiceLogic.register(newCmt, token));
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
	public @ResponseBody Map<String, Object> updateCmt(@RequestParam int nv_cmt_id, @RequestBody NovelCmt newCmt,
			HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", nvCmtServiceLogic.modify(nv_cmt_id, newCmt, token));
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
	public @ResponseBody Map<String, Object> deleteCmt(@RequestParam int nv_cmt_id, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", nvCmtServiceLogic.remove(nv_cmt_id, token));
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

}
