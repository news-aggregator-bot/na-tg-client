package bepicky.bot.client.message.handler.common;

import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.list.NewsSearchMessageHandler;
import bepicky.bot.core.message.builder.SendMessageBuilder;
import bepicky.bot.core.message.handler.CallbackMessageHandler;
import bepicky.bot.core.message.handler.IDefaultMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.service.IReaderService;
import bepicky.common.domain.dto.ReaderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Optional;

import static bepicky.bot.client.message.template.TemplateNames.SEARCH_NOTE_INSTRUCTION;

@Component
public class SearchNewsMessageHandler implements IDefaultMessageHandler {

    @Autowired
    private NewsSearchMessageHandler newsSearchMessageHandler;

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private IReaderService readerService;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        Optional<String> key = validateKey(cc.getKey());
        if (key.isEmpty()) {
            ReaderDto reader = readerService.find(cc.getChatId());
            return new SendMessageBuilder(
                cc.getChatId(),
                templateContext.processTemplate(SEARCH_NOTE_INSTRUCTION, reader.getLang())
            )
                .enableHtml()
                .build();
        }
        CallbackCommand clc = CallbackCommand.of(
            CommandType.SEARCH,
            EntityType.NEWS_NOTE,
            1,
            key.get()
        );
        clc.setChatId(cc.getChatId());
        CallbackMessageHandler.HandleResult result = newsSearchMessageHandler.handle(clc);

        return new SendMessageBuilder(clc.getChatId(), result.getText())
            .disableWebPreview()
            .replyMarkup(result.getInline())
            .enableHtml()
            .build();
    }

    @Override
    public String trigger() {
        return "/sn";
    }
}
