package com.squeed.hoverfly.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class TimeController {

    @GetMapping("time")
    public String time() {
        return new RestTemplate().getForObject("http://time.jsontest.com/", String.class);
    }

}
