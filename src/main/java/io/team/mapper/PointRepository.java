package io.team.mapper;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.team.domain.Point;

@Repository
public interface PointRepository extends JpaRepository<Point, Integer>{

}
