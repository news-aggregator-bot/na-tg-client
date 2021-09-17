package bepicky.bot.client.message.async;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
@Component
public class NatsFinisherManager {

    @Autowired
    private Connection natsConnection;

    @Autowired
    private List<NatsMessageFinisher> finishers;

    @PostConstruct
    public void subscribeFinishers() {
        for (NatsMessageFinisher finisher : finishers) {
            Dispatcher dispatcher = natsConnection.createDispatcher(msg -> finisher.finish(msg.getData()));
            dispatcher.subscribe(finisher.finishSubject());
            log.info("finisher:{}", finisher.finishSubject());
        }
    }
}
