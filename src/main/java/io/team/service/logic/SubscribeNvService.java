package io.team.service.logic;

import java.util.ArrayList;

import javax.servlet.http.HttpServletResponse;

import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
import org.springframework.stereotype.Service;

import io.team.Repository.SubNvRepository;
import io.team.domain.NovelCover;
import io.team.domain.SubscribeNovel;
import io.team.domain.User;
import io.team.service.logic.novel.NvCoverServiceLogic;
import io.team.service.logic.user.UserServicLogic;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscribeNvService {

	private final SubNvRepository subNvRepository;

	private final NvCoverServiceLogic nvCoverServiceLogic;

	private final FcmService fcmService;

	private final UserServicLogic userServicLogic;

	public int subscribeNv(int mem_id, int nvc_id) {

		try {
			SubscribeNovel subscribeNovel = SubscribeNovel.builder().memId(mem_id).nvcId(nvc_id).build();

			SubscribeNovel temp = new SubscribeNovel();
			temp = subNvRepository.findFirstByMemIdAndNvcId(mem_id, nvc_id);

			if (temp == null) {
				subNvRepository.save(subscribeNovel);
			}

//			else if (temp.getToken().equals(subscribeNovel.getToken())) {
//				return 1;
//			} else {
//				temp.setToken(subscribeNovel.getToken());
//				subNvRepository.save(temp);
//			}

			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

	public ArrayList<NovelCover> getSubList(int mem_id) {

		ArrayList<NovelCover> novelCovers = new ArrayList<>();
		ArrayList<SubscribeNovel> subscribeNovels = new ArrayList<>();

		subscribeNovels = subNvRepository.findNvcIdByMemId(mem_id);

		for (SubscribeNovel subscribeNovel : subscribeNovels) {
			NovelCover novelCover = nvCoverServiceLogic.find(subscribeNovel.getNvcId());
			novelCovers.add(novelCover);
		}

		return novelCovers;
	}

	public int deleteSubscribe(int mem_id, int nvc_id) {
		SubscribeNovel subscribeNovel = subNvRepository.findByMemIdAndNvcId(mem_id, nvc_id);
		System.out.println(subscribeNovel);
		subNvRepository.delete(subscribeNovel);
		return 1;
	}

	// 소설커버아이디, 제목, 내용입력시 구독자에게 푸시알림
	public int pushSubscribeNv(int nvc_id, String title, String contents) {
		ArrayList<SubscribeNovel> subscribeNovels = new ArrayList<>();

		try {
			subscribeNovels = subNvRepository.findByNvcId(nvc_id);

			for (SubscribeNovel subscribeNovel : subscribeNovels) {
				User user = userServicLogic.findByMemId(subscribeNovel.getMemId());

				if (user.getToken().equals(""))
					continue;
				fcmService.send_FCMtoken(user.getToken(), title, contents);

			}

			return 1;

		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}

}
