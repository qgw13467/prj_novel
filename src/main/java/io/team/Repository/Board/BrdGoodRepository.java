package io.team.Repository.Board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.team.domain.BrdGood;

@Repository
public interface BrdGoodRepository extends JpaRepository<BrdGood, Integer>{
	BrdGood findByBrdIdAndMemId(int brdId, int memId);
}
