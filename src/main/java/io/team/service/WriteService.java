package io.team.service;

import java.util.ArrayList;


public interface WriteService<T> {
	int register(T obj, String token);

	T find(int id);

	int modify(int id, T obj, String token);

	int remove(int id, String token);
	
	ArrayList<T> getList(int pagenum, int rownum);
	
	int report(int id, String token);
	
	int getPageNum();
}
