package bepicky.bot.client.service;

import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.core.BotRouter;
import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.message.builder.TgMessageBuilder;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.common.domain.dto.NewsNoteNotificationDto;
import bepicky.common.domain.request.NotifyMessageRequest;
import bepicky.common.domain.request.NotifyNewsRequest;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    private IValueNormalisationService normalisationService;

    @Override
    public void newsNoteNotification(NotifyNewsRequest request) {
        for (NewsNoteNotificationDto n : request.getNotifications()) {

            String noteMsg = buildMessage(n, request.getLang());
            try {
                bot.execute(TgMessageBuilder.noNotificationMsg(request.getChatId(), noteMsg));
            } catch (TelegramApiException e) {
                if (e instanceof TelegramApiRequestException) {
                    TelegramApiRequestException requestException = (TelegramApiRequestException) e;
                    if (requestException.getErrorCode() == 403) {
                        log.info("reader:disabled:{}", request.getChatId());
                        readerService.disable(request.getChatId());
                    }
                } else {
                    log.warn("reader:notify:failed:{}", e.getMessage());
                }
            }
        }
    }

    @Override
    public void messageNotification(NotifyMessageRequest request) {
        request.getReaders().parallelStream().forEach(reader -> {
            try {
                bot.execute(TgMessageBuilder.msg(reader, request.getMessage()));
            } catch (TelegramApiException e) {
                if (e instanceof TelegramApiRequestException) {
                    TelegramApiRequestException requestException = (TelegramApiRequestException) e;
                    if (requestException.getErrorCode() == 403) {
                        log.info("reader:disabled:{}", reader);
                        readerService.disable(reader);
                    }
                } else {
                    log.warn("reader:notify:failed:{}", e.getMessage());
                }
            }
        });
    }

    private String buildMessage(NewsNoteNotificationDto n, String lang) {
        if (n.getLink() == NewsNoteNotificationDto.LinkDto.CATEGORY) {
            String regions = n.getRegions();
            String categories = n.getCommons();
            String sourceNames = n.getSources();
            Map<String, Object> params = ImmutableMap.<String, Object>builder()
                .put("title", n.getTitle())
                .put("url", n.getUrl())
                .put("source", sourceNames)
                .put("region", normalisationService.normalise(regions))
                .put("category", normalisationService.normalise(categories))
                .put("author", normalisationService.normalise(n.getAuthor()))
                .build();
            return templateContext.processTemplate(
                TemplateNames.N_NN_C,
                lang,
                params
            ).trim();
        }
        if (n.getLink() == NewsNoteNotificationDto.LinkDto.TAG) {
            String sourceNames = n.getSources();
            Map<String, Object> params = ImmutableMap.<String, Object>builder()
                .put("title", n.getTitle())
                .put("url", n.getUrl())
                .put("source", sourceNames)
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
