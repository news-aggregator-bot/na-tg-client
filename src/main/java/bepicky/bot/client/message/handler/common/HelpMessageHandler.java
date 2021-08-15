package bepicky.bot.client.message.handler.common;

import bepicky.bot.client.message.button.ReplyMarkupBuilder;
import bepicky.bot.client.message.template.ButtonNames;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.message.builder.TgMessageBuilder;
import bepicky.bot.core.message.handler.MessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Arrays;

@Component
public class HelpMessageHandler implements MessageHandler {

    public static final String HELP = "/help";

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private IReaderService readerService;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        ReaderDto reader = readerService.find(cc.getChatId());
        String text = templateContext.processEmojiTemplate(TemplateNames.HELP, reader.getLang());

        ReplyMarkupBuilder replyMarkup = new ReplyMarkupBuilder();
        String helpText = templateContext.processTemplate(ButtonNames.HELP, reader.getLang());
        String tags = templateContext.processTemplate(ButtonNames.TAGS, reader.getLang());
        String settings = templateContext.processTemplate(ButtonNames.SETTINGS, reader.getLang());
        replyMarkup.addButtons(Arrays.asList(tags, settings));
        replyMarkup.addButtons(Arrays.asList(helpText));

        return TgMessageBuilder.msg(cc.getChatId(), text, replyMarkup.build());
    }

    @Override
    public String trigger() {
        return HELP;
    }
}
