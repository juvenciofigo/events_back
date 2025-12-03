package com.providences.events.shared.dto;

import java.util.List;

public class SystemDTO {

    public static record ItemWithPage<T>(
            List<T> items,
            int page,
            int totalPages,
            long totalItems) {
    }
}
