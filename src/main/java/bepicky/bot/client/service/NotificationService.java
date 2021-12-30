package bepicky.bot.client.service;

import bepicky.bot.client.message.nats.NewsNoteNotificationSuccessMessagePublisher;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.core.BotRouter;
import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.message.builder.SendMessageBuilder;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.common.domain.dto.NewsNoteNotificationDto;
import bepicky.common.domain.request.NewsNotificationRequest;
import bepicky.common.msg.TextMessage;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Map;

@Service
@Slf4j
public class NotificationService implements INotificationService {

    @Autowired
    private BotRouter bot;

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private IReaderService readerService;

    @Autowired
    private NewsNoteNotificationSuccessMessagePublisher successMessagePublisher;

    @Override
    public void newsNoteNotification(NewsNotificationRequest r) {

        String noteMsg = buildMessage(r.getNotification(), r.getLang());
        try {
            SendMessage msg = new SendMessageBuilder(r.getChatId(), noteMsg)
                .disableNotification()
                .enableHtml()
                .build();
            bot.execute(msg);
        } catch (TelegramApiException e) {
            if (e instanceof TelegramApiRequestException) {
                TelegramApiRequestException requestException = (TelegramApiRequestException) e;
                if (requestException.getErrorCode() == 403) {
                    log.info("reader:disabled:{}", r.getChatId());
                    readerService.disable(r.getChatId());
                } else {
                    log.warn("newsnote:notification:failed " + e.getMessage());
                }
            } else {
                log.warn("newsnote:notification:failed:{}", e.getMessage());
            }
        }
        successMessagePublisher.publish(r.getChatId(), r.getNotification().getNoteId());
    }

    @Override
    public void messageNotification(TextMessage t) {
        try {
            bot.execute(new SendMessageBuilder(t.getChatId(), t.getText()).build());
        } catch (TelegramApiException e) {
            if (e instanceof TelegramApiRequestException) {
                TelegramApiRequestException requestException = (TelegramApiRequestException) e;
                if (requestException.getErrorCode() == 403) {
                    log.info("reader:disabled:{}", t.getChatId());
                    readerService.disable(t.getChatId());
                } else {
                    log.warn("reader:notification:failed:{}", t.getChatId());
                }
            } else {
                log.warn("reader:notification:failed:{}", e.getMessage());
            }
        }
    }

    private String buildMessage(NewsNoteNotificationDto n, String lang) {
        if (n.getLink() == NewsNoteNotificationDto.LinkDto.CATEGORY) {
            Map<String, Object> params = ImmutableMap.<String, Object>builder()
                .put("title", n.getTitle())
                .put("url", n.getUrl())
                .build();
            return templateContext.processTemplate(
                TemplateNames.N_NN_C,
                lang,
                params
            ).trim();
        }
        if (n.getLink() == NewsNoteNotificationDto.LinkDto.TAG) {
            Map<String, Object> params = ImmutableMap.<String, Object>builder()
                .put("title", n.getTitle())
                .put("url", n.getUrl())
                .put("tag", n.getLinkKey())
                .build();
            return templateContext.processTemplate(
                TemplateNames.N_NN_T,
                LangUtils.ALL,
                params
            ).trim();
        }
        return StringUtils.EMPTY;
    }
}
