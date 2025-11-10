package com.providences.events.supplier.services;

import java.util.Optional;

import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier.SupplierRepository;
import com.providences.events.supplier.dto.RegisterSupplierDTO;
import com.providences.events.user.UserEntity;
import com.providences.events.user.UserRepository;
import com.providences.events.user.dto.AuthUserDTO;

import org.springframework.stereotype.Service;

@Service
public class RegisterSupplierService {

    private final SupplierRepository supplierRepository;
    private final UserRepository userRepository;

    public RegisterSupplierService(SupplierRepository supplierRepository, UserRepository userRepository) {
        this.supplierRepository = supplierRepository;
        this.userRepository = userRepository;
    }

    public RegisterSupplierDTO.Response execute(RegisterSupplierDTO.Request data, String userId) {

        Optional<UserEntity> user = userRepository.findById(userId);

        if (user.isEmpty() || user.get().getIsDeleted() == true) {
            new ResourceNotFoundException("Utilizador não encontrado");
        }

        SupplierEntity supplier = new SupplierEntity();
                supplier.setCompanyName(data.getCompanyName());
                supplier.setDescription(data.getDescription());
                supplier.setLogo(data.getLogo().isBlank() ? user.get().getProfilePicture() : data.getLogo());
                supplier.setProfilePicture(
                        data.getProfilePicture().isBlank() ? user.get().getProfilePicture() : data.getProfilePicture());
                supplier.setUser(user.get());

        SupplierEntity savedSupplier = supplierRepository.save(supplier);

        AuthUserDTO.Response userDTO = AuthUserDTO.Response.response(savedSupplier.getUser(), null);

        return RegisterSupplierDTO.Response.response(savedSupplier, userDTO);
    }
}
