package com.portal.bid.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriorityDTO {
    private Long id;
    private String priority; // Change 'name' to 'priority' for consistency
}
