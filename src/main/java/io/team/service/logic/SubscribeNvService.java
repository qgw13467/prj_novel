package io.team.service.logic;


import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;


import io.team.domain.SubscribeNovel;
import io.team.mapper.SubNvRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscribeNvService {

	private final SubNvRepository subNvRepository;

	private final FcmService fcmService;


	public int subscribeNv(int mem_id, int nvc_id, String token) {

		try {
			SubscribeNovel subscribeNovel = SubscribeNovel.builder().memid(mem_id).nvcid(nvc_id).token(token).build();

			SubscribeNovel temp = new SubscribeNovel();
			temp = subNvRepository.findFirstByMemidAndNvcid(mem_id, nvc_id);
			System.out.println(temp);
			if (temp == null) {
				subNvRepository.save(subscribeNovel);
			} else if (temp.getToken().equals(subscribeNovel.getToken())) {
				return 1;
			} else {
				temp.setToken(subscribeNovel.getToken());
				subNvRepository.save(temp);
			}

			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public int pushSubscribeNv(HttpServletResponse res, int nvc_id, String title, String contents) {
		ArrayList<SubscribeNovel> tokens = new ArrayList<>();
		
		try {
			tokens = subNvRepository.findTokenByNvcid(nvc_id);
			for (SubscribeNovel subscribeNovel : tokens) {
				fcmService.send_FCMtoken(subscribeNovel.getToken(), title, contents);
				//fcmService2.send_FCM(res, subscribeNovel.getToken(), title, contents);
				
			}
			
			return 1;
			
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}


}
