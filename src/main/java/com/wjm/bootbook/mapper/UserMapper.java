package com.wjm.bootbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjm.bootbook.entity.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author stephen wang
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
