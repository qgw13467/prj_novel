package io.team.handler;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;


@Component
public class OAuth2AuthenticationFailureHanlder implements AuthenticationFailureHandler {
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
	

		String msg = "ERROR";
		writeMsgResponse(response, msg);

	}
	
	 private void writeMsgResponse(HttpServletResponse response, String msg) 
	            throws IOException {
	    response.setContentType("text/html;charset=UTF-8");

	    response.addHeader("Authorization", msg);
	    response.setContentType("application/json;charset=UTF-8");
	 }
}