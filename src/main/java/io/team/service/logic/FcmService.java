package io.team.service.logic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;

@Service
public class FcmService {

	@Value("${firebase.path}")
	private String jsonUbuntuPath;

	@Value("${firebase.path2}")
	private String jsonWinPath;
	

	public void send_FCMtoken(String token, String title, String content) {

		String jsonPath;
		String os = System.getProperty("os.name").toLowerCase();

		if (os.contains("win")) {
			jsonPath = jsonWinPath;
		} else{
			jsonPath = jsonUbuntuPath;
		}


		try {

			ClassPathResource resource = new ClassPathResource(jsonPath);

			FileInputStream serviceAccount = new FileInputStream(resource.getPath());

			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

			if (FirebaseApp.getApps().isEmpty()) {
				FirebaseApp.initializeApp(options);
			}

			Message msg = Message.builder().setAndroidConfig(AndroidConfig.builder().setTtl(3600 * 1000) // 1 hour in

					.setPriority(AndroidConfig.Priority.NORMAL).putData("title", title).putData("body", content)
//					.setNotification(AndroidNotification.builder().setTitle(title).setBody(content)
//							.setIcon("stock_ticker_update").setColor("#f45342").build())
					.build()).setToken(token).build();

			String responseString = FirebaseMessaging.getInstance().send(msg);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FirebaseMessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
