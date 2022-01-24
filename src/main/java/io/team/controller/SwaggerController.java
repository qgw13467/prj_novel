package io.team.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class SwaggerController {

	@GetMapping("/hello")
    public String Hello(){
        return "hello";
    }
}
