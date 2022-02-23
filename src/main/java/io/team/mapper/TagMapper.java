package io.team.mapper;

import org.springframework.stereotype.Repository;

@Repository
public interface TagMapper {
	
	int create(long tag_hash, String tag_name);
	
	String findByTagHash(long tag_hash);
	
	String findByTageName(String tag_name);
	
}
