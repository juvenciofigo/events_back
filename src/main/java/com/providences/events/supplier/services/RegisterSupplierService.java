package com.providences.events.supplier.services;

import java.util.Optional;

import com.providences.events.shared.exception.exceptions.ResourceNotFoundException;
import com.providences.events.supplier.SupplierEntity;
import com.providences.events.supplier.SupplierRepository;
import com.providences.events.supplier.dto.RegisterSupplierDTO;
import com.providences.events.user.UserEntity;
import com.providences.events.user.UserRepository;

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

        SupplierEntity supplier = SupplierEntity.builder()
                .companyName(data.getCompanyName())
                .description(data.getDescription())
                .logo(data.getLogo().isBlank() ? user.get().getProfilePicture() : data.getLogo())
                .profilePicture(
                        data.getProfilePicture().isBlank() ? user.get().getProfilePicture() : data.getProfilePicture())
                .user(user.get())
                .build();

        SupplierEntity savedSupplier = supplierRepository.save(supplier);

        RegisterSupplierDTO.UserDTO userDTO = new RegisterSupplierDTO.UserDTO(
                savedSupplier.getUser().getId(),
                savedSupplier.getUser().getName(),
                savedSupplier.getUser().getEmail(),
                savedSupplier.getUser().getProfilePicture());

        RegisterSupplierDTO.Response responseDTO = new RegisterSupplierDTO.Response(
                savedSupplier.getId(),
                savedSupplier.getCompanyName(),
                savedSupplier.getProfilePicture(),
                savedSupplier.getLogo(),
                savedSupplier.getDescription(),
                userDTO);

        return responseDTO;
    }
}
