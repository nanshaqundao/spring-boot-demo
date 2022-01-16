package me.nansha.controller;

import com.alibaba.druid.util.StringUtils;
import me.nansha.Result;
import me.nansha.ResultInfo;
import me.nansha.api.TestService;
import me.nansha.handler.exception.MyRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/test")
    public Result test() {

        String test = testService.test();
        throw new MyRuntimeException("111","customised error");
//        if (!StringUtils.isEmpty(test)) {
//            return Result.success().codeAndMessage(ResultInfo.SUCCESS).data("data", test);
//        }
//        return Result.error().codeAndMessage(ResultInfo.NOT_FOUND);
    }
}
