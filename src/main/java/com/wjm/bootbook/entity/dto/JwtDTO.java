package com.wjm.bootbook.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author stephen wang
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JwtDTO {
    Long userId;
    String username;
}
