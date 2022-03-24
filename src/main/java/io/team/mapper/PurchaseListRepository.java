package io.team.mapper;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ArrayListMultimap;

import io.team.domain.PurchaseList;

@Repository
public interface PurchaseListRepository extends JpaRepository<PurchaseList, Integer>{
	
	PurchaseList findByMemidAndNvid(int memid, int nvid);
	
	ArrayList<PurchaseList> findByMemid(int memid);
	
	boolean existsByMemidAndNvid(int memid, int nvid);
}
