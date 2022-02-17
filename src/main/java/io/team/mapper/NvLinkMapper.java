package io.team.mapper;

import java.util.ArrayList;
import java.util.List;

import io.team.domain.Novel;
import io.team.domain.NovelCover;
import io.team.domain.NovelLink;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NvLinkMapper extends JpaRepository<NovelLink, Integer>{
	

	ArrayList<NovelLink> findByNvlparents(int nvl_parents);
	
	ArrayList<NovelLink> findByNvid(int nv_id);

}
