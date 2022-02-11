package io.team.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

@Repository
public interface NvTagMapper {
	
	int create(int tag_id, int nv_id);
	
	ArrayList<Integer> findByTagid(int tag_id);
	
	ArrayList<Integer> findByNvid(int nv_id);
}
