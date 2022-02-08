package io.team.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import io.team.domain.Novel;

@Repository
public interface NovelMapper {
	int create(int mem_id, int img_id, String nv_writer, String nv_title, String nv_contents, int nv_state);

	Novel read(int nv_id);

	int update(int nv_id, int img_id, String nv_writer, String nv_title, String nv_contents, int nv_state);

	int delete(int nv_id);

	int count_hit(int nv_id);

	ArrayList<Novel> getNovels(int pagenum, int pagecount);

	int nvcount();
//////////////////////
	int review(int nv_id, int point);

	int getNextNovel();

	int report(int nv_id, int mem_id);

}
