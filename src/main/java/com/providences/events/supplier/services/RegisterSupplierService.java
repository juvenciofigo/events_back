package com.providences.events.supplier.services;

import com.providences.events.shared.exception.exceptions.BusinessException;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier.SupplierRepository;
import com.providences.events.supplier.dto.SupplierDTO;
import com.providences.events.user.UserEntity;
import com.providences.events.user.UserRepository;
import com.providences.events.user.dto.AuthUserDTO;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterSupplierService {

    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    public RegisterSupplierService(SupplierRepository supplierRepository, UserRepository userRepository) {
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
    }

    public SupplierDTO.Response execute(SupplierDTO.Create data, String userId) {

        UserEntity user = userRepository.findId(userId)
                .filter(u -> !u.getIsDeleted())
                .orElseThrow(() -> new BusinessException("Utilizador não encontrado", HttpStatus.NOT_FOUND));

        boolean alreadyHasSupplier = supplierRepository.existsByUserId(userId);
        if (alreadyHasSupplier) {
            throw new BusinessException("Esse utilizador já possui um fornecedor!", HttpStatus.CONFLICT);
        }

        boolean companyNameExists = supplierRepository.existsByCompanyName(data.getCompanyName());
        if (companyNameExists) {
            throw new BusinessException("Nome da empresa existente, escolha outro!", HttpStatus.CONFLICT);
        }

        SupplierEntity supplier = new SupplierEntity();
        supplier.setCompanyName(data.getCompanyName());
        supplier.setDescription(data.getDescription() != null ? data.getDescription() : "");
        supplier.setLogo("");
        supplier.setProfilePicture("");
        supplier.setUser(user);

        SupplierEntity savedSupplier = supplierRepository.save(supplier);

        AuthUserDTO.Response userDTO = AuthUserDTO.Response.response(user, null);

        return SupplierDTO.Response.response(savedSupplier, userDTO);
    }

}
