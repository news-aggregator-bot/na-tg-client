package bepicky.bot.client.message.handler.common;

import bepicky.bot.client.message.button.MenuKeyboardBuilder;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.message.builder.SendMessageBuilder;
import bepicky.bot.core.message.handler.MessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class HelpMessageHandler implements MessageHandler {

    public static final String HELP = "/help";

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private MenuKeyboardBuilder menuKeyboardBuilder;

    @Autowired
    private IReaderService readerService;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        ReaderDto reader = readerService.find(cc.getChatId());
        String text = templateContext.processEmojiTemplate(TemplateNames.HELP, reader.getLang());
        return new SendMessageBuilder(cc.getChatId(), text)
            .replyMarkup(menuKeyboardBuilder.getMenu(reader.getLang()).build())
            .build();
    }

    @Override
    public String trigger() {
        return HELP;
    }
}
