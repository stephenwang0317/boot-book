package com.wjm.bootbook.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author stephen wang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_article")
public class Article {
    @TableId(type = IdType.AUTO)
    public Long artId;
    public Long artAuthor;
    public Integer artLike;
    public String artTitle;
    public String artContent;
    public Long artCreated;
    public Long artModified;
}
