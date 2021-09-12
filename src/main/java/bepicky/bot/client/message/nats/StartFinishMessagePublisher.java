package bepicky.bot.client.message.nats;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class StartFinishMessagePublisher {

    @Autowired
    private Connection natsConnection;

    @Autowired
    private ObjectMapper om;

    public void publish(String startSubject, String finishSubject, Object data) {
        try {
            natsConnection.publish(
                startSubject,
                finishSubject,
                om.writeValueAsString(data).getBytes(StandardCharsets.UTF_8)
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
}
