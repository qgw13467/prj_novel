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
	private String img_url;
	private String nvc_title;
	private String nvc_contents;
	private int nvc_hit;
	private int nvc_reviewpoint;
	private int nvc_reviewcount;

	
	public NovelCover (HashMap<String, String> map) {
		this.img_url=map.get("img_url");
		this.nvc_title=map.get("nvc_title");
		this.nvc_contents=map.get("nvc_content");
	}
}
