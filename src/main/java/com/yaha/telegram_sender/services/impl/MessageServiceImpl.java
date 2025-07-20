package com.yaha.telegram_sender.services.impl;

import com.yaha.telegram_sender.components.Bot;
import com.yaha.telegram_sender.exceptions.ApiException;
import com.yaha.telegram_sender.model.dto.MessageResponse;
import com.yaha.telegram_sender.model.dto.MessageToBotDto;
import com.yaha.telegram_sender.model.entities.AppUser;
import com.yaha.telegram_sender.model.entities.UserMessage;
import com.yaha.telegram_sender.repositories.MessageRepository;
import com.yaha.telegram_sender.services.MessageService;
import com.yaha.telegram_sender.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final Bot bot;

    @Override
    public void sendMessage(String text) {
        sendToBot(text);
        saveMessage(text);
    }

    @Override
    public List<MessageResponse> getMessages() {
        return messageRepository.findAllByAuthor(userService.getCurrentUser())
                .stream()
                .map(e -> new MessageResponse(e.getText(), e.getCreatedAt()))
                .toList();
    }

    private void saveMessage(String text) {
        UserMessage message = new UserMessage();
        message.setText(text);
        message.setAuthor(userService.getCurrentUser());
        messageRepository.save(message);
    }

    private void sendToBot(String text) {
        AppUser user = userService.getCurrentUser();

        if (user.getChatId() == null) {
            throw new ApiException("Chat is not linked", HttpStatus.CONFLICT);
        }

        try {
            bot.notifyUser(new MessageToBotDto(
                    user.getChatId(),
                    user.getName(),
                    text
            ));

        } catch (TelegramApiException e) {
            throw new ApiException("Failed to send message to Telegram chat " + user.getChatId(), HttpStatus.CONFLICT);
        }
    }

}
