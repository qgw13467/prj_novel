package io.team.Repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.google.common.collect.ArrayListMultimap;

import io.team.domain.PurchaseList;

@Repository
public interface PurchaseListRepository extends JpaRepository<PurchaseList, Integer>{
	
	PurchaseList findByMemIdAndNvId(int memid, int nvid);
	
	ArrayList<PurchaseList> findByMemId(int memid);
	
	boolean existsByMemIdAndNvId(int memid, int nvid);
}
