package com.wjm.bootbook.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author stephen wang
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateVO {
    String username;
    String avatar;
}
