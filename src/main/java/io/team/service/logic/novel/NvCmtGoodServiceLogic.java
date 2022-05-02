package io.team.service.logic.novel;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import io.team.Repository.Novel.NvCmtGoodRepository;
import io.team.domain.NovelCmtGood;
import io.team.mapper.NvCmtMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NvCmtGoodServiceLogic {
	private final NvCmtGoodRepository nvCmtGoodRepository;

	private final NvCmtMapper nvCmtMapper;

	@Transactional
	public int assessNvCmtGood(int nv_cmt_id, int mem_id) {

		NovelCmtGood nvCmtGood = nvCmtGoodRepository.findByNvCmtIdAndMemId(nv_cmt_id, mem_id);
		if (nvCmtGood == null) {
			nvCmtGood = NovelCmtGood.builder()
					.nvCmtId(nv_cmt_id)
					.memId(mem_id)
					.state(1)
					.build();
			nvCmtGoodRepository.save(nvCmtGood);
			nvCmtMapper.like(nv_cmt_id);
		} else if (nvCmtGood.getState() == -1) {
			nvCmtGood.setState(1);
			nvCmtGood.setDatetime(LocalDateTime.now());
			nvCmtGoodRepository.save(nvCmtGood);
			nvCmtMapper.like(nv_cmt_id);
			nvCmtMapper.dislikeMinus(nv_cmt_id);
		} else if (nvCmtGood.getState() == 1) {
			nvCmtGood.setState(0);
			nvCmtGood.setDatetime(LocalDateTime.now());
			nvCmtGoodRepository.save(nvCmtGood);
			nvCmtMapper.likeMinus(nv_cmt_id);
		} else if (nvCmtGood.getState() == 0) {
			nvCmtGood.setState(1);
			nvCmtGood.setDatetime(LocalDateTime.now());
			nvCmtGoodRepository.save(nvCmtGood);
			nvCmtMapper.like(nv_cmt_id);
		}

		return nvCmtGood.getState();
	}

	@Transactional
	public int assessNvCmtBad(int nv_cmt_id, int mem_id) {

		NovelCmtGood nvCmtGood = nvCmtGoodRepository.findByNvCmtIdAndMemId(nv_cmt_id, mem_id);
		if (nvCmtGood == null) {
			nvCmtGood = NovelCmtGood.builder()
					.nvCmtId(nv_cmt_id)
					.memId(mem_id)
					.state(-1)
					.build();

			nvCmtGoodRepository.save(nvCmtGood);
			nvCmtMapper.dislike(nv_cmt_id);
		} else if (nvCmtGood.getState() == 1) {
			nvCmtGood.setState(-1);
			nvCmtGood.setDatetime(LocalDateTime.now());
			nvCmtGoodRepository.save(nvCmtGood);
			nvCmtMapper.dislike(nv_cmt_id);
			nvCmtMapper.likeMinus(nv_cmt_id);
		} else if (nvCmtGood.getState() == -1) {
			nvCmtGood.setState(0);
			nvCmtGood.setDatetime(LocalDateTime.now());
			nvCmtGoodRepository.save(nvCmtGood);
			nvCmtMapper.dislikeMinus(nv_cmt_id);
		} else if (nvCmtGood.getState() == 0) {
			nvCmtGood.setState(-1);
			nvCmtGood.setDatetime(LocalDateTime.now());
			nvCmtGoodRepository.save(nvCmtGood);
			nvCmtMapper.dislike(nv_cmt_id);
		}

		return nvCmtGood.getState();
	}
}
