package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import javax.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.ExpiredJwtException;
import io.team.domain.Novel;
import io.team.domain.NovelCover;
import io.team.domain.NovelLink;
import io.team.jwt.JwtManager;
import io.team.service.logic.SubscribeNvService;
import io.team.service.logic.novel.NvCoverServiceLogic;
import io.team.service.logic.novel.NvServiceLogic;
import io.team.service.logic.novel.NvtagServiecLogic;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NvCoverController {

	private final NvCoverServiceLogic nvCoverServiceLogic;

	private final NvServiceLogic nvServiceLogic;

	private final NvtagServiecLogic nvtagServiecLogic;

	private final SubscribeNvService subscribeNvService;

	private final JwtManager jwtManager;

	@GetMapping("/novels/genre")
	public ResponseEntity<?> findByGenre(@RequestParam(value = "genre") String genre, Pageable pageable) {

		ArrayList<Integer> nvcList = nvtagServiecLogic.findByTagName(genre);

		Page<NovelCover> novelCover = nvCoverServiceLogic.findAll(nvcList, pageable);
		ArrayList<ArrayList<String>> tags = new ArrayList<ArrayList<String>>();
		for (Integer integer : nvcList) {
			ArrayList<String> tempList = nvtagServiecLogic.findByNvcId(integer);
			tags.add(tempList);
		}

		HashMap<String, Object> result = new HashMap<>();
		result.put("tags", tags);
		result.put("content", novelCover.getContent());
		result.put("pageable", pageable);
		result.put("totalElements", novelCover.getTotalElements());
		result.put("totalPages", novelCover.getTotalPages());
		result.put("size", novelCover.getSize());
		result.put("numberOfElements", novelCover.getNumberOfElements());

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/novels")
	public ResponseEntity<?> findAllNvCover(Pageable pageable, HttpServletRequest req) {

		int mem_id = 0;
		String token = req.getHeader("Authorization");
		ArrayList<NovelCover> subNvCovers = new ArrayList<>();
		Page<NovelCover> novelCover = nvCoverServiceLogic.findAll(pageable);
		ArrayList<String> subscribe = new ArrayList<>();
		ArrayList<ArrayList<String>> tags = new ArrayList<ArrayList<String>>();

		// 토큰이 있을경우 memid를 구하고 만료확인
		if (token != null) {
			try {
				mem_id = jwtManager.getIdFromToken(token);
				subNvCovers = subscribeNvService.getSubList(mem_id);
			} catch (ExpiredJwtException e) {
				Map<String, Object> result = new HashMap<String, Object>();
				result.put("msg", "JWT expiration");
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
		}

		for (NovelCover novelcvs : novelCover) {
			ArrayList<String> tempList = nvtagServiecLogic.findByNvcId(novelcvs.getNvcId());
			tags.add(tempList);
			if (mem_id != 0) {
				int checkSub = 0;
				for (NovelCover nvc : subNvCovers) {
					if (novelcvs.getNvcId() == nvc.getNvcId()) {
						subscribe.add("check");
						checkSub = 1;
						break;
					}
				}
				if (checkSub == 0) {
					subscribe.add("uncheck");
				}

			}

		}
		HashMap<String, Object> result = new HashMap<>();
		result.put("tags", tags);
		result.put("content", novelCover.getContent());
		result.put("pageable", pageable);
		result.put("totalElements", novelCover.getTotalElements());
		result.put("totalPages", novelCover.getTotalPages());
		result.put("size", novelCover.getSize());
		result.put("numberOfElements", novelCover.getNumberOfElements());
		if (mem_id != 0) {
			result.put("subscribe", subscribe);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@GetMapping("/novels/{id}")
	public ResponseEntity<?> findByNvCover(@PathVariable int id, Pageable pageable) {

		NovelCover novelCover = nvCoverServiceLogic.find(id);
		int hitcount = novelCover.getNvcHit();
		novelCover.setNvcHit(hitcount + 1);
		Queue<Integer> queue = new LinkedList<Integer>();
		queue.add(novelCover.getNvId());
		HashSet<Integer> node = new HashSet<Integer>();
		HashMap<Integer, ArrayList<Integer>> novelLinkMap = new LinkedHashMap<Integer, ArrayList<Integer>>();
		HashMap<String, Object> result = new HashMap<>();

		ArrayList<NovelLink> novelLinks = nvServiceLogic.findLinks(novelCover.getNvId());
		ArrayList<Novel> novelList = new ArrayList<>();

		for (NovelLink novelLink : novelLinks) {
			node.add(novelLink.getNvlParents());
			node.add(novelLink.getNvlChildnode());
		}

		for (Integer key : node) {
			Novel tempNovel = nvServiceLogic.findInfo(key);
			novelList.add(tempNovel);
		}

		while (!queue.isEmpty()) {
			int parent = queue.poll();
			ArrayList<Integer> tempList = new ArrayList<>();
			for (NovelLink novelLink : novelLinks) {
				if (novelLink.getNvlParents() == parent) {
					tempList.add(novelLink.getNvlChildnode());
					queue.add(novelLink.getNvlChildnode());

				}
			}
			novelLinkMap.put(parent, tempList);
		}

		result.put("episode", novelLinkMap);
		result.put("NovelInfo", novelList);
		result.put("NovelCover", novelCover);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/novels")
	public ResponseEntity<?> update(@RequestBody HashMap<String, Object> map, HttpServletRequest req) {

		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		ArrayList<String> tagList = new ArrayList<>();
		HashMap<String, String> novelCoverHashMap = (HashMap<String, String>) map.get("novelCover");
		NovelCover novelCover = new NovelCover(novelCoverHashMap);
		novelCover.setNvId(0);
		System.out.println(novelCover);

		tagList = (ArrayList<String>) map.get("tag");
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			int nvCoverId = nvCoverServiceLogic.register(novelCover, token);
			
			for (String string : tagList) {
				nvtagServiecLogic.register(nvCoverId, string);
			}
			result.put("msg", "OK");

			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
	}

	@GetMapping("/novels/search")
	public ResponseEntity<?> findByTitleContain(@RequestParam(value = "keyword") String keyword, Pageable pageable) {

		Page<NovelCover> novelCover = nvCoverServiceLogic.findByTitleContain(keyword, pageable);

		return new ResponseEntity<>(novelCover, HttpStatus.OK);
	}
}
