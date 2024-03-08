package com.wjm.bootbook.entity.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author stephen wang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @Min(value = 100000, message = "userId too short")
    @Max(value = 999999, message = "userId too long")
    Long userId;
    @NotEmpty(message = "password cannot be empty")
    String password;
}
