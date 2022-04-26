package io.team.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import io.team.domain.Enum.ReportState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "novel_report")
public class NovelReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="mem_id")
	private int memId;
	
	@Column(name="nv_id")
	private int nvId;
	
	@Column(name="report_type")
	@Enumerated(EnumType.STRING)
	private ReportState reportState;
	
	@Column(name="content")
	private String content;
	
	@Column(name="datetime")
	@CreationTimestamp
	private LocalDateTime Datetime = LocalDateTime.now();
}
