package io.team.domain;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Novel {

	private int nvId;
	private int memId;
	private String imgUrl;
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
	
	
	public Novel(int nvId){
		this.nvId=nvId;
	}
	

	public Novel(HashMap<String, String> map){
		this.memId= Integer.parseInt(map.get("memId"));
		this.imgUrl= map.get("imgUrl");
		this.nvWriter= (String)map.get("nvWriter");
		this.nvTitle= (String)map.get("nvTitle");
		this.nvContents= (String)map.get("nvContents");
		this.nvState= Integer.parseInt(map.get("nvState"));
		this.nvPoint = Integer.parseInt(map.get("nvPoint"));
	}



}
