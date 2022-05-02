package io.team.Repository.Novel;

import io.team.domain.NovelCover;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NvCoverMapper extends JpaRepository<NovelCover, Integer> {
	
	NovelCover findByNvcId(int nvc_id);
	
	NovelCover findFirstByNvId(int nv_id);
	
	List<NovelCover> findAllByNvcIdIn(List<Integer> nvc_id);
	
	Page<NovelCover> findAllByNvcIdIn(List<Integer> nvc_id, Pageable pageable);
	
	Page<NovelCover> findByNvcTitleContaining(String keyword, Pageable pageable);
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