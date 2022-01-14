package io.team;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "io.team.mapper")
public class PrjNovelApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrjNovelApplication.class, args);
	}

}
