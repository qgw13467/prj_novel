package io.team.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.team.domain.BrdReport;

@Repository
public interface BrdReportRepository extends JpaRepository<BrdReport, Integer>{
	BrdReport findFirstByBrdIdAndMemId(int BrdId, int MemId);
}
