package io.team.mapper;

import java.util.ArrayList;

import org.springframework.stereotype.Repository;

import io.team.domain.User;

@Repository
public interface UserMapper {

	String create(User newUser);

	void update(User newUser);

	void delete(int id);

	User retrive(int id);

	ArrayList<User> selectAll();
}
