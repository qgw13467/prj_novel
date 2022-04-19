package io.team.service.logic.board;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.team.Repository.BrdGoodRepository;
import io.team.domain.BrdGood;
import io.team.mapper.BoardMapper;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardGoodServiceLogic {

	private final BrdGoodRepository brdGoodRepository;

	private final BoardMapper boardMapper;

	@Transactional
	public int assessBrdGood(int brd_id, int mem_id) {

		BrdGood brdGood = brdGoodRepository.findByBrdIdAndMemId(brd_id, mem_id);
		if (brdGood == null) {
			brdGood = new BrdGood();
			brdGood.setBrdId(brd_id);
			brdGood.setMemId(mem_id);
			brdGood.setState(1);
			brdGood.setDatetime(null);
			brdGoodRepository.save(brdGood);
			boardMapper.like(brd_id);
		} else if (brdGood.getState() == -1) {
			brdGood.setState(1);
			brdGood.setDatetime(LocalDateTime.now());
			brdGoodRepository.save(brdGood);
			boardMapper.like(brd_id);
			boardMapper.dislikeMinus(brd_id);
		} else if (brdGood.getState() == 1) {
			brdGood.setState(0);
			brdGood.setDatetime(LocalDateTime.now());
			brdGoodRepository.save(brdGood);
			boardMapper.likeMinus(brd_id);
		} else if (brdGood.getState() == 0) {
			brdGood.setState(1);
			brdGood.setDatetime(LocalDateTime.now());
			brdGoodRepository.save(brdGood);
			boardMapper.like(brd_id);
		}

		return brdGood.getState();
	}

	@Transactional
	public int assessBrdBad(int brd_id, int mem_id) {

		BrdGood brdGood = brdGoodRepository.findByBrdIdAndMemId(brd_id, mem_id);
		if (brdGood == null) {
			brdGood = new BrdGood();
			brdGood.setBrdId(brd_id);
			brdGood.setMemId(mem_id);
			brdGood.setState(-1);
			brdGoodRepository.save(brdGood);
			boardMapper.dislike(brd_id);
		} else if (brdGood.getState() == 1) {
			brdGood.setState(-1);
			brdGood.setDatetime(LocalDateTime.now());
			brdGoodRepository.save(brdGood);
			boardMapper.dislike(brd_id);
			boardMapper.likeMinus(brd_id);
		} else if (brdGood.getState() == -1) {
			brdGood.setState(0);
			brdGood.setDatetime(LocalDateTime.now());
			brdGoodRepository.save(brdGood);
			boardMapper.dislikeMinus(brd_id);
		} else if (brdGood.getState() == 0) {
			brdGood.setState(-1);
			brdGood.setDatetime(LocalDateTime.now());
			brdGoodRepository.save(brdGood);
			boardMapper.dislike(brd_id);
		}

		return brdGood.getState();
	}
}
