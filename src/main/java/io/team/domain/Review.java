package io.team.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="review")
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="rv_id")
	private int rvId;
	
	@Column(name="nv_id")
	private int nvId;
	@Column(name="mem_id")
	private int memId;
	@Column(name="rv_point")
	private int rvPoint;
	
	@Column(name="rv_datetime")
	@CreationTimestamp
	private LocalDateTime rvDatetime = LocalDateTime.now();
	

}
