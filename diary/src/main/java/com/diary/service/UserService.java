package com.diary.service;

import com.diary.dto.UserResponseDto;
import com.diary.entity.User;
import com.diary.exception.ResourceNotFoundException;
import com.diary.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean login(String email, String rawPassword){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new  ResourceNotFoundException("User not found"));

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    public User userRegister(User user){
        user.setUsername(user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public UserResponseDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new  ResourceNotFoundException("User not found"));
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setUsername(user.getUsername());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setId(user.getId());

        return userResponseDto;
    }

    public User updateUser(Long id, User updateData) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new  ResourceNotFoundException("User not found"));
        existingUser.setUsername(updateData.getUsername());
        existingUser.setEmail(updateData.getEmail());
        if (updateData.getPassword() != null && !updateData.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updateData.getPassword()));
        }
        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id, String currentUserEmail) {
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new  ResourceNotFoundException("User not found :("));
        if (!currentUser.getId().equals(id)) {
            throw new AccessDeniedException("You can't delete other users!");
        }
        userRepository.deleteById(id);
    }
}
