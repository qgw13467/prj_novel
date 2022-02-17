package io.team.mapper;

import org.springframework.data.jpa.repository.JpaRepository;

import io.team.domain.NovelCover;

public interface ReviewMapper extends JpaRepository<NovelCover, Integer>{

}
