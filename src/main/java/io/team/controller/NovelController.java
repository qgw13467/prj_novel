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
import io.team.domain.dto.BoardDTO;
import io.team.domain.dto.NovelDTO;
import io.team.domain.dto.UserInfoDTO;
import io.team.jwt.JwtManager;
import io.team.service.logic.PointServiceLogic;
import io.team.service.logic.S3Servicelogic;
import io.team.service.logic.kafka.KafkaProducer;
import io.team.service.logic.novel.NvCmtServiceLogic;
import io.team.service.logic.novel.NvCoverServiceLogic;
import io.team.service.logic.novel.NvServiceLogic;
import io.team.service.logic.user.PurchaseService;
import io.team.service.logic.user.UserServiceLogic;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NovelController {

	private final NvServiceLogic nvServiceLogic;
	private final NvCoverServiceLogic nvCoverServiceLogic;
	private final PointServiceLogic pointServiceLogic;
	private final JwtManager jwtManager;
	private final KafkaProducer kafkaProducer;
	private final PurchaseService purchaseService;
	private final S3Servicelogic s3Servicelogic;
	private final UserServiceLogic userServicLogic;

//	// 소설의 전체 에시포드 보기
//	@GetMapping("/novels/detail")
//	public @ResponseBody Map<String, Object> getAllNovels(
//			@RequestParam(value = "page", required = false, defaultValue = "1") String pagenum,
//			@RequestParam(value = "rownum", required = false, defaultValue = "10") String rownum) {
//
//		ArrayList<Novel> boards = nvServiceLogic.getList(Integer.parseInt(pagenum), Integer.parseInt(rownum));
//		int page = nvServiceLogic.getPageNum();
//
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("boards", boards);
//		result.put("pagenum", page);
//
//		return result;
//	}

	// 소설의 한 에피소드 보기
	@GetMapping("/novels/detail/{titleId}")
	public ResponseEntity<?> read(@PathVariable int titleId, @RequestParam(value = "nv-id") int nv_id,
			HttpServletRequest req) {

		String token = req.getHeader("Authorization");
		Novel novel = new Novel();
		Map<String, Object> result = new HashMap<String, Object>();

		try {

			novel = nvServiceLogic.find(nv_id);
			int checkMem_id = jwtManager.getIdFromToken(token);
			
			if(novel.getNvState() == 1 ) {
				novel.setNvTitle("삭제된 에피소드");
				novel.setNvContents("삭제된 에피소드 입니다. 누구나 해당 에피소드를 수정할 수 있습니다");
				novel.setImgUrl("0");
				NovelDTO novelDTO = NovelDTO.novelDTOfromNovel(novel);
				ArrayList<String> imgUrls = s3Servicelogic.findByEventId(Integer.parseInt(novel.getImgUrl()));

				novelDTO.setImgUrls(imgUrls);

				result.put("novel", novelDTO);
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			
			int check = pointServiceLogic.readNovel(PointPurpose.READNOVEL, novel.getNvPoint(), nv_id, novel.getMemId(),
					checkMem_id);

			// 포인트부족
			if (check == -1) {
				result.put("msg", "point lack");
				return new ResponseEntity<>(result, HttpStatus.OK);
			}

			// 커버 조회수++1
			NovelCover novelCover = nvCoverServiceLogic.find(titleId);
			int hitcount = novelCover.getNvcHit();
			novelCover.setNvcHit(hitcount + 1);
			nvCoverServiceLogic.modify(titleId, novelCover, token);
			// 에피소드 조회수++1
			nvServiceLogic.countCheck(nv_id);

			NovelDTO novelDTO = NovelDTO.novelDTOfromNovel(novel);
			ArrayList<String> imgUrls = s3Servicelogic.findByEventId(Integer.parseInt(novel.getImgUrl()));

			novelDTO.setImgUrls(imgUrls);

			UserInfoDTO userInfoDTO = UserInfoDTO.userInfoDTOfromUser(userServicLogic.findByMemId(novelDTO.getMemId()));

			result.put("novel", novelDTO);
			result.put("user", userInfoDTO);
			return new ResponseEntity<>(result, HttpStatus.OK);

		} catch (ExpiredJwtException e) {
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
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
			hashMap.put("memId", Integer.toString(memId));
			if (!hashMap.containsKey("nvPoint") || Integer.parseInt(hashMap.get("nvPoint")) == 0) {
				hashMap.put("nvPoint", "10");
			}
			Novel novel = new Novel(hashMap);
			NovelCover novelCover = nvCoverServiceLogic.find(titleId);
			int parent = Integer.parseInt((String) map.get("parent"));
			if (novelCover.getNvId() == 0) { // 1화를 처음작성하면 표지생성

				int nv_id = nvServiceLogic.register(novel, token);
				novelCover.setNvId(nv_id);
				check += nvCoverServiceLogic.modify(titleId, novelCover, token);
				result.put("msg", "OK");

			} else { // 1화가 아니면 부모자식테이블, 모든에피소드테이블에 등록
				if (parent == 0) {
					result.put("msg", "exist");
					return new ResponseEntity<>(result, HttpStatus.OK);
				}

				int firstNvid = nvServiceLogic.findFirstNvid(parent);
				NovelCover parentNovelCover = nvCoverServiceLogic.findByNvid(firstNvid);
				if (parentNovelCover.getNvcId() != titleId) {
					result.put("msg", "parent episodes not included");
					return new ResponseEntity<>(result, HttpStatus.OK);
				}

				check += nvServiceLogic.register(novel, token, Integer.parseInt((String) map.get("parent")), titleId);
				result.put("msg", "OK");
			}

			// 소설 작성시 구독 사용자에게 푸시알림
			String title = "구독 알림";
			String contents = "구독하신 소설 " + novelCover.getNvcTitle() + "의 최신화가 나왔습니다";

			HashMap<String, String> msg = new HashMap<>();
			msg.put("titleId", Integer.toString(titleId));
			msg.put("title", title);
			msg.put("contents", contents);
			JSONObject json = new JSONObject(msg);

			kafkaProducer.sendMessage(json.toJSONString());


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
	public @ResponseBody Map<String, Object> update(@PathVariable int titleId, @RequestParam(value = "nv-id") int nv_id,
			@RequestBody Novel newNovel, HttpServletRequest req) {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int memId = jwtManager.getIdFromToken(token);
			newNovel.setMemId(memId);
			newNovel.setNvId(nv_id);
			int state = nvServiceLogic.modify(newNovel.getNvId(), newNovel, token);
			if(state ==1) {
				result.put("msg", "OK");
				return result;
			}
			else {
				result.put("msg", "not your episode");
				return result;
			}
			
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
			int state =nvServiceLogic.remove(nv_id, token);
			
			if(state ==1) {
				result.put("msg", "OK");
				return result;
			}
			else {
				result.put("msg", "not your episode");
				return result;
			}
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

			if (!purchaseService.isPurchased(mem_id, id)) {
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
