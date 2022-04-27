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

	public ArrayList<PurchaseList> getPurchaseList(int mem_id) {

		ArrayList<PurchaseList> purchaseLists = new ArrayList<>();

		purchaseLists = purchaseListRepository.findByMemId(mem_id);

		return purchaseLists;
	}

	public boolean isPurchased(int mem_id, int nv_id) {
		ArrayList<PurchaseList> purchaseLists = getPurchaseList(mem_id);
		for (PurchaseList purchaseList : purchaseLists) {
			if (purchaseList.getMemId() == mem_id && purchaseList.getNvId() == nv_id) {
				return true;
			}
		}
		return false;

	}

}
