package bepicky.bot.client.message.nats;

import bepicky.bot.client.YamlPropertySourceFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.nats.client.Connection;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class NewsNoteNotificationSuccessMessagePublisherTest {

    @MockBean
    private Connection connection;

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NewsNoteNotificationSuccessMessagePublisher publisher;

    @Test
    public void publish_ChecksSubjectAndBody() {
        ArgumentCaptor<String> subjectAc = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<byte[]> bodyAc = ArgumentCaptor.forClass(byte[].class);

        publisher.publish(1L , 1L);

        verify(connection).publish(subjectAc.capture(), bodyAc.capture());

        assertEquals("newsnote.notification.success", subjectAc.getValue());
        String body = new String(bodyAc.getValue());
        assertEquals("{\"chat_id\":1,\"note_id\":1}", body);
    }

    @TestConfiguration
    @EntityScan(basePackages = "bepicky.bot.client")
    @PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:application.yml")
    static class NewsNoteNotificationSuccessMessagePublisherTestConfig {

        @Bean
        public ObjectMapper om() {
            return new ObjectMapper();
        }

        @Bean
        public NewsNoteNotificationSuccessMessagePublisher publisher() {
            return new NewsNoteNotificationSuccessMessagePublisher();
        }
    }

}