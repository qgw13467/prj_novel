package io.team.Repository;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.team.domain.SubscribeNovel;


@Repository
public interface SubNvRepository extends JpaRepository<SubscribeNovel, Integer>{
	
	SubscribeNovel findFirstByMemIdAndNvcId(int memid, int nvcid);
	
	ArrayList<SubscribeNovel> findByNvcId(int nvcid);
	
	ArrayList<SubscribeNovel> findByMemIdIn(List<Integer> memids);
	
	ArrayList<SubscribeNovel> findTokenByNvcId(int nvcid);
	
	ArrayList<SubscribeNovel> findNvcIdByMemId(int memid);
	
	SubscribeNovel findByMemIdAndNvcId(int memid, int nvcid);
	
}
