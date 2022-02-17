package io.team.domain;

import javax.persistence.Column;
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
@Entity(name="novel_link")
public class NovelLink {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int nvl_id;
	
	@Column(name="nvl_parents")
	private int nvlparents;
	private int nvl_childnode;
	@Column(name="nv_id")
	private int nvid;
}
