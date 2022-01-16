package me.nansha.controller;

import me.nansha.api.TestService;
import me.nansha.entity.User;
import me.nansha.helper.Result;
import me.nansha.helper.ResultInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public Result test() {
        User userById = testService.getUserById(1);
        return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", userById);
    }

}
