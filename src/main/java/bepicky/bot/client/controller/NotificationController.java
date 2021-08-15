package bepicky.bot.client.controller;

import bepicky.bot.client.service.INotificationService;
import bepicky.bot.core.BotRouter;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.IReaderService;
import bepicky.bot.client.service.IValueNormalisationService;
import bepicky.common.domain.dto.NewsNoteDto;
import bepicky.common.domain.dto.NewsNoteNotificationDto;
import bepicky.common.domain.dto.SourcePageDto;
import bepicky.common.domain.request.NotifyMessageRequest;
import bepicky.common.domain.request.NotifyNewsRequest;
import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
public class NotificationController {

    @Autowired
    private INotificationService notificationService;

    @PutMapping("/notify/news")
    public void notifyNews(@RequestBody NotifyNewsRequest request) {
        notificationService.newsNoteNotification(request);
    }

    @PutMapping("/notify/message")
    public void notifyMessage(@RequestBody NotifyMessageRequest request) {
        notificationService.messageNotification(request);
    }
}
