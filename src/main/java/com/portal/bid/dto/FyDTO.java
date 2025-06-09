package com.portal.bid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FyDTO {
    private Long id;
    private int obFy;  // Change 'name' to 'obFy'
}
