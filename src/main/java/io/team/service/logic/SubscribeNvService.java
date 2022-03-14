package io.team.service.logic;


import org.springframework.stereotype.Service;
import io.team.domain.SubscribeNovel;
import io.team.mapper.SubNvRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SubscribeNvService {
	
	private final SubNvRepository subNvRepository;
	
	public int subscribeNv(int mem_id, int nvc_id) {
		
		try {
			SubscribeNovel subscribeNovel = SubscribeNovel.builder()
					.mem_id(mem_id)
					.nvc_id(nvc_id)
					.build();
			subNvRepository.save(subscribeNovel);

			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public int pushSubscribeNv(int nvc_id) {
		
		try {
			
			return 1;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
}
