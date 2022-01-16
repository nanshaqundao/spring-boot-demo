package me.nansha.controller;

import me.nansha.Result;
import me.nansha.api.TestService;
import me.nansha.entity.User;
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
        return Result.success().data("data", userById);
//
//        String test = testService.test();
//        throw new MyRuntimeException("111","customised error");
////        if (!StringUtils.isEmpty(test)) {
////            return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", test);
////        }
////        return Result.error().codeAndMessage(ResultInfo.NOT_FOUND);
    }
}
