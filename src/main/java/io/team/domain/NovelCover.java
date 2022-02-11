package io.team.domain;

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
@Entity
public class NovelCover {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int nvc_id;
	
	private int nv_id;
	private int img_id;
	private String nvc_title;
	private int nvc_hit;
	private int nvc_reviewpoint;
}
