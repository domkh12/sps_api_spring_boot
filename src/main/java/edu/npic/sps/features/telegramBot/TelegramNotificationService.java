package edu.npic.sps.features.telegramBot;

import edu.npic.sps.features.telegramBot.dto.TelegramMessage;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class TelegramNotificationService {

    @Value("${telegram.bot-token}")
    private String botToken;

    @Value("${telegram.chat-id}")
    private String chatId;

    private WebClient webClient;

    @PostConstruct
    public void init() {
        String baseUrl = "https://api.telegram.org/bot" + botToken;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public void sendPhotoByUrl(String imageUrl, String caption) {
        try {
            log.info("Sending photo: {}", imageUrl);
            log.info("Caption: {}", caption);
            webClient.post()
                    .uri("/sendPhoto")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(
                            TelegramMessage.builder()
                                    .chatId(chatId)
                                    .photo(imageUrl)
                                    .caption(caption)
                                    .parseMode("HTML")
                                    .build()
                    )
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(
                            response -> log.info("Photo by URL sent successfully"),
                            error -> log.error("Failed to send photo by URL: {}", error.getMessage())
                    );
        } catch (Exception e) {
            log.error("Error sending photo by URL", e);
        }
    }


    public void sendParkingNotification(String message) {
        try {

            webClient.post()
                    .uri("/sendMessage")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(TelegramMessage.builder()
                            .chatId(chatId)
                            .caption(message)
                            .parseMode("HTML")
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(
                            response -> log.info("Parking notification sent successfully"),
                            error -> log.error("Failed to send parking notification: {}", error.getMessage())
                    );

        } catch (Exception e) {
            log.error("Error sending telegram notification", e);
        }
    }

    public void sendCheckInNotification(String numberPlate, String province, LocalDateTime timeIn, String imageUrl) {
        String message = String.format(
                "🚗 <b>VEHICLE CHECK-IN</b>\n\n" +
                        "📋 <b>License Plate:</b> %s\n" +
                        "🏘️ <b>Province:</b> %s\n" +
                        "⏰ <b>Time In:</b> %s\n" +
                        "📍 <b>Status:</b> Parked Successfully\n\n" +
                        "✅ <i>Vehicle has entered the parking area</i>",
                numberPlate,
                province != null ? province : "Unknown",
                timeIn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        sendPhotoByUrl(imageUrl, message);
    }

    public void sendCheckOutNotification(String numberPlate, String province,
                                         LocalDateTime timeIn, LocalDateTime timeOut,
                                         Long durationHours, String imageUrl) {
        String message = String.format(
                "🚙 <b>VEHICLE CHECK-OUT</b>\n\n" +
                        "📋 <b>License Plate:</b> %s\n" +
                        "🏘️ <b>Province:</b> %s\n" +
                        "⏰ <b>Time In:</b> %s\n" +
                        "🕐 <b>Time Out:</b> %s\n" +
                        "⏱️ <b>Duration:</b> %d hours\n" +
                        "📍 <b>Status:</b> Departed Successfully\n\n" +
                        "🚪 <i>Vehicle has left the parking area</i>",
                numberPlate,
                province != null ? province : "Unknown",
                timeIn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                timeOut.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                durationHours
        );

        sendPhotoByUrl(imageUrl, message);
    }
}
