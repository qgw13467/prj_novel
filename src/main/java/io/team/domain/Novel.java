package io.team.domain;

import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Novel {
	
	private int nv_id;
	private int mem_id;
	private String img_url;
	private String nv_writer;
	private String nv_title;
	private String nv_contents;
	private int nv_hit;
	private int nv_comment_count;
	private int nv_reviewpoint;
	private int nv_reviewcount;
	private int nv_state;
	private String nv_datetime;
	private String nv_updatetime;
	private int nv_point;
	
	public Novel(int nv_id){
		this.nv_id=nv_id;
	}
	

	public Novel(HashMap<String, String> map){
		this.mem_id= Integer.parseInt(map.get("mem_id"));
		this.img_url= map.get("img_url");
		this.nv_writer= (String)map.get("nv_writer");
		this.nv_title= (String)map.get("nv_title");
		this.nv_contents= (String)map.get("nv_contents");
		this.nv_state= Integer.parseInt(map.get("nv_state"));
		this.nv_point = Integer.parseInt(map.get("nv_point"));
	}



}
