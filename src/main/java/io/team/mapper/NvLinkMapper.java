package io.team.mapper;

import java.util.ArrayList;
import io.team.domain.Novel;
import org.springframework.stereotype.Repository;


@Repository
public interface NvLinkMapper {
	
	int create(int nvl_parents, int nvl_childnode, int mem_id);

	ArrayList<Novel> findByParetID(int nvl_parents);
	

}
