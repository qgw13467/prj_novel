package io.team.domain.dto;


import java.util.ArrayList;

import io.team.domain.Novel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NovelDTO {
	private int nvId;
	private int memId;
	private ArrayList<String> imgUrls;
	private String nvWriter;
	private String nvTitle;
	private String nvContents;
	private int nvHit;
	private int nvCommentCount;
	private int nvReviewpoint;
	private int nvReviewcount;
	private int nvState;
	private String nvDatetime;
	private String nvUpdatetime;
	private int nvPoint;
	
	public static NovelDTO novelDTOfromNovel(Novel novel) {
		return NovelDTO.builder().
				nvId(novel.getNvId())
				.memId(novel.getMemId())
				.nvWriter(novel.getNvWriter())
				.nvTitle(novel.getNvTitle())
				.nvContents(novel.getNvContents())
				.nvHit(novel.getNvHit())
				.nvCommentCount(novel.getNvCommentCount())
				.nvReviewpoint(novel.getNvReviewpoint())
				.nvReviewcount(novel.getNvCommentCount())
				.nvDatetime(novel.getNvDatetime())
				.nvUpdatetime(novel.getNvUpdatetime())
				.nvPoint(novel.getNvPoint())
				.build();
				
	}
}
