package com.wjm.bootbook.entity.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.URL;

/**
 * @author stephen wang
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserUpdateDTO {
    String username;
    String password;

    // TODO 修改头像
}
