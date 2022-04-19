package io.team.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.team.domain.NovelCover;
import io.team.domain.Review;

@Repository
public interface ReviewMapper extends JpaRepository<Review, Integer>{

	Review findByNvidAndMemid(int nvid, int memid);

}
