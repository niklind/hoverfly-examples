package com.squeed.hoverfly.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TimeController {

    @GetMapping("/time")
    public ResponseEntity<String> time() {

        try{
            return new RestTemplate().getForEntity("http://time.jsontest.com/", String.class);
        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
        }
    }

}
