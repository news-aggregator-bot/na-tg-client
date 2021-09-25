package bepicky.bot.client.message.handler.common;

import bepicky.bot.client.message.button.MenuKeyboardBuilder;
import bepicky.bot.client.service.IReaderService;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.message.builder.SendMessageBuilder;
import bepicky.bot.core.message.handler.MessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.request.ReaderRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.User;

import static bepicky.bot.client.message.template.TemplateNames.WELCOME;

@Component
@Slf4j
public class WelcomeMessageHandler implements MessageHandler {

    private final MessageTemplateContext templateContext;

    private final IReaderService readerService;

    private final MenuKeyboardBuilder menuKeyboardBuilder;

    public WelcomeMessageHandler(
        MessageTemplateContext templateContext,
        IReaderService readerService,
        MenuKeyboardBuilder menuKeyboardBuilder
    ) {
        this.templateContext = templateContext;
        this.readerService = readerService;
        this.menuKeyboardBuilder = menuKeyboardBuilder;
    }

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        ReaderDto reader = checkRegistered(cc);
        String text = templateContext.processTemplate(
            WELCOME,
            reader.getPrimaryLanguage().getLang()
        );
        return new SendMessageBuilder(cc.getChatId(), text)
            .replyMarkup(menuKeyboardBuilder.getMenu(reader.getLang()).build())
            .enableHtml()
            .build();
    }

    @Override
    public String trigger() {
        return "/start";
    }

    private ReaderDto checkRegistered(ChatCommand cc) {
        ReaderDto r = readerService.find(cc.getChatId());
        if (r != null) {
            return r;
        }
        ReaderRequest readerRequest = buildReaderRequest(cc);
        return readerService.register(readerRequest);
    }

    private ReaderRequest buildReaderRequest(ChatCommand msg) {
        User from = msg.getFrom();
        ReaderRequest rr = new ReaderRequest();
        rr.setChatId(msg.getChatId());
        rr.setUsername(from.getUserName());
        rr.setFirstName(from.getFirstName());
        rr.setLastName(from.getLastName());
        rr.setPrimaryLanguage(from.getLanguageCode());
        rr.setPlatform("TELEGRAM");
        return rr;
    }
}
