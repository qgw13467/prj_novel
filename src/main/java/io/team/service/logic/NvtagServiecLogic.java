package io.team.service.logic;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import io.team.mapper.NvTagMapper;
import io.team.mapper.TagMapper;

@Service
public class NvtagServiecLogic {
	
	@Autowired
	NvTagMapper nvTagMapper;
	
	@Autowired
	TagMapper tagMapper;
	
	public int register(int nvc_id, String tagName) {
		int result=0;
		result += nvTagMapper.create(tagName.hashCode(), nvc_id);
		String temptag = tagMapper.findByTagHash(tagName.hashCode());
		if(temptag==null) {
			result += tagMapper.create(tagName.hashCode(), tagName);
		}
		
		return 0;
	}
	
	public ArrayList<Integer> findByTagName(String tagName) {
		
		String tagHashString = tagMapper.findByTageName(tagName);
		ArrayList<Integer> result = new ArrayList<Integer>();
		
		if(tagHashString == null) {
			return result;
		}
		int tagHash = Integer.parseInt(tagHashString);
		result = nvTagMapper.findByTagHash(tagHash);
		return result;
	}
	
	public ArrayList<String> findByNvcId(int nvc_id) {
		
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<Integer> tagHashList = new ArrayList<Integer>();
		ArrayList<String> tagNameList = new ArrayList<String>();
		
		tagHashList= nvTagMapper.findByNvcid(nvc_id);
		
		for (int hash : tagHashList) {
			String temp =tagMapper.findByTagHash(hash);
			tagNameList.add(temp);
		}
		
		return tagNameList;
	}
	
}
