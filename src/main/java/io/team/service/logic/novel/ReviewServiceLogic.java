package io.team.service.logic.novel;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.team.domain.Review;
import io.team.jwt.JwtManager;
import io.team.mapper.ReviewMapper;

@Service
public class ReviewServiceLogic {
	
	@Autowired
	JwtManager jwtManager;

	@Autowired
	ReviewMapper reviewMapper;
	
	@Autowired
	NvServiceLogic nvServiceLogic;
	
	public int register(Review newReview, String token) {
		
		int mem_id = jwtManager.getIdFromToken(token);
		if (newReview.getMemid() == mem_id) {
			reviewMapper.save(newReview);
			nvServiceLogic.review(newReview.getNvid(), newReview.getRvpoint());
			return 1;
		} else {
			return -1;
		}
	}
	
	public int findPastReview(int nv_id, int mem_id) {
		try {
			Review review= reviewMapper.findByNvidAndMemid(nv_id,mem_id);
			System.out.println(review);
			if(review == null) {
				return 0;
			}
			return 1;
		}catch (Exception e) {
			return -1;
		}
		
	}
	
	
	/*
	public int modify(Review newReview, String token) {
		
		int mem_id = jwtManager.getIdFromToken(token);
		Review pastReview = reviewMapper.findByNvidAndMemid(newReview.getNvid(), newReview.getMemid());
		System.out.println(pastReview);
		if (newReview.getMemid() == mem_id) {
			pastReview.setRvpoint(newReview.getRvpoint());
			return 1;
		} else {
			return -1;
		}
	}
	*/


}
