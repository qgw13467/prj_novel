package io.team.domain;

import java.util.HashMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class NovelCover {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="nvc_id")
	private int nvcid;
	
	@Column(name="nv_id")
	private int nvid;
	private String imgUrl;
	private String nvcTitle;
	private String nvcContents;
	private int nvcHit;
	private int nvcReviewpoint;
	private int nvcReviewcount;

	
	public NovelCover (HashMap<String, String> map) {
		this.imgUrl=map.get("img_url");
		this.nvcTitle=map.get("nvc_title");
		this.nvcContents=map.get("nvc_content");
	}
}
