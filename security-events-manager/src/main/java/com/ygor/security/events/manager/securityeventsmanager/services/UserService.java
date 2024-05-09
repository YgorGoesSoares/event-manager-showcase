package com.ygor.security.events.manager.securityeventsmanager.services;

import com.ygor.security.events.manager.securityeventsmanager.dtos.RoleDTO;
import com.ygor.security.events.manager.securityeventsmanager.dtos.UserDTO;
import com.ygor.security.events.manager.securityeventsmanager.dtos.UserInsertDTO;
import com.ygor.security.events.manager.securityeventsmanager.entities.Role;
import com.ygor.security.events.manager.securityeventsmanager.entities.User;
import com.ygor.security.events.manager.securityeventsmanager.repository.RoleRepository;
import com.ygor.security.events.manager.securityeventsmanager.repository.UserRepository;
import com.ygor.security.events.manager.securityeventsmanager.services.exceptions.DatabaseException;
import com.ygor.security.events.manager.securityeventsmanager.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {

        Page<User> list = userRepository.findAll(pageable);
        return list.map(x -> new UserDTO(x));

    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> obj = userRepository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found."));
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insertNewUser(UserInsertDTO dto) {
        User entity = new User();
        copyDtoToEntity(dto, entity);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }



    @Transactional
    public UserDTO updateUser(Long id, UserDTO dto) {
        try {User entity = userRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = userRepository.save(entity);
            return new UserDTO(entity);}
        catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation: " + e.getCause());
        }

    }

    public void deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
        catch (EmptyResultDataAccessException e) {
            throw new ResourceNotFoundException("Id not found (" + id +")");
        }
    }

    private void copyDtoToEntity(UserDTO dto, User entity) {
        if (dto.getEmail() != null) {
            entity.setEmail(dto.getEmail());
        }

        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            Set<Role> existingRoles = entity.getRoles();
            Set<Long> dtoRoleIds = dto.getRoles().stream().map(RoleDTO::getId).collect(Collectors.toSet());

            for (RoleDTO roleDTO : dto.getRoles()) {
                if (!existingRoles.contains(roleDTO.getId())) {
                    Role role = roleRepository.getReferenceById(roleDTO.getId());
                    existingRoles.add(role);
                }
            }

            existingRoles.removeIf(role -> !dtoRoleIds.contains(role.getId()));

        }
    }
}
