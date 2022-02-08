package io.team.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


import io.team.jwt.JwtManager;
import io.team.service.logic.S3Servicelogic;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class S3Controller {

	@Autowired
	JwtManager jwtManager;
	
	@Autowired
	private S3Servicelogic s3Service;

	@PostMapping("/upload")
    public @ResponseBody Map<String, Object> upload(@RequestParam("images") MultipartFile multipartFile, HttpServletRequest req) throws IOException {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			int img_id= s3Service.upload(multipartFile,  multipartFile.getOriginalFilename(), token);
			result.put("msg", img_id);
			return result;
		}
		catch (Exception e){
			result.put("msg", "ERROR");
			return result;
		}

    }

	@GetMapping("/imgs/{id}")
    public @ResponseBody Map<String, Object> getImgUrl(@PathVariable int id) throws IOException {
		
		String img_url= s3Service.findUrlById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("img_url", img_url);
        return result;
    }
}
