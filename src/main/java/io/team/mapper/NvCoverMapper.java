package io.team.mapper;

import io.team.domain.NovelCover;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NvCoverMapper extends JpaRepository<NovelCover, Integer> {
	
	public List<NovelCover> findById(int nvc_id);

	
	
	
	
	
	/*
	 * int create(NovelCover novelCover);
	 * 
	 * NovelCover findByTitle(String nvc_title);
	 * 
	 * NovelCover findByNvid(int nv_id);
	 * 
	 * int update(int nv_id, int img_id, String nvc_title);
	 * 
	 * int count_hit(int nv_id)
	 */;
	
}
