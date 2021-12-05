package bepicky.bot.client.message.nats;

import io.nats.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class AdminMessagePublisher {

    private static final String ID = "tg-client";

    private final Connection natsConnection;

    @Value("${topics.message.admin}")
    private String adminSubject;

    public AdminMessagePublisher(Connection natsConnection) {this.natsConnection = natsConnection;}

    public void publish(String... text) {
        natsConnection.publish(
            adminSubject,
            String.join("\n", ID, String.join("\n", text)).getBytes(StandardCharsets.UTF_8)
        );
    }
}
