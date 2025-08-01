package edu.npic.sps.features.telegramBot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
public record TelegramMessage(
        @JsonProperty("chat_id")
        String chatId,
        String photo,
        String caption,
        @JsonProperty("parse_mode")
        String parseMode
) {
}
