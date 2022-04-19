package io.team.Repository;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.team.domain.SubscribeNovel;


@Repository
public interface SubNvRepository extends JpaRepository<SubscribeNovel, Integer>{
	
	SubscribeNovel findFirstByMemidAndNvcid(int memid, int nvcid);
	
	ArrayList<SubscribeNovel> findByNvcid(int nvcid);
	
	ArrayList<SubscribeNovel> findByMemidIn(List<Integer> memids);
	
	ArrayList<SubscribeNovel> findTokenByNvcid(int nvcid);
	
	ArrayList<SubscribeNovel> findNvcidByMemid(int memid);
	
	SubscribeNovel findByMemidAndNvcid(int memid, int nvcid);
	
}
