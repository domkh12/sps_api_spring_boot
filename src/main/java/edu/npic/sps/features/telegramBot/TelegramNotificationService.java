package edu.npic.sps.features.telegramBot;

import edu.npic.sps.features.telegramBot.dto.TelegramMessage;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
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
            log.info("Chat ID: {}", chatId);

            // Use form data instead of JSON for sendPhoto with URL
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("chat_id", chatId);
            formData.add("photo", imageUrl);
            formData.add("caption", caption);
            formData.add("parse_mode", "HTML");

            webClient.post()
                    .uri("/sendPhoto")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(
                            response -> {
                                log.info("Photo by URL sent successfully");
                                log.debug("Response: {}", response);
                            },
                            error -> {
                                log.error("Failed to send photo by URL: {}", error.getMessage());
                                // Fallback: try sending as text message
                                sendTextMessage(caption);
                            }
                    );
        } catch (Exception e) {
            log.error("Error sending photo by URL", e);
            // Fallback: try sending as text message
            sendTextMessage(caption);
        }
    }

    // Fallback method to send text message if photo fails
    private void sendTextMessage(String text) {
        try {
            MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
            formData.add("chat_id", chatId);
            formData.add("text", text);
            formData.add("parse_mode", "HTML");

            webClient.post()
                    .uri("/sendMessage")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(String.class)
                    .subscribe(
                            response -> log.info("Text message sent as fallback"),
                            error -> log.error("Failed to send fallback text message: {}", error.getMessage())
                    );
        } catch (Exception e) {
            log.error("Error sending fallback text message", e);
        }
    }

    // Helper method to format duration from minutes to a readable format
    private String formatDuration(Long durationMinutes) {
        if (durationMinutes == null || durationMinutes < 0) {
            return "Unknown";
        }

        long hours = durationMinutes / 60;
        long minutes = durationMinutes % 60;

        if (hours > 0 && minutes > 0) {
            return String.format("%d hours %d minutes", hours, minutes);
        } else if (hours > 0) {
            return String.format("%d hours", hours);
        } else {
            return String.format("%d minutes", minutes);
        }
    }

    public void sendCheckInNotification(String numberPlate, String province, LocalDateTime timeIn, String imageUrl, String branchName) {
        String message = String.format(
                "🚗 <b>VEHICLE CHECK-IN</b>\n\n" +
                        "📋 <b>License Plate:</b> %s\n" +
                        "🏘️ <b>Province:</b> %s\n" +
                        "🏢 <b>Branch:</b> %s\n" +
                        "⏰ <b>Time In:</b> %s\n" +
                        "📍 <b>Status:</b> Parked Successfully\n\n" +
                        "✅ <i>Vehicle has entered the parking area</i>",
                numberPlate,
                province != null ? province : "Unknown",
                branchName != null ? branchName : "Unknown",
                timeIn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        );

        sendPhotoByUrl(imageUrl, message);
    }

    // Updated method signature to accept duration in minutes
    public void sendCheckOutNotification(String numberPlate, String province,
                                         LocalDateTime timeIn, LocalDateTime timeOut,
                                         Long durationMinutes, String imageUrl, String branchName) {
        String message = String.format(
                "🚙 <b>VEHICLE CHECK-OUT</b>\n\n" +
                        "📋 <b>License Plate:</b> %s\n" +
                        "🏘️ <b>Province:</b> %s\n" +
                        "🏢 <b>Branch:</b> %s\n" +
                        "⏰ <b>Time In:</b> %s\n" +
                        "🕐 <b>Time Out:</b> %s\n" +
                        "⏱️ <b>Duration:</b> %s\n" +
                        "📍 <b>Status:</b> Departed Successfully\n\n" +
                        "🚪 <i>Vehicle has left the parking area</i>",
                numberPlate,
                province != null ? province : "Unknown",
                branchName != null ? branchName : "Unknown",
                timeIn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                timeOut.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                formatDuration(durationMinutes)
        );

        sendPhotoByUrl(imageUrl, message);
    }
}