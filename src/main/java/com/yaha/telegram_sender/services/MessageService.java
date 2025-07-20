package com.yaha.telegram_sender.services;

import com.yaha.telegram_sender.model.dto.MessageResponse;

import java.util.List;

public interface MessageService {

    void sendMessage(String text);

    List<MessageResponse> getMessages();

}
