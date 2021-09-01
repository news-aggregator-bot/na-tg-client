package bepicky.bot.client.message.nats;

import bepicky.bot.client.service.INotificationService;
import bepicky.common.domain.request.NewsNotificationRequest;
import bepicky.common.msg.TextMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Component
@Slf4j
public class ReaderMessageNotificationListener {

    @Autowired
    private Connection natsConnection;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private INotificationService notificationService;

    @Value("${topics.reader.notification}")
    private String readerNotificationSubject;

    @PostConstruct
    public void createDispatcher() {
        Dispatcher dispatcher = natsConnection.createDispatcher(msg -> {
            try {
                TextMessage t = om.readValue(msg.getData(), TextMessage.class);
                log.info("reader:notification:{}:{}", t.getChatId(), t.toString());
                handle(t);
            } catch (IOException e) {
                log.error("reader:notificaton:failed " + e.getMessage());
            }
        });
        dispatcher.subscribe(readerNotificationSubject);
    }

    public void handle(TextMessage t) {
        notificationService.messageNotification(t);
    }

}
