package io.team.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.team.domain.BrdGood;


public interface BrdGoodRepository extends JpaRepository<BrdGood, Integer>{
	BrdGood findByBrdIdAndMemId(int brdId, int memId);
}
