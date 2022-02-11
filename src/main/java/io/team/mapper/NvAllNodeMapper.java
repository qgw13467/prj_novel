package io.team.mapper;

import java.util.ArrayList;
import org.springframework.stereotype.Repository;


@Repository
public interface NvAllNodeMapper {
	
	int create(int nv_id, int nvall_descendantnode, int mem_id);

	ArrayList<Integer> findByNvID(int nv_id);
	
	ArrayList<Integer> findByUerID(int mem_id);
	
	int findByDescendantNode(int nvall_descendantnode);
	
}
