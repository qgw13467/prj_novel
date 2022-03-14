package io.team.service.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import io.jsonwebtoken.ExpiredJwtException;
import io.team.domain.Point;
import io.team.domain.PurchaseList;
import io.team.domain.User;
import io.team.domain.Enum.PointPurpose;
import io.team.jwt.JwtManager;
import io.team.mapper.PointRepository;
import io.team.mapper.PurchaseListRepository;
import io.team.service.logic.novel.NvServiceLogic;

@Service
public class PointServiceLogic {

	@Autowired
	JwtManager jwtManager;

	@Autowired
	PointRepository pointRepository;

	@Autowired
	UserServicLogic userServicLogic;

	@Autowired
	NvServiceLogic nvServiceLogic;

	@Autowired
	PurchaseListRepository purchaseListRepository;

	// 출석시 포인트
	
	public int attend(int mem_id, PointPurpose pointPurpose, int pnt_spend, String mem_lastlogin_datetime) {

		try {
			mem_lastlogin_datetime = mem_lastlogin_datetime.substring(8, 10);
			String nowDate = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
			nowDate = nowDate.substring(8, 10);

			if (!mem_lastlogin_datetime.equals(nowDate)) {
				Point newPoint = new Point(mem_id, pointPurpose, pnt_spend);
				pointRepository.save(newPoint);
				userServicLogic.changePoint(mem_id, newPoint.getPnt_spend());
				return 1;
				
			} else {
				return -1;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return -2;
		}
	}

	// 소설 작성시 포인트
	public int writeNovel(int mem_id, PointPurpose pointPurpose, int pnt_spend, String token) {
		int checkMem_id = jwtManager.getIdFromToken(token);
		if (mem_id == checkMem_id) {
			try {
				Point newPoint = new Point(mem_id, pointPurpose, pnt_spend);
				pointRepository.save(newPoint);
				userServicLogic.changePoint(mem_id, newPoint.getPnt_spend());

				return 1;
			} catch (Exception e) {
				return -2;
			}
		} else {
			return -1;
		}
	}

	// 소설 읽을시 포인트  포인트 없을 때, 포인트 없을 때 소설 넘어감, 구매한 물건 다시 구매됨
	public int readNovel(PointPurpose pointPurpose, int pnt_spend, int nv_id, int writer_id, int checkMem_id) {
		
		try {
			

			if (!purchaseListRepository.existsByMemidAndNvid(checkMem_id, nv_id)) {
				PurchaseList newpurchaseList = new PurchaseList(checkMem_id, nv_id);
				purchaseListRepository.save(newpurchaseList);
				Point newPoint = new Point(checkMem_id, pointPurpose, pnt_spend);
				pointRepository.save(newPoint);
				userServicLogic.changePoint(checkMem_id, -newPoint.getPnt_spend());
				userServicLogic.changePoint(writer_id, newPoint.getPnt_spend());
			}

			User user = userServicLogic.findByMemid(checkMem_id);
			if (user.getMem_point() < pnt_spend) {
				return -1;
			}
			
			return 1;
		} catch (Exception e) {
			return -2;
		}
	}

	/*
	 * public int purchase(Point newPoint, String token) { int mem_id =
	 * jwtManager.getIdFromToken(token); if (newPoint.getMemid() == mem_id) { try {
	 * pointRepository.save(newPoint); userServicLogic.changePoint(mem_id,
	 * newPoint.getPnt_spend()); return 1; }catch (Exception e) { return -2; } }
	 * else { return -1; } }
	 */

}
