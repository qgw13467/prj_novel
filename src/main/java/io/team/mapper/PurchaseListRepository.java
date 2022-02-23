package io.team.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.team.domain.PurchaseList;

@Repository
public interface PurchaseListRepository extends JpaRepository<PurchaseList, Integer>{
	PurchaseList findByMemidAndNvid(int memid, int nvid);
	boolean existsByMemidAndNvid(int memid, int nvid);
}
