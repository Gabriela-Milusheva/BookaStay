package com.hotelmanager.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hotelmanager.dtos.User.ChangeUserPasswordDto;
import com.hotelmanager.dtos.User.LoginResponse;
import com.hotelmanager.dtos.User.LoginUserDto;
import com.hotelmanager.dtos.User.RegisterUserDto;
import com.hotelmanager.dtos.User.UpdateUserRequest;
import com.hotelmanager.dtos.User.UserDto;
import com.hotelmanager.enumerations.User.RoleEnum;
import com.hotelmanager.exception.User.UserCustomException;
import com.hotelmanager.models.Role;
import com.hotelmanager.models.User;
import com.hotelmanager.repositories.UserRepository;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final Logger logger;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    @Transactional
    public UserDto getUser(UUID id) {
        try {
            User user = this.userRepository.findById(id).orElse(null);

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            UserDto userDto = this.modelMapper.map(user, UserDto.class);
            this.setUserDtoRoleName(userDto, user.getRole());
            return userDto;
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public User getUserEntityById(UUID id) {
        try {
            User user = this.userRepository.findById(id).orElse(null);

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            return user;
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public User getUserEntityByEmail(String email) {
        try {
            User user = this.userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            return user;
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public List<UserDto> getUsers() {
        try {
            List<User> users = this.userRepository.findAll();

            return users.stream()
                    .map(user -> modelMapper.map(user, UserDto.class))
                    .collect(Collectors.toList());
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public UserDto findById(UUID id) {
        try {
            User user = userRepository.findById(id).orElse(null);

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            return this.MapUserToDto(user);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public UserDto findByEmail(String email) {
        try {
            if (email.isEmpty()) {
                throw new UserCustomException.InvalidEmail();
            }

            User user = userRepository.findByEmail(email).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            return this.MapUserToDto(user);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public UserDto findByUsername(String username) {
        try {
            if (username.isEmpty()) {
                throw new UserCustomException.InvalidUsernameException();
            }

            User user = userRepository.findByUsername(username).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            return this.MapUserToDto(user);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public UserDto registerUser(RegisterUserDto userDto) {
        try {
            if (userDto == null) {
                throw new UserCustomException.InvalidUserDto();
            }

            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new UserCustomException.UserAlreadyExistsException();
            }

            User user = new User();
            user.setEmail(userDto.getEmail());
            user.setUsername(userDto.getUsername());
            user.setPassword(hashPassword(userDto.getPassword()));
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setPhone(userDto.getPhone());

            Role role = this.roleService.findRoleByName(RoleEnum.USER.getRoleName());
            user.setRole(role);

            userRepository.save(user);

            return this.MapUserToDto(user);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public LoginResponse loginUser(LoginUserDto loginUserDto) {
        try {
            if (loginUserDto == null) {
                throw new UserCustomException.InvalidLoginUserDto();
            }

            User user = this.userRepository.findByEmail(loginUserDto.getEmail()).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            if (!passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())) {
                throw new UserCustomException.PasswordMismatchException();
            }

            UserDto userDto = this.MapUserToDto(user);
            this.setUserDtoRoleName(userDto, user.getRole());

            this.userRepository.save(user);

            userDto.setRoleId(user.getRole().getId());
            userDto.setRoleName(user.getRole().getName());
            return new LoginResponse(userDto);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public UserDto updateUser(UUID userId, UpdateUserRequest userRequest) {
        try {
            if (userRequest == null) {
                throw new UserCustomException.InvalidUserDto();
            }

            User userForUpdate = this.userRepository.findById(userId).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            Role role = this.roleService.findRoleById(userRequest.getRoleId());

            userForUpdate.setFirstName(userRequest.getFirstName());
            userForUpdate.setLastName(userRequest.getLastName());
            userForUpdate.setPhone(userRequest.getPhone());
            userForUpdate.setUsername(userRequest.getUsername());
            userForUpdate.setRole(role);
            userForUpdate.setEmail(userRequest.getEmail());

            this.userRepository.save(userForUpdate);

            return this.MapUserToDto(userForUpdate);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void updatePassword(ChangeUserPasswordDto userDto) {
        try {
            if (userDto == null) {
                throw new UserCustomException.InvalidUserDto();
            }

            User user = this.userRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            if (!passwordEncoder.matches(userDto.getOldPassword(), user.getPassword())) {
                throw new UserCustomException.PasswordMismatchException();
            }

            user.setPassword(hashPassword(userDto.getNewPassword()));
            this.userRepository.save(user);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public UserDto deleteUser(UUID id) {
        try {
            if (id.toString().isEmpty()) {
                throw new UserCustomException.InvalidId();
            }

            User userForDelete = this.userRepository.findById(id).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            this.userRepository.delete(userForDelete);

            return this.MapUserToDto(userForDelete);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = this.userRepository.findByEmail(email).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    new ArrayList<>()
            );
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private UserDto MapUserToDto(User user) {
        try {
            UserDto dto = modelMapper.map(user, UserDto.class);

            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UserDto MapUserDtoToUserEntity(User user) {
        try {
            return this.modelMapper.map(user, UserDto.class);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setUserDtoRoleName(UserDto userDto, Role role) {
        try {
            userDto.setRoleName(role.getName());
            userDto.setRoleId(role.getId());
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
