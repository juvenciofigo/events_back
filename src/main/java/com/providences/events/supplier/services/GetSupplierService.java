package com.providences.events.supplier.services;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier.SupplierRepository;
import com.providences.events.supplier.dto.SupplierDTO;


@Service
@Transactional(readOnly = true)
public class GetSupplierService {
    private final SupplierRepository supplierRepository;

    public GetSupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public SupplierDTO.Response execute(String supplierId, String userId) {
        SupplierEntity supplier = supplierRepository.findId(supplierId)
                .orElseThrow(() -> new BusinessException("Fornecedor não encontrado", HttpStatus.NOT_FOUND));

        if (userId != null && supplier.getUser().getId().equals(userId)) {
            // If the requester is the owner, include user details
            return SupplierDTO.Response.responseMe(supplier);
        }

        return SupplierDTO.Response.response2(supplier);

    }
}
