package io.team.mapper;


import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.team.domain.SubscribeNovel;


@Repository
public interface SubNvRepository extends JpaRepository<SubscribeNovel, Integer>{
	
	SubscribeNovel findFirstByMemidAndNvcid(int memid, int nvcid);
	
	ArrayList<Integer> findMemidByNvcid(int nvcid);
	
	ArrayList<SubscribeNovel> findTokenByNvcid(int nvcid);
	
}
