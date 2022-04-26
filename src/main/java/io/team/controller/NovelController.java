package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
import io.team.domain.BrdReport;
import io.team.domain.Novel;
import io.team.domain.NovelCmt;
import io.team.domain.NovelCover;
import io.team.domain.NovelReport;
import io.team.domain.Enum.PointPurpose;
import io.team.jwt.JwtManager;
import io.team.service.logic.PointServiceLogic;
import io.team.service.logic.kafka.KafkaProducer;
import io.team.service.logic.novel.NvCmtServiceLogic;
import io.team.service.logic.novel.NvCoverServiceLogic;
import io.team.service.logic.novel.NvServiceLogic;
import io.team.service.logic.user.PurchaseService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NovelController {

	private final NvServiceLogic nvServiceLogic;

	private final NvCoverServiceLogic nvCoverServiceLogic;

	private final NvCmtServiceLogic nvCmtServiceLogic;

	private final PointServiceLogic pointServiceLogic;

	private final JwtManager jwtManager;

	private final KafkaProducer kafkaProducer;	

	private final PurchaseService purchaseService;	
	
	@GetMapping("/novels/detail")
	public @ResponseBody Map<String, Object> getAllNovels(
			@RequestParam(value = "page", required = false, defaultValue = "1") String pagenum,
			@RequestParam(value = "rownum", required = false, defaultValue = "10") String rownum) {

		ArrayList<Novel> boards = nvServiceLogic.getList(Integer.parseInt(pagenum), Integer.parseInt(rownum));
		int page = nvServiceLogic.getPageNum();

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("boards", boards);
		result.put("pagenum", page);

		return result;
	}

	@GetMapping("/novels/detail/{titleId}")
	public ResponseEntity<?> read(@PathVariable int titleId, @RequestParam(value = "nv-id") int nv_id,
			HttpServletRequest req) {

		String token = req.getHeader("Authorization");
		NovelCover novelCover = nvCoverServiceLogic.find(titleId);

		int hitcount = novelCover.getNvcHit();
		novelCover.setNvcHit(hitcount + 1);

		nvCoverServiceLogic.modify(titleId, novelCover, null);
		nvServiceLogic.countCheck(nv_id);
		Novel novel = new Novel();

		try {

			Map<String, Object> result = new HashMap<String, Object>();

			novel = nvServiceLogic.find(nv_id);
			int checkMem_id = jwtManager.getIdFromToken(token);

			int check = pointServiceLogic.readNovel(PointPurpose.READNOVEL, novel.getNvPoint(), nv_id, novel.getMemId(),
					checkMem_id);

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
	

	@Transactional
	@PostMapping("/novels/detail/{titleId}")
	public ResponseEntity<?> write(@PathVariable int titleId, @RequestBody HashMap<String, Object> map,
			HttpServletRequest req, HttpServletResponse res) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		String token = req.getHeader("Authorization");
		int check = 0;

		try {
			int memId = jwtManager.getIdFromToken(token);
			
			HashMap<String, String> hashMap = (HashMap<String, String>) map.get("novel");
			hashMap.put("memId",Integer.toString(memId));
			if (!hashMap.containsKey("nvPoint")) {
				hashMap.put("nvPoint", "10");
			}
			Novel novel = new Novel(hashMap);
			if (Integer.parseInt((String) map.get("parent")) == 0) { // 1화를 처음작성하면 표지생성
				
				int nv_id = nvServiceLogic.register(novel, token);
				NovelCover novelCover = nvCoverServiceLogic.find(titleId);
				novelCover.setNvId(nv_id);
				check += nvCoverServiceLogic.modify(titleId, novelCover, token);
				result.put("msg", check);

			} else { // 1화가 아니면 부모자식테이블, 모든에피소드테이블에 등록
				System.out.println(novel);
				check += nvServiceLogic.register(novel, token, Integer.parseInt((String) map.get("parent")), titleId);
				result.put("msg", check);
			}

			// 소설 작성시 구독 사용자에게 푸시알림

			NovelCover novelCover = nvCoverServiceLogic.find(titleId);
			String title = "구독 알림";
			String contents = "구독하신 소설 " + novelCover.getNvcTitle() + "의 최신화가 나왔습니다";

			HashMap<String, String> msg = new HashMap<>();
			msg.put("titleId", Integer.toString(titleId));
			msg.put("title", title);
			msg.put("contents", contents);
			JSONObject json = new JSONObject(msg);

			kafkaProducer.sendMessage(json.toJSONString());
			// subscribeNvService.pushSubscribeNv(titleId, title, contents);
			//

			pointServiceLogic.writeNovel(novel.getMemId(), PointPurpose.WRITENOVEL, 50, token);
			return new ResponseEntity<>(result, HttpStatus.OK);

		} catch (ExpiredJwtException e) {
			e.printStackTrace();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
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
	public @ResponseBody Map<String, Object> delete(@PathVariable int titleId, @RequestParam(value = "nv-id") int nv_id,
			HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", nvServiceLogic.remove(nv_id, token));
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
	public @ResponseBody Map<String, Object> writeCmt(@PathVariable int titleId, @RequestBody NovelCmt newCmt,
			HttpServletRequest req) {
		String token = req.getHeader("Authorization");

		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int memId = jwtManager.getIdFromToken(token);
			newCmt.setMemId(memId);
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
	public @ResponseBody Map<String, Object> updateCmt( @RequestBody NovelCmt newCmt,
			HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int memId = jwtManager.getIdFromToken(token);
			newCmt.setMemId(memId);
			result.put("msg", nvCmtServiceLogic.modify(newCmt.getNvCmtId(), newCmt, token));
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
	public @ResponseBody Map<String, Object> deleteCmt( @RequestParam(value = "nv-cmt-id") int nvCmtId, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", nvCmtServiceLogic.remove(nvCmtId, token));
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
	
	@PostMapping("/novels/{id}/report")
	public ResponseEntity<?> writeReport(@PathVariable int id, HttpServletRequest req,
			@RequestBody NovelReport novelReport) {

		String token = req.getHeader("Authorization");

		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			novelReport.setMemId(mem_id);
			novelReport.setNvId(id);
			Optional<NovelReport> optNovelReport = nvServiceLogic.findReport(id, mem_id);
			
			if(!purchaseService.isPurchased(mem_id, id)) {
				result.put("msg", "Not purchase");
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			
			if (!optNovelReport.isPresent()) {
				nvServiceLogic.report(novelReport);
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
