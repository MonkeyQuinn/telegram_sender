package com.yaha.telegram_sender.services;

import com.yaha.telegram_sender.model.dto.LoginRequest;
import com.yaha.telegram_sender.model.dto.LoginResponse;
import com.yaha.telegram_sender.model.dto.RegisterRequest;
import com.yaha.telegram_sender.model.entities.AppUser;

public interface UserService {

    void registerUser(RegisterRequest request);

    LoginResponse loginUser(LoginRequest request);

    void updateChat(Long chatId);

    AppUser getCurrentUser();

}
