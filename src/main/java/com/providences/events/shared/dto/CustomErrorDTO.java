package com.providences.events.shared.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomErrorDTO {
    private Instant timestamp;
    private Integer status;
    private String message;
    private String path;

}
