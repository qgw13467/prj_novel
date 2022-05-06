package io.team.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.jsonwebtoken.ExpiredJwtException;
import io.team.jwt.JwtManager;
import io.team.service.logic.S3Servicelogic;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class S3Controller {

	private final JwtManager jwtManager;

	private final S3Servicelogic s3Service;

	@PostMapping("/upload")
	public ResponseEntity<?> upload(@RequestParam("images") List<MultipartFile> multipartFiles,
			HttpServletRequest req) throws IOException {
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			ArrayList<Integer> img_ids = new ArrayList<Integer>();
			for (MultipartFile multipartFile : multipartFiles) {

				img_ids.add(s3Service.upload(multipartFile, multipartFile.getOriginalFilename(), mem_id));
				if(multipartFiles.size()==1) {
					
					String img_url = s3Service.findUrlById(img_ids.get(0));
					result.put("imgUrl", img_url);
				}
			}
			s3Service.saveImgIds(img_ids);
			
			
			result.put("msg", img_ids.get(0));
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ExpiredJwtException e) {
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@GetMapping("/imgs/{id}")
	public @ResponseBody Map<String, Object> getImgUrl(@PathVariable int id) throws IOException {

		String imgUrl = s3Service.findUrlById(id);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("imgUrl", imgUrl);
		return result;
	}
}
