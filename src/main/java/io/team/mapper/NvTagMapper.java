package io.team.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

@Repository
public interface NvTagMapper {
	
	int create(long tag_hash, int nvc_id);
	
	ArrayList<Integer> findByTagHash(long tag_hash);
	
	ArrayList<Integer> findByNvcid(int nvc_id);
}
