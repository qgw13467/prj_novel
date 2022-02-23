package io.team.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.team.domain.Enum.PointPurpose;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="point")
public class Point {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pnt_id")
	private int rvid;
	
	@Column(name="mem_id")
	private int memid;
	
	@Column(name="pnt_usehistory")
	@Enumerated(EnumType.STRING)
	private PointPurpose pointPurpose;
	
	@Column(name="pnt_spend")
	private int pnt_spend;
	
	@Column(name="pnt_datetime")
	@CreationTimestamp
	private LocalDateTime pnt_datetime = LocalDateTime.now();
	
	
	public Point(int mem_id, PointPurpose pointPurpose, int pnt_spend) {
		this.memid = mem_id;
		this.pointPurpose = pointPurpose;
		this.pnt_spend = pnt_spend;
	}

}
