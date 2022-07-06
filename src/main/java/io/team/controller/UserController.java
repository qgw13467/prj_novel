package io.team.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import io.team.service.logic.novel.NvCoverServiceLogic;
import io.team.service.logic.novel.NvServiceLogic;
import io.team.service.logic.user.PurchaseService;
import io.team.service.logic.user.UserServiceLogic;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserServiceLogic userServicLogic;

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	private final JwtManager jwtManager;

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
		HashMap<String, String> map = new HashMap<String, String>();

		String pwd = newUser.getMemPassword();
		String encPwd = bCryptPasswordEncoder.encode(pwd);
		newUser.setMemPassword(encPwd);
		String resultString = userServicLogic.register(newUser);
		map.put("msg", resultString);
		return map;
	}

	@PutMapping("/users")
	public ResponseEntity<?> modify(@RequestBody User newUser, HttpServletRequest req) {

		Map<String, Object> result = new HashMap<String, Object>();

		try {
			String token = req.getHeader("Authorization");
			int mem_id = jwtManager.getIdFromToken(token);
			newUser.setMemId(mem_id);
			int checkNick = userServicLogic.modify(newUser, token);

			if (checkNick == -1) {
				result.put("msg", "nickname reduplication");
			} else {
				result.put("msg", "OK");
			}

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

	@PutMapping("/users/pwd")
	public ResponseEntity<?> modifypwd(@RequestBody User newUser, HttpServletRequest req) {

		Map<String, Object> result = new HashMap<String, Object>();

		try {
			String token = req.getHeader("Authorization");
			int mem_id = jwtManager.getIdFromToken(token);
			String pwd = newUser.getMemPassword();
			String encPwd = bCryptPasswordEncoder.encode(pwd);
			int check = userServicLogic.modify(mem_id, encPwd, pwd);
			if (check == -2) {
				result.put("msg", "same as the previous ");
				return new ResponseEntity<>(result, HttpStatus.OK);
			}

			result.put("msg", "OK");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@DeleteMapping("/users")
	public ResponseEntity<?> remove( HttpServletRequest req) {

		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String token = req.getHeader("Authorization");
			String mem_userIdString = jwtManager.getUserIdFromToken(token);
			User newUser = new User();
			newUser.setMemUserId(mem_userIdString);
			userServicLogic.remove(newUser, token);
			result.put("msg", "OK");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@PutMapping("/users/token")
	public ResponseEntity<?> updateToken(@RequestBody HashMap<String, String> map, HttpServletRequest req) {

		Map<String, Object> result = new HashMap<String, Object>();

		try {
			String token = req.getHeader("Authorization");
			int mem_id = jwtManager.getIdFromToken(token);
			userServicLogic.updateToken(map.get("token"), mem_id);
			result.put("msg", "OK");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@PutMapping("/users/refresh")
	public ResponseEntity<?> refreshToken(HttpServletRequest req, HttpServletResponse res) {

		Map<String, Object> result = new HashMap<String, Object>();

		try {
			String token = req.getHeader("Authorization");
			int mem_id = jwtManager.getIdFromToken(token);
			int check = jwtManager.isRefreshToken(token);
			if (mem_id != check) {
				result.put("msg", "Not RefreshToken");
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
			User user = userServicLogic.findByMemId(mem_id);
			String newToken = jwtManager.generateJwtToken(user);
			res.addHeader("Authorization", newToken);
			result.put("msg", "OK");
			return new ResponseEntity<>(result, HttpStatus.OK);

		} catch (SecurityException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "Invalid JWT signature");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

//		 catch (MalformedJwtException e) {
//				result = new HashMap<String, Object>();
//				result.put("msg", "Invalid JWT token");
//				return new ResponseEntity<>(result, HttpStatus.OK);
//	        } catch (UnsupportedJwtException e) {
//	            result = new HashMap<String, Object>();
//				result.put("msg", "Unsupported JWT token");
//				return new ResponseEntity<>(result, HttpStatus.OK);
//	        } catch (IllegalArgumentException e) {
//	            result = new HashMap<String, Object>();
//				result.put("msg", "JWT token compact of handler are invalid");
//				return new ResponseEntity<>(result, HttpStatus.OK);
//	        }
	}

	@GetMapping("/users/purchase")
	public ResponseEntity<?> getPurchaseList(HttpServletRequest req) {

		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			int mem_id = jwtManager.getIdFromToken(token);
			ArrayList<PurchaseList> purchaseLists = purchaseService.getPurchaseList(mem_id);
			ArrayList<Novel> novels = new ArrayList<>();
			ArrayList<NovelCover> novelCovers = new ArrayList<>();

			HashSet<Integer> firstNvid = new HashSet<>();

			for (PurchaseList purchaseList : purchaseLists) {
				novels.add(nvServiceLogic.find(purchaseList.getNvId()));
			}
			System.out.println(novels);

			for (Novel novel : novels) {
				firstNvid.add(nvServiceLogic.findFirstNvid(novel.getNvId()));
			}
			System.out.println(firstNvid);
			for (Integer integer : firstNvid) {
				novelCovers.add(nvCoverServiceLogic.findByNvid(integer));
			}

			return new ResponseEntity<>(novelCovers, HttpStatus.OK);
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@GetMapping("/users/purchase/{titleId}")
	public ResponseEntity<?> getPurchaseNovelList(@PathVariable int titleId, HttpServletRequest req) {

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
			novelLinks = nvServiceLogic.findLinks(novelCover.getNvId());
			System.out.println(novelLinks);
			novels.add(nvServiceLogic.findInfo(novelCover.getNvId()));
			System.out.println(novels);

			for (PurchaseList purchaseList : purchaseLists) {
				if (purchaseList.getNvId() == novelCover.getNvId()) {
					puchaseNvId.add(purchaseList.getNvId());
				}
				for (NovelLink novelLink : novelLinks) {
//					
					if (novelLink.getNvlChildnode() == purchaseList.getNvId()) {

						puchaseNvId.add(purchaseList.getNvId());
					}
				}
			}

			for (Integer integer : puchaseNvId) {
				resultNovels.add(nvServiceLogic.findInfo(integer));
			}

			return new ResponseEntity<>(resultNovels, HttpStatus.OK);
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}

	@GetMapping("/users/point")
	public ResponseEntity<?> getPoint(HttpServletRequest req) {

		String token = req.getHeader("Authorization");
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			int mem_id = jwtManager.getIdFromToken(token);

			int point = userServicLogic.getPoint(mem_id);
			result.put("point", point);

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

}
