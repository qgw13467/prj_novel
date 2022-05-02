package io.team.service.logic.board;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.team.Repository.Board.BrdCmtGoodRepository;
import io.team.domain.BrdCmtGood;
import io.team.mapper.BrdCmtMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrdCmtGoodService {
	private final BrdCmtGoodRepository brdCmtGoodRepository;

	private final BrdCmtMapper brdCmtMapper;

	@Transactional
	public int assessBrdCmtGood(int brd_cmt_id, int mem_id) {

		BrdCmtGood brdCmtGood = brdCmtGoodRepository.findByBrdCmtIdAndMemId(brd_cmt_id, mem_id);
		if (brdCmtGood == null) {
			brdCmtGood = BrdCmtGood.builder()
					.brdCmtId(brd_cmt_id)
					.memId(mem_id)
					.state(1)
					.build();
			brdCmtGoodRepository.save(brdCmtGood);
			brdCmtMapper.like(brd_cmt_id);
		} else if (brdCmtGood.getState() == -1) {
			brdCmtGood.setState(1);
			brdCmtGood.setDatetime(LocalDateTime.now());
			brdCmtGoodRepository.save(brdCmtGood);
			brdCmtMapper.like(brd_cmt_id);
			brdCmtMapper.dislikeMinus(brd_cmt_id);
		} else if (brdCmtGood.getState() == 1) {
			brdCmtGood.setState(0);
			brdCmtGood.setDatetime(LocalDateTime.now());
			brdCmtGoodRepository.save(brdCmtGood);
			brdCmtMapper.likeMinus(brd_cmt_id);
		} else if (brdCmtGood.getState() == 0) {
			brdCmtGood.setState(1);
			brdCmtGood.setDatetime(LocalDateTime.now());
			brdCmtGoodRepository.save(brdCmtGood);
			brdCmtMapper.like(brd_cmt_id);
		}

		return brdCmtGood.getState();
	}

	@Transactional
	public int assessBrdCmtBad(int brd_cmt_id, int mem_id) {

		BrdCmtGood brdCmtGood = brdCmtGoodRepository.findByBrdCmtIdAndMemId(brd_cmt_id, mem_id);
		if (brdCmtGood == null) {
			brdCmtGood = BrdCmtGood.builder()
					.brdCmtId(brd_cmt_id)
					.memId(mem_id)
					.state(-1)
					.build();

			brdCmtGoodRepository.save(brdCmtGood);
			brdCmtMapper.dislike(brd_cmt_id);
		} else if (brdCmtGood.getState() == 1) {
			brdCmtGood.setState(-1);
			brdCmtGood.setDatetime(LocalDateTime.now());
			brdCmtGoodRepository.save(brdCmtGood);
			brdCmtMapper.dislike(brd_cmt_id);
			brdCmtMapper.likeMinus(brd_cmt_id);
		} else if (brdCmtGood.getState() == -1) {
			brdCmtGood.setState(0);
			brdCmtGood.setDatetime(LocalDateTime.now());
			brdCmtGoodRepository.save(brdCmtGood);
			brdCmtMapper.dislikeMinus(brd_cmt_id);
		} else if (brdCmtGood.getState() == 0) {
			brdCmtGood.setState(-1);
			brdCmtGood.setDatetime(LocalDateTime.now());
			brdCmtGoodRepository.save(brdCmtGood);
			brdCmtMapper.dislike(brd_cmt_id);
		}

		return brdCmtGood.getState();
	}
}
