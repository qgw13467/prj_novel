package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import io.jsonwebtoken.ExpiredJwtException;
import io.team.domain.Novel;
import io.team.domain.NovelCover;
import io.team.domain.NovelLink;
import io.team.domain.PurchaseList;
import io.team.domain.User;
import io.team.jwt.JwtManager;
import io.team.mapper.UserMapper;
import io.team.service.logic.PointServiceLogic;
import io.team.service.logic.UserServicLogic;
import io.team.service.logic.novel.NvCoverServiceLogic;
import io.team.service.logic.novel.NvServiceLogic;
import io.team.service.logic.user.PurchaseService;
import lombok.RequiredArgsConstructor;


@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserServicLogic userServicLogic;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
	private final JwtManager jwtManager;

	private final PointServiceLogic pointServiceLogic;
	
	private final PurchaseService purchaseService;
	
	private final NvServiceLogic nvServiceLogic;
	
	private final NvCoverServiceLogic nvCoverServiceLogic;
	
	
//	@PostMapping("/login")
//	@ResponseBody
//	public HashMap find(@RequestBody User newUser, HttpServletResponse response) {
//		HashMap<String,String> map = new HashMap<>();
//		try {
//			String token = userServicLogic.makeToken(newUser);
//			map=userServicLogic.find(newUser);
//			int attendance_point = 100;
//			int check = pointServiceLogic.attend(Integer.parseInt(map.get("mem_id")), PointPurpose.ATTENDANCE, attendance_point, map.get("mem_lastlogin_datetime"));
//
//			if(check == 1) {
//				map.put("attendance point", ""+attendance_point);
//				
//			}
//			else {
//				map.put("attendance point", "0");
//			}
//			map.put("Authorization", token);
//			response.setHeader("Authorization", token);
//			return map;
//		}catch (Exception e) {
//			e.printStackTrace();
//			map.put("msg", "ERROR");
//			return map;
//		}
//		
//	}
	
	@PostMapping("/join")
	public HashMap register(@RequestBody User newUser) {
		HashMap<String,String> map=new HashMap<String, String>();
		
		String pwd = newUser.getMem_password();
		String encPwd = bCryptPasswordEncoder.encode(pwd);
		newUser.setMem_password(encPwd);
		
		map.put("msg", userServicLogic.register(newUser));
		return map;
	}
	
	
	@PutMapping("/users")
	public Map<String, Object> modify(@RequestBody User newUser,HttpServletRequest req ) {
		
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		
		String pwd = newUser.getMem_password();
		String encPwd = bCryptPasswordEncoder.encode(pwd);
		newUser.setMem_password(encPwd);
		
		try {
			result.put("msg", userServicLogic.modify(newUser, token));
			return result;
		}
		catch (Exception e){
			result.put("msg", "ERROR");
			return result;
		}
		
	}
	
	
	@DeleteMapping("/users")
	public Map<String, Object> remove(@RequestBody User newUser,HttpServletRequest req) {

		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result.put("msg", userServicLogic.remove(newUser, token));
			return result;
		}
		catch (Exception e){
			result.put("msg", "ERROR");
			return result;
		}
		
	}	
	

	
	@GetMapping("/users/purchase")
	public ResponseEntity<?> getPurchaseList(HttpServletRequest req ) {
		
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			ArrayList<PurchaseList> purchaseLists = purchaseService.getPurchaseList(mem_id);
			ArrayList<Novel> novels = new ArrayList<>();
			ArrayList<NovelCover> novelCovers = new ArrayList<>();
			
			HashSet<Integer> firstNvid = new HashSet<>();
			
			
			for (PurchaseList purchaseList : purchaseLists) {
				novels.add(nvServiceLogic.find(purchaseList.getNvid()));
			}
			System.out.println(novels);
			
			for (Novel novel : novels) {
				firstNvid.add(nvServiceLogic.findFirstNvid(novel.getNv_id()));
			}
			System.out.println(firstNvid);
			for (Integer integer : firstNvid) {
				novelCovers.add(nvCoverServiceLogic.findByNvid(integer));
			}
			
			 		
			return new ResponseEntity<>(novelCovers, HttpStatus.OK);
		}catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e){
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/users/purchase/{titleId}")
	public ResponseEntity<?> getPurchaseNovelList(@PathVariable int titleId, HttpServletRequest req ) {
		
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			
			ArrayList<Integer> puchaseNvId = new ArrayList<>();
			ArrayList<Novel> novels = new ArrayList<>();
			ArrayList<Novel> resultNovels = new ArrayList<>();
			ArrayList<NovelLink> novelLinks = new ArrayList<>();
			ArrayList<PurchaseList> purchaseLists = purchaseService.getPurchaseList(mem_id);
			
			System.out.println(purchaseLists);
			NovelCover novelCover = nvCoverServiceLogic.find(titleId);
			novelLinks = nvServiceLogic.findLinks(novelCover.getNvid());
			System.out.println(novelLinks);
			novels.add(nvServiceLogic.findInfo(novelCover.getNvid()));
			System.out.println(novels);
			
			
			for (PurchaseList purchaseList : purchaseLists) {
				if(purchaseList.getNvid()==novelCover.getNvid()) {
					puchaseNvId.add(purchaseList.getNvid());
				}
				for (NovelLink novelLink : novelLinks) {
//					
					if(novelLink.getNvlchildnode() == purchaseList.getNvid()) {

						puchaseNvId.add(purchaseList.getNvid());
					}
				}
			}
			
			for (Integer integer : puchaseNvId) {
				resultNovels.add(nvServiceLogic.findInfo(integer));
			}
			 		
			return new ResponseEntity<>(resultNovels, HttpStatus.OK);
		}catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e){
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/users/point")
	public ResponseEntity<?> getPoint(HttpServletRequest req ) {
		
		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			
			int point = userServicLogic.getPoint(mem_id);
			result.put("point", point);
			
			 		
			return new ResponseEntity<>(result, HttpStatus.OK);
		}catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e){
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}
		
	}
	
	@GetMapping("/test/login")
	public void test(Authentication authentication) {
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		System.out.println(oAuth2User.getAuthorities());
		
	}	

}
