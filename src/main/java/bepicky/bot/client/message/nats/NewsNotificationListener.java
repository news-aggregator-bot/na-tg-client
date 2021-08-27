package bepicky.bot.client.message.nats;

import bepicky.bot.client.service.INotificationService;
import bepicky.common.domain.request.NewsNotificationSuccessMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import bepicky.common.domain.request.NewsNotificationRequest;
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
public class NewsNotificationListener {

    @Autowired
    private Connection natsConnection;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private INotificationService notificationService;

    @Value("${topics.news.notification.new}")
    private String newsNotificationNew;

    @PostConstruct
    public void createDispatcher() {
        Dispatcher dispatcher = natsConnection.createDispatcher(msg -> {
            try {
                NewsNotificationRequest r = om.readValue(msg.getData(), NewsNotificationRequest.class);
                log.info("newsnote:notification:{}:{}", r.getChatId(), r.getNotification().toString());
                handle(r);
            } catch (IOException e) {
                log.error("newsnote:notificaton:failed " + e.getMessage());
            }
        });
        dispatcher.subscribe(newsNotificationNew);
    }

    public void handle(NewsNotificationRequest r) {
        notificationService.newsNoteNotification(r);
    }

}
