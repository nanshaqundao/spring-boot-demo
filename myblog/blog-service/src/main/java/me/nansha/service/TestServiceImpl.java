package me.nansha.service;

import me.nansha.api.TestService;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {
    @Override
    public String test() {
        return "hello world";
    }
}
