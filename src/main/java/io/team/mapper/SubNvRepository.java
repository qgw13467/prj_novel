package io.team.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.team.domain.SubscribeNovel;


@Repository
public interface SubNvRepository extends JpaRepository<SubscribeNovel, Integer>{

}
