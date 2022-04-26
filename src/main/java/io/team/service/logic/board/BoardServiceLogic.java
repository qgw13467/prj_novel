package io.team.service.logic.board;

import java.util.ArrayList;
import java.util.Optional;
import org.springframework.stereotype.Service;
import io.team.Repository.BrdReportRepository;
import io.team.domain.Board;
import io.team.domain.BrdReport;
import io.team.jwt.JwtManager;
import io.team.mapper.BoardMapper;
import io.team.service.WriteService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceLogic implements WriteService<Board> {

	private final JwtManager jwtManager;
	private final BoardMapper boardMapper;
	private final BrdReportRepository brdReportRepository;
	
	@Override
	public int register(Board newBoard, String token) {

		try {
			int result = boardMapper.create(newBoard.getMemId(), newBoard.getImgUrl(), newBoard.getMemNickname(),
					newBoard.getBrdTitle(), newBoard.getBrdContents(), newBoard.getBrdState(),
					newBoard.getBrdDatetime(), newBoard.getBrdImg(), newBoard.getBrdFile());
			return result;
		} catch (Exception e) {
			return -1;
		}
		
	}

	@Override
	public Board find(int id) {
		boardMapper.count_hit(id);
		return boardMapper.read(id);
	}

	@Override
	public int modify(int brd_id, Board newBoard, String token) {
		
		newBoard.setBrdId(brd_id);
		Board board = boardMapper.read(brd_id);
		int mem_id = jwtManager.getIdFromToken(token);
		if (board.getMemId() == mem_id) {
			int result = boardMapper.update(newBoard.getBrdId(), newBoard.getImgUrl(), newBoard.getMemNickname(),
					newBoard.getBrdTitle(), newBoard.getBrdContents(), newBoard.getBrdState(), newBoard.getBrdImg(),
					newBoard.getBrdFile());

			return result;

		} else {
			return -1;
		}
	}

	@Override
	public int remove(int id, String token) {

		int mem_id = jwtManager.getIdFromToken(token);
		Board board = boardMapper.read(id);

		if (board.getMemId() == mem_id) {

			return boardMapper.delete(id);

		} else {

			return -1;
		}

	}

	@Override
	public ArrayList<Board> getList(int pagenum, int rownum) {
		
		return boardMapper.getBoards((pagenum - 1) * rownum, rownum);
	}
	
	@Override
	public int getPageNum() {
		int pagecount = 10;
		int brdNum = boardMapper.brdcount();
		int result = brdNum / pagecount;

		if (brdNum % pagecount != 0) {
			result = brdNum / pagecount + 1;
			return result;
		} else
			return result;

	}
	
	
	public ArrayList<Board> findByTitleContain(String keyword, int pagenum, int rownum){
		return boardMapper.findByTitleContain(keyword, (pagenum - 1) * rownum, rownum);
	}
	
	public int getTitleContainNum(String keyword) {
		int pagecount = 10;
		int brdNum = boardMapper.getTitleContainCount(keyword);
		int result = brdNum / pagecount;

		if (brdNum % pagecount != 0) {
			result = brdNum / pagecount + 1;
			return result;
		} else
			return result;
	}
	
	public ArrayList<Board> findByContentsContain(String keyword, int pagenum, int rownum){
		return boardMapper.findByContentsContain(keyword, (pagenum - 1) * rownum, rownum);
	}
	
	public int getContentsContainNum(String keyword) {
		int pagecount = 10;
		int brdNum = boardMapper.getContentsContainCount(keyword);
		int result = brdNum / pagecount;

		if (brdNum % pagecount != 0) {
			result = brdNum / pagecount + 1;
			return result;
		} else
			return result;
	}


	public BrdReport report(BrdReport brdReport) {
		
		return brdReportRepository.save(brdReport);
	}
	
	public Optional<BrdReport> findReport(int brd_id, int mem_id) {
		Optional<BrdReport> optBrdReport = Optional.ofNullable( brdReportRepository.findFirstByBrdIdAndMemId(brd_id, mem_id));
		return optBrdReport;
	}

}
