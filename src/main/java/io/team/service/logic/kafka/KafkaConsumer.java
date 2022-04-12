package io.team.service.logic.kafka;

import java.io.IOException;
import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.team.service.logic.SubscribeNvService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KafkaConsumer {
	
	private final SubscribeNvService subscribeNvService;
	
    @KafkaListener(topics = "FcmMsg", groupId = "FcmMsg")
    public void consume(String message) throws IOException {
    	ObjectMapper mapper = new ObjectMapper();
    	Map<String, String> map = mapper.readValue(message, Map.class);
    	
    	subscribeNvService.pushSubscribeNv(Integer.parseInt(map.get("titleId")), map.get("title"), map.get("contents"));
        System.out.println(map);
    
     
    }
    



}
