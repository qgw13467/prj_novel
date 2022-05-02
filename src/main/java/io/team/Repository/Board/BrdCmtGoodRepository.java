package io.team.Repository.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.team.domain.BrdCmtGood;


@Repository
public interface BrdCmtGoodRepository extends JpaRepository<BrdCmtGood, Integer> {
	BrdCmtGood findByBrdCmtIdAndMemId(int brdCmtId, int MemId);
}
