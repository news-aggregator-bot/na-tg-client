package bepicky.bot.client.message.nats;

import bepicky.common.domain.request.NewsNotificationSuccessMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class NewsNoteNotificationSuccessMessagePublisher {

    @Autowired
    private Connection natsConnection;

    @Autowired
    private ObjectMapper om;

    @Value("${topics.news.notification.success}")
    private String newsNotificationSuccess;

    public void publish(Long chatId, Long noteId) {
        NewsNotificationSuccessMessage success =
            new NewsNotificationSuccessMessage(
                chatId, noteId
            );
        try {
            natsConnection.publish(
                newsNotificationSuccess,
                om.writeValueAsString(success).getBytes(StandardCharsets.UTF_8)
            );
        } catch (JsonProcessingException e) {
            log.error("news:notification success:failed chatid {} noteid {}", chatId, noteId, e);
        }
    }
}
