package com.hotelmanager.services;

import java.lang.reflect.Type;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import com.hotelmanager.dtos.role.RoleDto;
import com.hotelmanager.exception.User.RoleCustomException;
import com.hotelmanager.models.Role;
import com.hotelmanager.repositories.RoleRepository;

import jakarta.transaction.Transactional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleService(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public List<RoleDto> getRoles() {
        try {
            List<Role> roles = this.roleRepository.findAll();
            if (roles.isEmpty()) {
                throw new RoleCustomException.RoleNotFoundException();
            }
            return MapToListOfRoleDto(roles);
        } catch (RoleCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Role findRoleByName(String roleName) {
        try {
            if (roleName == null || roleName.trim().isEmpty()) {
                throw new RoleCustomException.InvalidRoleNameException();
            }

            return this.roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RoleCustomException.RoleNotFoundException());
        } catch (RoleCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Role findRoleById(int roleId) {
        try {
            if (roleId <= 0) {
                throw new RoleCustomException.RoleIdNotFoundException();
            }

            return this.roleRepository.findById(roleId)
                    .orElseThrow(() -> new RoleCustomException.RoleIdNotFoundException());
        } catch (RoleCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Role CreateRole(Role role) {
        try {
            if (role == null || role.getName() == null || role.getName().trim().isEmpty()) {
                throw new RoleCustomException.InvalidRoleNameException();
            }

            boolean roleExists = this.roleRepository.findByName(role.getName()).isPresent();
            if (roleExists) {
                throw new RoleCustomException.RoleAlreadyExistsException();
            }

            return roleRepository.save(role);
        } catch (RoleCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Role UpdateRole(Role role) {
        try {
            if (role == null || role.getName() == null || role.getName().trim().isEmpty()) {
                throw new RoleCustomException.InvalidRoleNameException();
            }
            if (role.getId() <= 0) {
                throw new RoleCustomException.RoleIdNotFoundException();
            }

            Role existingRole = roleRepository.findById(role.getId())
                    .orElseThrow(() -> new RoleCustomException.RoleIdNotFoundException());

            existingRole.setName(role.getName());
            return roleRepository.save(existingRole);
        } catch (RoleCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Role DeleteRole(String roleName) {
        try {
            if (roleName == null || roleName.trim().isEmpty()) {
                throw new RoleCustomException.InvalidRoleNameException();
            }

            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new RoleCustomException.RoleNotFoundException());

            roleRepository.delete(role);
            return role;
        } catch (RoleCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<RoleDto> MapToListOfRoleDto(List<Role> roles) {
        try {
            if (roles == null) {
                throw new RoleCustomException.RoleNotFoundException();
            }

            Type listType = new TypeToken<List<RoleDto>>() {
            }.getType();
            return this.modelMapper.map(roles, listType);
        } catch (RoleCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
