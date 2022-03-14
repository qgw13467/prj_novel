package io.team.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "subscribe_novel")
@Builder
public class SubscribeNovel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subnv_id")
	private int subnv_id;
	
	@Column(name = "mem_id")
	private int mem_id;

	@Column(name = "nvc_id")
	private int nvc_id;

	@Column(name = "subnv_datetime")
	@CreationTimestamp
	private LocalDateTime subnvDatetime = LocalDateTime.now();

}