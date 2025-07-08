package com.portal.bid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private static final String EMAIL_API_URL = "https://gsbmail.pythonanywhere.com/send_email";

    @Autowired
    private RestTemplate restTemplate;

    public void sendEmail(String toEmail, String subject, String body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> emailRequest = new HashMap<>();
        emailRequest.put("to_email", toEmail);
        emailRequest.put("subject", subject);
        emailRequest.put("body", body);

        HttpEntity<Map<String, String>> request = new HttpEntity<>(emailRequest, headers);

        restTemplate.postForObject(EMAIL_API_URL, request, String.class);
    }
}