package io.team.service.logic.fcmserver;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AndroidPushNotificationsService {
	private static final String firebase_server_key = "AAAAYjWrTXo:APA91bHa5UZX8MqDR7G-htJ-wgIp29wC6FtLdlTFjZHHfrl-bzV07q2sWboxEQjQPTv3-TgC8on8Beu4tu4nvz6uSoJSb8GlRYhze7MCrkGvmv5a7hzUE3go_lkaDCaqFcxeSgNhMyiA";
	private static final String firebase_api_url = "https://fcm.googleapis.com/fcm/send";

	@Async
	public CompletableFuture<String> send(HttpEntity<String> entity) {

		RestTemplate restTemplate = new RestTemplate();

		ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

		interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + firebase_server_key));
		interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json; UTF-8 "));
		restTemplate.setInterceptors(interceptors);

		String firebaseResponse = restTemplate.postForObject(firebase_api_url, entity, String.class);

		return CompletableFuture.completedFuture(firebaseResponse);
	}
}
