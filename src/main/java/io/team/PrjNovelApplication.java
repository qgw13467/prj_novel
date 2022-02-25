package io.team;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@MapperScan(basePackages = "io.team.mapper")
@EnableWebMvc
public class PrjNovelApplication {


	public static void main(String[] args) {
		SpringApplication.run(PrjNovelApplication.class, args);
	}

}
