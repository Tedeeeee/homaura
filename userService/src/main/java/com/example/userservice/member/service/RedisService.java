package com.example.userservice.member.service;

import java.time.Duration;

public interface RedisService {

    void setValues(String key, String value, Duration duration);

    String getValue(String key);

}
