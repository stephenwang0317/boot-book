package com.wjm.bootbook.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;

/**
 * @author stephen wang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RedisToMqDTO {
    String key;
    Object value;
    Duration duration;
}
