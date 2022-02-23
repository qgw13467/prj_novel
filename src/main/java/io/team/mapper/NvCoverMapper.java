package io.team.mapper;

import io.team.domain.NovelCover;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NvCoverMapper extends JpaRepository<NovelCover, Integer> {
	
	NovelCover findByNvcid(int nvc_id);
	
	List<NovelCover> findByNvid(int nv_id);
	
	List<NovelCover> findAllByNvcidIn(List<Integer> nvc_id);
	
	Page<NovelCover> findAllByNvcidIn(List<Integer> nvc_id, Pageable pageable);
	
	
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
	 */
	
}
