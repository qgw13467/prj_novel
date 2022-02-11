package io.team.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

@Repository
public interface TagMapper {
	
	int create(int tag_id, String tag_name);
	
	ArrayList<String> findByTagid(int tag_id);
	
	ArrayList<Integer> findBtTageName(String tag_name);
	
}
