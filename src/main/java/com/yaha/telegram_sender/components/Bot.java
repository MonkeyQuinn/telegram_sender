package com.yaha.telegram_sender.components;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaha.telegram_sender.model.dto.ChatLinkRequest;
import com.yaha.telegram_sender.model.dto.MessageToBotDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Map;

@Component
@Slf4j
public class Bot extends TelegramLongPollingBot {

    private final String username;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public Bot(@Value("${telegram.bot.token}") String token,
               @Value("${telegram.bot.username}") String username,
               RestClient restClient,
               ObjectMapper objectMapper) {
        super(token);
        this.username = username;
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public void onUpdateReceived(Update update) {
        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText();

        if (text.equals("/start")) {
            sendRawSafe(chatId, "Please enter your token");

        } else {
            try {
                linkChat(text, new ChatLinkRequest(chatId));
                sendRawSafe(chatId, "Chat successfully linked!");

            } catch (RestClientResponseException e) {
                String message = extractMessage(e.getResponseBodyAsString(), "An error occurred");
                sendRawSafe(chatId, message);

            } catch (Exception e) {
                log.error("Unexpected error while linking chat", e);
                sendRawSafe(chatId, e.getMessage());
            }
        }
    }

    public void notifyUser(MessageToBotDto messageDto) throws TelegramApiException {
        String text = String.format(
                "%s, я получил от тебя сообщение:\n%s",
                messageDto.getName(), messageDto.getText()
        );
        sendRaw(messageDto.getChatId(), text);
    }

    private void sendRaw(Long chatId, String text) throws TelegramApiException {
        execute(SendMessage.builder().chatId(chatId).text(text).build());
    }

    private void sendRawSafe(Long chatId, String text) {
        try {
            execute(SendMessage.builder().chatId(chatId).text(text).build());

        } catch (TelegramApiException e) {
            log.error("Failed to send message to Telegram chat {}", chatId, e);
        }
    }

    private void linkChat(String token, ChatLinkRequest payload) {
        restClient.post()
                .uri(uriBuilder -> UriComponentsBuilder.fromPath("/api/link-chat").build().toUri())
                .header("Authorization", "Bearer " + token)
                .body(payload)
                .retrieve()
                .toBodilessEntity();
    }

    private String extractMessage(String json, String defaultMessage) {
        try {
            Map<String, String> map = objectMapper.readValue(json, new TypeReference<>() {
            });
            return map.getOrDefault("message", defaultMessage);

        } catch (JsonProcessingException e) {
            log.error("Failed to parse error response JSON: {}", json, e);
            return defaultMessage;
        }
    }

}
