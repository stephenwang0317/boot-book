package com.wjm.bootbook.entity.dto.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author stephen wang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TextReviewDTO {
    String conclusion;
    List<DataElement> data;

    public String getReason() {
        StringBuilder sb = new StringBuilder();
        data.forEach(i -> sb.append(i.getMsg()).append(","));
        return sb.toString();
    }
}
