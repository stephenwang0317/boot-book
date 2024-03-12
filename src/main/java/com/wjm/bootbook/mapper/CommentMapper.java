package com.wjm.bootbook.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wjm.bootbook.entity.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author stephen wang
 */
@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}
