package edu.npic.sps.config;

import edu.npic.sps.base.Status;
import edu.npic.sps.features.user.UserRepository;
import edu.npic.sps.features.user.UserServiceImpl;
import edu.npic.sps.features.user.dto.IsOnlineRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final UserServiceImpl userServiceImpl;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        // Assuming you have a way to get the uuid from the headers or the principal
        String uuid = headerAccessor.getNativeHeader("uuid").get(0); // Get uuid from headers
        headerAccessor.getSessionAttributes().put("uuid", uuid); // Store uuid in session attributes
        log.info("Received a new session connect event: {}", headerAccessor);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        // Retrieve the session attributes
        String uuid = (String) headerAccessor.getSessionAttributes().get("uuid");
        // Now you can use the uuid for your logic
        log.info("Received a new session disconnect event: {}", headerAccessor);

        userServiceImpl.connectedUsers(uuid, new IsOnlineRequest(false));

    }

}
