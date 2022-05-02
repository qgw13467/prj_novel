package io.team.Repository.Novel;

import org.springframework.data.jpa.repository.JpaRepository;
import io.team.domain.NovelCmtReport;

public interface NvCmtReportRepository extends JpaRepository<NovelCmtReport, Integer>{
	NovelCmtReport findByNvCmtIdAndMemId(int nvCmtId, int memId);
}
