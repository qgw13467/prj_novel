package io.team.service.logic.user;

import java.util.ArrayList;

import org.springframework.stereotype.Service;

import io.team.domain.PurchaseList;
import io.team.mapper.PurchaseListRepository;
import io.team.service.logic.UserServicLogic;
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
		
		purchaseLists = purchaseListRepository.findByMemid(mem_id);
		
		
		return purchaseLists;
	}
	
}