package com.wjm.bootbook.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wjm.bootbook.entity.pojo.Comment;
import com.wjm.bootbook.mapper.CommentMapper;
import com.wjm.bootbook.service.CommentService;
import org.springframework.stereotype.Service;

/**
 * @author stephen wang
 */
@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
}
