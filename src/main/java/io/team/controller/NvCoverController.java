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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import io.team.domain.Novel;
import io.team.domain.NovelCover;
import io.team.domain.NovelLink;
import io.team.service.logic.NvCoverServiceLogic;
import io.team.service.logic.NvServiceLogic;
import io.team.service.logic.NvtagServiecLogic;

@RestController
public class NvCoverController {
	
	@Autowired
	NvCoverServiceLogic nvCoverServiceLogic;
	
	@Autowired
	NvServiceLogic nvServiceLogic;

	@Autowired
	NvtagServiecLogic nvtagServiecLogic;
	
	
	@GetMapping("/novels/genre")
    public ResponseEntity findByGenre(@RequestParam(value = "genre") String genre, Pageable pageable) {
		
		ArrayList<Integer> nvcList = nvtagServiecLogic.findByTagName(genre);
		
		Page<NovelCover> novelCover = nvCoverServiceLogic.findAll(nvcList,pageable);
		ArrayList<ArrayList<String>> tags = new ArrayList<ArrayList<String>>();
		for (Integer integer : nvcList) {
			ArrayList<String> tempList = nvtagServiecLogic.findByNvcId(integer);
			tags.add(tempList);
		}
		
		HashMap<String, Object> result=new HashMap<>();
		result.put("tags", tags);
		result.put("content",novelCover.getContent());
		result.put("pageable", pageable);
		result.put("totalElements", novelCover.getTotalElements());
		result.put("totalPages", novelCover.getTotalPages());
		result.put("size", novelCover.getSize());
		result.put("numberOfElements", novelCover.getNumberOfElements());

        return new ResponseEntity<>(result,HttpStatus.OK);
    }
	
	@GetMapping("/novels")
    public ResponseEntity findAllNvCover(Pageable pageable) {
		Page<NovelCover> novelCover = nvCoverServiceLogic.findAll(pageable);
		ArrayList<ArrayList<String>> tags = new ArrayList<ArrayList<String>>();
		for (NovelCover novelcvs : novelCover) {
			ArrayList<String> tempList = nvtagServiecLogic.findByNvcId(novelcvs.getNvcid());
			tags.add(tempList);
		}
		HashMap<String, Object> result=new HashMap<>();
		result.put("tags", tags);
		result.put("content",novelCover.getContent());
		result.put("pageable", pageable);
		result.put("totalElements", novelCover.getTotalElements());
		result.put("totalPages", novelCover.getTotalPages());
		result.put("size", novelCover.getSize());
		result.put("numberOfElements", novelCover.getNumberOfElements());
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

	@GetMapping("/novels/{id}")
    public ResponseEntity findByNvCover(@PathVariable int id, Pageable pageable) {
		
		NovelCover novelCover= nvCoverServiceLogic.find(id);
		int hitcount = novelCover.getNvc_hit();
		novelCover.setNvc_hit(hitcount+1);
		Queue<Integer> queue=new LinkedList<Integer>();
		queue.add(novelCover.getNvid());
		HashSet<Integer> node=new HashSet<Integer>();
		HashMap<Integer, ArrayList<Integer>> novelLinkMap = new LinkedHashMap<Integer, ArrayList<Integer>>();
		HashMap<String, Object> result=new HashMap<>();
		
		
		ArrayList<NovelLink> novelLinks = nvServiceLogic.findLinks(novelCover.getNvid());
		ArrayList<Novel> novelList=new ArrayList<>(); 
		HashMap<Integer, Novel> novelMap=new HashMap<>();
		for (NovelLink novelLink : novelLinks) {
			node.add(novelLink.getNvlparents());
			node.add(novelLink.getNvl_childnode());
		}
		
		for (Integer key : node) {
			Novel tempNovel = nvServiceLogic.findInfo(key);
			novelList.add(tempNovel);
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
		result.put("NovelInfo", novelList);
		result.put("NovelCover", novelCover);

        return new ResponseEntity<>(result,HttpStatus.OK);
    }

	
	@PostMapping("/novels")
	public @ResponseBody Map<String, Object> update(@RequestBody HashMap<String, Object> map, HttpServletRequest req) {
		
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		ArrayList<String> tagList =new ArrayList<>();
		HashMap<String, String>  novelCoverHashMap =(HashMap<String, String>) map.get("novelCover");
		NovelCover novelCover = new NovelCover(novelCoverHashMap);
		novelCover.setNvid(0);
		System.out.println(novelCover);
		
		tagList= (ArrayList<String>)map.get("tag");
		try {
			
			int nvCoverId= nvCoverServiceLogic.register(novelCover ,token);
			result.put("msg",  nvCoverServiceLogic.register(novelCover,token));
			for (String string : tagList) {
				nvtagServiecLogic.register(nvCoverId,string);
			}
			result.put("msg", nvCoverId );

			return result;
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return result;
		}
	}
}
