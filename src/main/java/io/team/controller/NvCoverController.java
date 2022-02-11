package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.HttpHead;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import io.team.domain.NovelCover;
import io.team.mapper.NvCoverMapper;
import io.team.service.logic.NvCoverServiceLogic;

@RestController
public class NvCoverController {
	
	@Autowired
	NvCoverServiceLogic nvCoverServiceLogic;
	
	@Autowired
	NvCoverMapper nvCoverMapper;
	
	@GetMapping("/novels")
    public ResponseEntity findAllNvCover(Pageable pageable) {
        Page<NovelCover> posts = nvCoverServiceLogic.findAll(pageable);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }

	@GetMapping("/novels/list")
    public ResponseEntity findByNvCover(@RequestParam(value = "titleId") String nvc_id,
    		Pageable pageable) {
        Page<NovelCover> posts = nvCoverServiceLogic.findAll(pageable);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }


	
	@PostMapping("/newTitle")
	public @ResponseBody Map<String, Object> update(@RequestBody NovelCover newNovelCover, HttpServletRequest req) {
		
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		System.out.println(newNovelCover);
		try {
			nvCoverMapper.save(newNovelCover);
			result.put("msg",  nvCoverServiceLogic.register(newNovelCover,token));
			return result;
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return result;
		}
	}
}
