package me.nansha.api;

import me.nansha.entity.User;

public interface TestService {
    String test();

    User getUserById(Integer id);
}
