package com.yaha.telegram_sender.controllers;

import com.yaha.telegram_sender.model.dto.ChatLinkRequest;
import com.yaha.telegram_sender.model.dto.LoginRequest;
import com.yaha.telegram_sender.model.dto.MessageRequest;
import com.yaha.telegram_sender.model.dto.RegisterRequest;
import com.yaha.telegram_sender.services.MessageService;
import com.yaha.telegram_sender.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private final UserService userService;
    private final MessageService messageService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.loginUser(request));
    }

    @PostMapping("/update-chat")
    public ResponseEntity<?> chat(@RequestBody ChatLinkRequest request) {
        userService.updateChat(request.getChatId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/update-chat")
    public ResponseEntity<?> chat() {
        userService.updateChat(null);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send-message")
    public ResponseEntity<?> send(@RequestBody MessageRequest request) {
        messageService.sendMessage(request.getText());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-messages")
    public ResponseEntity<?> messages() {
        return ResponseEntity.ok().body(messageService.getMessages());
    }

}
