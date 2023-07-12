package ru.dto.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ViewStats {
    private String app;
    private String uri;
    private Long hits;
}
