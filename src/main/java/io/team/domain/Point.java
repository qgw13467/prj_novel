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
	private int rvId;
	
	@Column(name="mem_id")
	private int memId;
	
	@Column(name="pnt_usehistory")
	@Enumerated(EnumType.STRING)
	private PointPurpose pointPurpose;
	
	@Column(name="pnt_spend")
	private int pntSpend;
	
	@Column(name="pnt_datetime")
	@CreationTimestamp
	private LocalDateTime pntDatetime = LocalDateTime.now();
	
	
	public Point(int mem_id, PointPurpose pointPurpose, int pnt_spend) {
		this.memId = mem_id;
		this.pointPurpose = pointPurpose;
		this.pntSpend = pnt_spend;
	}

}
