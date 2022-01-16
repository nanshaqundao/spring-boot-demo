package me.nansha.mapper;

import me.nansha.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;


@Mapper
public interface TestMapper {
    User getUserById(@Param("id")Integer id);
}
