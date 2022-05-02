package io.team.Repository.Novel;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.team.domain.NovelReport;

@Repository
public interface NvReportRepository extends JpaRepository<NovelReport, Integer>{
	NovelReport findFirstByNvIdAndMemId(int NvId, int MemId);
}
