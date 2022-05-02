package io.team.Repository.Board;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import io.team.domain.ImgIds;


@Repository
public interface ImgIdsRepository extends JpaRepository<ImgIds, Integer>{
	ArrayList<ImgIds> findByEventId(int eventId);
}
