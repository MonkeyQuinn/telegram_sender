package com.yaha.telegram_sender.services.impl;

import com.yaha.telegram_sender.exceptions.ApiException;
import com.yaha.telegram_sender.model.dto.LoginRequest;
import com.yaha.telegram_sender.model.dto.LoginResponse;
import com.yaha.telegram_sender.model.dto.RegisterRequest;
import com.yaha.telegram_sender.model.entities.AppUser;
import com.yaha.telegram_sender.repositories.UserRepository;
import com.yaha.telegram_sender.services.JwtService;
import com.yaha.telegram_sender.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public void registerUser(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ApiException("Username already exists", HttpStatus.CONFLICT);
        }

        checkPassword(request.getPassword(), request.getConfirmPassword());
        AppUser user = new AppUser();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        userRepository.save(user);
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        AppUser user = findUserByUsername(request.getUsername());

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ApiException("Invalid password", HttpStatus.UNAUTHORIZED);
        }

        String token = jwtService.generateToken(user);
        return new LoginResponse(token);
    }

    @Override
    public void updateChat(Long chatId) {
        if (chatId != null && userRepository.existsByChatId(chatId)) {
            throw new ApiException("Chat already exists", HttpStatus.CONFLICT);
        }

        AppUser user = findUserByUsername(getCurrentUser().getUsername());
        user.setChatId(chatId);
        userRepository.save(user);
    }

    @Override
    public AppUser getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return findUserByUsername(username);
    }

    private AppUser findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
    }

    private void checkPassword(String password, String confirmPassword) {
        if (password.length() < 8) {
            throw new ApiException("Password must be at least 8 characters", HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (!password.equals(confirmPassword)) {
            throw new ApiException("Passwords do not match", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

}
