package com.providences.events.services.dto;

import java.math.BigDecimal;
import com.providences.events.services.ServiceEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ServicesBySupplierDTO {
    private String id;
    private String category;
    private String description;
    private BigDecimal priceBase;
    // private List<ServiceUnavailability> unavailability;

    public static ServicesBySupplierDTO response(ServiceEntity data) {
        // Hibernate.initialize(data.getUnavailability());

        ServicesBySupplierDTO service = new ServicesBySupplierDTO(data.getId(), data.getCategory(),
                data.getDescription(), data.getPriceBase());

        return service;
    }
}
