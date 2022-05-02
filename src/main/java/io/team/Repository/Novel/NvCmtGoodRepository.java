package io.team.Repository.Novel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import io.team.domain.NovelCmtGood;

@Repository
public interface NvCmtGoodRepository extends JpaRepository<NovelCmtGood, Integer>{
	NovelCmtGood findByNvCmtIdAndMemId(int nvCmtId, int memId);
}
