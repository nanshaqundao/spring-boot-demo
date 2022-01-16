package me.nansha.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.nansha.api.TestService;
import me.nansha.entity.User;
import me.nansha.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl extends ServiceImpl<TestMapper, User> implements TestService {
    @Autowired
    private TestMapper testMapper;

    @Override
    public String test() {
        return "hello world";
    }

    @Override
    public User getUserById(Integer id) {
        return testMapper.getUserById(id);
    }
}
