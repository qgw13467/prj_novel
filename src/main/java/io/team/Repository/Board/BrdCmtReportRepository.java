package io.team.Repository.Board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.team.domain.BrdCmtReport;

@Repository
public interface BrdCmtReportRepository extends JpaRepository<BrdCmtReport, Integer>{
	BrdCmtReport findByBrdCmtIdAndMemId(int brdCmtId, int memId);
}
