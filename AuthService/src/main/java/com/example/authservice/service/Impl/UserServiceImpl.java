package com.example.authservice.service.Impl;


import com.example.authservice.dto.RegisterUserDto;
import com.example.authservice.model.User;
import com.example.authservice.model.role.Role;
import com.example.authservice.repository.UserRepository;
import com.example.authservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserServiceImpl implements UserService {

    UserRepository userRepository;
    ModelMapper modelMapper;
    PasswordEncoder passwordEncoder;

    @Override
    public void save(RegisterUserDto userDto) {
        User user=modelMapper.map(userDto, User.class);
        user.setRoles(Set.of(Role.ROLE_USER));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
