package com.providences.events.supplier.services;

import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier.SupplierRepository;
import com.providences.events.supplier.dto.SupplierDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateSupplierService {

    private final SupplierRepository supplierRepository;

    public UpdateSupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    public SupplierDTO.Response execute(String supplierId, SupplierDTO.Update data, String userId) {

        SupplierEntity supplier = supplierRepository.findId(supplierId)
                .orElseThrow(() -> new BusinessException("Fornecedor não encontrado", HttpStatus.NOT_FOUND));

        if (!supplier.getUser().getId().equals(userId)) {
            throw new BusinessException("Não autoridado", HttpStatus.FORBIDDEN);
        }

        supplier.setCompanyName(data.getCompanyName());
        supplier.setDescription(data.getDescription());

        return SupplierDTO.Response.response2(supplierRepository.save(supplier));
    }
}
