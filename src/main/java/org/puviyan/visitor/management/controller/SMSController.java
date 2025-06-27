package org.puviyan.visitor.management.controller;


import org.puviyan.visitor.management.model.SMSRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("/api/sms")
@CrossOrigin(origins = "http://localhost:3000") // Allow React access
public class SMSController {

    @Value("${msg91.auth-key}")
    private String authKey;

    @Value("${msg91.sender-id}")
    private String senderId;

    @Value("${msg91.route}")
    private String route;

    @Value("${msg91.country-code}")
    private String countryCode;

    @PostMapping("/send")
    public ResponseEntity<String> sendSMS(@RequestBody SMSRequest request) {
        RestTemplate restTemplate = new RestTemplate();

        Map<String, Object> payload = new HashMap<>();
        payload.put("sender", senderId);
        payload.put("route", route);
        payload.put("country", countryCode);
        payload.put("sms", List.of(Map.of(
                "message", request.getMessage(),
                "to", List.of(request.getNumber())
        )));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("authkey", authKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        String msg91Url = "https://api.msg91.com/api/v2/sendsms";
        ResponseEntity<String> response = restTemplate.postForEntity(msg91Url, entity, String.class);

        return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
    }
}
