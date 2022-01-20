package io.team;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan(basePackages = "io.team.mapper")
public class PrjNovelApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrjNovelApplication.class, args);
	}

}
