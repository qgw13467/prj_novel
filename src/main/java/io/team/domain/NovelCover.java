package io.team.domain;

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
	private int img_id;
	private String nvc_title;
	private int nvc_hit;
	private int nvc_reviewpoint;
	
	@ColumnDefault("0")
	private int nvc_reviewcount;
}
