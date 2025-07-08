package com.portal.bid.config;



import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class UnAuthorizedUserAuthenticationEntryPoint implements AuthenticationEntryPoint {

//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//                         AuthenticationException authException) throws IOException, ServletException {
//
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"UnAuthorized User");
//    }
    private static final Logger logger = LoggerFactory.getLogger(UnAuthorizedUserAuthenticationEntryPoint.class);


    @Override
    public void commence(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, AuthenticationException authException) throws IOException, jakarta.servlet.ServletException {

        // response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"UnAuthorized User");
        logger.error("Unauthorized access attempt", authException);
        logger.error("Request URI: " + request.getRequestURI());
        logger.error("Request Method: " + request.getMethod());
        
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().println(
            "{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}"
        );
    }
}