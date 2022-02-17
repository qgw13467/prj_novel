package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import io.team.domain.Novel;
import io.team.domain.NovelCover;
import io.team.domain.NovelLink;
import io.team.service.logic.NvCoverServiceLogic;
import io.team.service.logic.NvServiceLogic;

@RestController
public class NvCoverController {
	
	@Autowired
	NvCoverServiceLogic nvCoverServiceLogic;
	
	@Autowired
	NvServiceLogic nvServiceLogic;


	
	
	@GetMapping("/novels")
    public ResponseEntity findAllNvCover(Pageable pageable) {
        Page<NovelCover> posts = nvCoverServiceLogic.findAll(pageable);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }

	@GetMapping("/novels/{id}")
    public ResponseEntity findByNvCover(@PathVariable int id, Pageable pageable) {
		
		NovelCover novelCover= nvCoverServiceLogic.find(id);
		Queue<Integer> queue=new LinkedList<Integer>();
		queue.add(novelCover.getNvid());
		HashSet<Integer> node=new HashSet<Integer>();
		HashMap<Integer, ArrayList<Integer>> novelLinkMap = new LinkedHashMap<Integer, ArrayList<Integer>>();
		HashMap<String, Object> result=new HashMap<>();
		
		
		ArrayList<NovelLink> novelLinks = nvServiceLogic.findLinks(novelCover.getNvid());
		HashMap<Integer, Novel> novelMap=new HashMap<>();
		for (NovelLink novelLink : novelLinks) {
			node.add(novelLink.getNvlparents());
			node.add(novelLink.getNvl_childnode());
		}
		
		for (Integer key : node) {
			Novel tempNovel = nvServiceLogic.findInfo(key);
			novelMap.put(key, tempNovel);
		}

		while(!queue.isEmpty()) {
			int parent=queue.poll();
			ArrayList<Integer> tempList = new ArrayList<>();
			for (NovelLink novelLink : novelLinks) {
				if(novelLink.getNvlparents()==parent) {
					tempList.add(novelLink.getNvl_childnode());
					queue.add(novelLink.getNvl_childnode());
					
				}
			}
			novelLinkMap.put(parent, tempList);
		}
		result.put("episode", novelLinkMap);
		result.put("NovelInfo", novelMap);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

	
	@PostMapping("/novels")
	public @ResponseBody Map<String, Object> update(@RequestBody NovelCover newNovelCover, HttpServletRequest req) {
		
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		System.out.println(newNovelCover);
		try {
			result.put("msg",  nvCoverServiceLogic.register(newNovelCover,token));
			return result;
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return result;
		}
	}
}
