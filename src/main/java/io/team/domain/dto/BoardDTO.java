package io.team.domain.dto;

import java.util.ArrayList;
import io.team.domain.Board;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDTO {
	private int brdId;
	private int memId;
	private ArrayList<String> imgUrls;
	private String memNickname;
	private String brdTitle;
	private String brdContents;
	private int brdState;
	private String brdDatetime;
	private String brdUpdatetime;
	private int brdHit;
	private int brdCommentCount;
	private int brdLike;
	private int brdDislike;
	private int brdNotice;
	private int brdImg;
	private int brdFile;



	public static BoardDTO boardDTOfromBoard(Board board) {
		return BoardDTO.builder()
				.brdId(board.getBrdId())
				.memId(board.getMemId())
				.memNickname(board.getMemNickname())
				.brdTitle(board.getBrdTitle())
				.brdContents(board.getBrdContents())
				.brdDatetime(board.getBrdDatetime())
				.brdUpdatetime(board.getBrdUpdatetime())
				.brdHit(board.getBrdHit())
				.brdCommentCount(board.getBrdCommentCount())
				.brdLike(board.getBrdLike())
				.brdDislike(board.getBrdDislike())
				.brdImg(board.getBrdImg())
				.build();

	}
}
