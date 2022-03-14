package io.team.controller;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.loader.plan.exec.process.internal.AbstractRowReader;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;

import io.jsonwebtoken.ExpiredJwtException;
import io.team.domain.SubscribeNovel;
import io.team.jwt.JwtManager;
import io.team.service.logic.SubscribeNvService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SubscribeController {

	private final SubscribeNvService subscribeNvService;

	private final JwtManager jwtManager;

	@PostMapping("/nvc")
	public ResponseEntity review(@RequestBody SubscribeNovel subscribeNovel, HttpServletRequest req) {

		HashMap<String, Object> result = new HashMap<>();
		String token = req.getHeader("Authorization");
		
		try {
			int mem_id = jwtManager.getIdFromToken(token);
			if (mem_id == subscribeNovel.getMem_id()) {
				subscribeNvService.subscribeNv(subscribeNovel.getMem_id(), subscribeNovel.getNvc_id());
				result.put("msg", "OK");
				return new ResponseEntity<>(result, HttpStatus.OK);
			} else {
				result.put("msg", "mem_id is mismatch");
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
		} catch (ExpiredJwtException e) {
			result = new HashMap<String, Object>();
			result.put("msg", "JWT expiration");
			return new ResponseEntity<>(result, HttpStatus.OK);
		} catch (Exception e) {
			result.put("msg", "ERROR");
			return new ResponseEntity<>(result, HttpStatus.OK);
		}

	}
	
	@PostMapping(value="/fcmTest", produces="text/plain;charset=UTF-8")
    public void fcmTest() throws Exception {
        try {    
            
            String path = "C:/** .. **/webapp/resources/google/{fcm-test-*******************************.json}";           
            String MESSAGING_SCOPE = "https://www.googleapis.com/auth/firebase.messaging";
            String[] SCOPES = { MESSAGING_SCOPE };
            
            GoogleCredential googleCredential = GoogleCredential
                                .fromStream(new FileInputStream(path))
                                .createScoped(Arrays.asList(SCOPES));
            googleCredential.refreshToken();
                                
            HttpHeaders headers = new HttpHeaders();
            headers.add("content-type" , MediaType.APPLICATION_JSON_VALUE);
            headers.add("Authorization", "Bearer " + googleCredential.getAccessToken());
            
            JSONObject notification = new JSONObject();
            notification.put("body", "TEST");
            notification.put("title", "TEST");
            
            JSONObject message = new JSONObject();
            message.put("token", "fa_qIyte8d4:APA91bHOGnZulT059PyK3z_sb1dIkDXTiZUIuRksmS7TdK6XgXAS5kopeGIwUfyhad3X3iXMNknCUOZaF6_mgoj1ohG10CanRyJ_EW1d3xN2E-1DPiLdbMK4pdOgdhB1ztZClqB-25rC");
            message.put("notification", notification);
            
            JSONObject jsonParams = new JSONObject();
            jsonParams.put("message", message);
            
            HttpEntity<JSONObject> httpEntity = new HttpEntity<JSONObject>(jsonParams, headers);
            RestTemplate rt = new RestTemplate();            
            
            ResponseEntity<String> res = rt.exchange("https://fcm.googleapis.com/v1/projects/{프로젝트명}/messages:send"
                    , HttpMethod.POST
                    , httpEntity
                    , String.class);
        
            if (res.getStatusCode() != HttpStatus.OK) {
            	System.out.println("FCM-Exception");
                System.out.println(res.getStatusCode().toString());
                System.out.println(res.getHeaders().toString());
                System.out.println(res.getBody().toString());
                
                
                
            } else {
            	System.out.println(res.getStatusCode().toString());
            	System.out.println(res.getHeaders().toString());
            	System.out.println  (res.getBody().toLowerCase());
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
