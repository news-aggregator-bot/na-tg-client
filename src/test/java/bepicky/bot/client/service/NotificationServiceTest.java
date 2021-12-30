package bepicky.bot.client.service;

import bepicky.bot.client.config.TemplateConfig;
import bepicky.bot.client.message.nats.NewsNoteNotificationSuccessMessagePublisher;
import bepicky.bot.core.BotRouter;
import bepicky.bot.core.cmd.CommandManager;
import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.common.domain.dto.NewsNoteNotificationDto;
import bepicky.common.domain.request.NewsNotificationRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    private static final long CHAT_ID = 1L;

    @InjectMocks
    private NotificationService notificationService;

    @Mock
    private BotRouter bot;

    @Mock
    private NewsNoteNotificationSuccessMessagePublisher successMessagePublisher;

    protected TemplateConfig templateConfig = new TemplateConfig();

    @Spy
    protected MessageTemplateContext templateContext =
        new MessageTemplateContext(templateConfig.templateConfiguration());

    @Spy
    protected CommandManager commandManager = new CommandManager();

    @Test
    public void newsNoteNotification_NoteContainsTitleUrlCommon_ShouldNotifyWithCorrectMessage()
        throws TelegramApiException {
        ArgumentCaptor<SendMessage> sendMsgAc = ArgumentCaptor.forClass(SendMessage.class);
        NewsNoteNotificationDto noteRequest = newNoteReq("title", "url", null);
        NewsNotificationRequest request = notifyNewsReq(noteRequest);
        doNothing().when(successMessagePublisher).publish(any(), any());

        notificationService.newsNoteNotification(request);

        verify(bot).execute(sendMsgAc.capture());

        SendMessage value = sendMsgAc.getValue();
        assertTrue(value.getDisableNotification());
        assertEquals(CHAT_ID, Long.parseLong(value.getChatId()));
        assertEquals("<a href=\"url\">title</a>", value.getText());
    }

    @Test
    public void newsNoteNotification_TagNotification_ShouldNotifyWithCorrectMessage()
        throws TelegramApiException {
        ArgumentCaptor<SendMessage> sendMsgAc = ArgumentCaptor.forClass(SendMessage.class);
        NewsNoteNotificationDto noteRequest = newNoteReq(
            "title",
            "url",
            null,
            NewsNoteNotificationDto.LinkDto.TAG,
            "linkkey"
        );
        NewsNotificationRequest request = notifyNewsReq(noteRequest);
        doNothing().when(successMessagePublisher).publish(any(), any());

        notificationService.newsNoteNotification(request);

        verify(bot).execute(sendMsgAc.capture());

        SendMessage value = sendMsgAc.getValue();
        assertTrue(value.getDisableNotification());
        assertEquals(CHAT_ID, Long.parseLong(value.getChatId()));
        assertEquals("#linkkey\n<a href=\"url\">title</a>", value.getText());
    }

    @Test
    public void newsNoteNotification_NoteNotContainsUrl_ShouldThrowAndException() {
        NewsNoteNotificationDto noteRequest = newNoteReq("title", null, null);
        NewsNotificationRequest request = notifyNewsReq(noteRequest);

        Assertions.assertThrows(
            NullPointerException.class, () -> notificationService.newsNoteNotification(request)
        );
    }

    @Test
    public void newsNoteNotification_NoteNotContainsTitle_ShouldThrowAndException() {
        NewsNoteNotificationDto noteRequest = newNoteReq(null, "url", null);
        NewsNotificationRequest request = notifyNewsReq(noteRequest);

        Assertions.assertThrows(
            NullPointerException.class, () -> notificationService.newsNoteNotification(request)
        );
    }

    private NewsNotificationRequest notifyNewsReq(NewsNoteNotificationDto notification) {
        NewsNotificationRequest request = new NewsNotificationRequest();
        request.setChatId(CHAT_ID);
        request.setLang(LangUtils.DEFAULT);
        request.setNotification(notification);
        return request;
    }

    private NewsNoteNotificationDto newNoteReq(String title, String url, String author) {
        NewsNoteNotificationDto n = new NewsNoteNotificationDto();
        n.setTitle(title);
        n.setUrl(url);
        n.setAuthor(author);
        n.setLink(NewsNoteNotificationDto.LinkDto.CATEGORY);
        return n;
    }

    private NewsNoteNotificationDto newNoteReq(
        String title,
        String url,
        String author,
        NewsNoteNotificationDto.LinkDto linkDto,
        String key
    ) {
        NewsNoteNotificationDto n = new NewsNoteNotificationDto();
        n.setTitle(title);
        n.setUrl(url);
        n.setAuthor(author);
        n.setLink(linkDto);
        n.setLinkKey(key);
        return n;
    }

}