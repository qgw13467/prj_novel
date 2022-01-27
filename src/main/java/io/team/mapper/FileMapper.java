package io.team.mapper;

import org.springframework.stereotype.Repository;


@Repository
public interface FileMapper {
	int uploadFile(int mem_id, String img_originname, String img_filename, String img_url);
	
	int findByFilename(String img_filename);
	
	String findUrlById(int img_id);
}
