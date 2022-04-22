package io.team.domain;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.hibernate.annotations.CreationTimestamp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name="purchase_list")
public class PurchaseList {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="pl_id")
	private int plId;
	
	@Column(name="mem_id")
	private int memId;
	
	@Column(name="nv_id")
	private int nvId;

	@Column(name="pl_datetime")
	@CreationTimestamp
	private LocalDateTime plDatetime = LocalDateTime.now();
	
	
	public PurchaseList(int mem_id, int nv_id) {
		this.memId = mem_id;
		this.nvId = nv_id;
	}
}
