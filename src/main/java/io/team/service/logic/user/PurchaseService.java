package io.team.service.logic.user;

import java.util.ArrayList;
import java.util.HashSet;

import org.springframework.stereotype.Service;

import io.team.Repository.PurchaseListRepository;
import io.team.domain.PurchaseList;
import io.team.service.logic.novel.NvCoverServiceLogic;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PurchaseService {
	
	private final PurchaseListRepository purchaseListRepository;
	
	private final UserServicLogic userServicLogic;
	
	private final NvCoverServiceLogic nvCoverServiceLogic;
	
	public ArrayList<PurchaseList> getPurchaseList(int mem_id) {
		
		ArrayList<PurchaseList> purchaseLists = new ArrayList<>();
		
		purchaseLists = purchaseListRepository.findByMemId(mem_id);
		
		
		return purchaseLists;
	}
	
	public HashSet<PurchaseList> getPurchaseListAsSet(int mem_id) {
		
		ArrayList<PurchaseList> purchaseLists = new ArrayList<>();
		HashSet<PurchaseList> purchaseListAsSet = new HashSet<>();
		purchaseLists = purchaseListRepository.findByMemId(mem_id);
		for (PurchaseList purchaseList : purchaseListAsSet) {
			purchaseListAsSet.add(purchaseList);
		}
		
		return purchaseListAsSet;
	}
	
}
