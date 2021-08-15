package bepicky.bot.client.message.handler.common.tag;

import bepicky.bot.client.message.handler.pick.TagSubscribeMessageHandler;
import bepicky.bot.client.message.handler.rm.TagUnsubscribeMessageHandler;
import bepicky.bot.client.service.IReaderService;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.message.builder.TgMessageBuilder;
import bepicky.bot.core.message.handler.CallbackMessageHandler;
import bepicky.bot.core.message.handler.MessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.common.domain.dto.ReaderDto;
import bepicky.common.domain.dto.StatusReaderDto;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static bepicky.bot.client.message.template.TemplateNames.PICK_TAG_INSTRUCTION;

@Component
public class TagMessageHandler implements MessageHandler {

    @Autowired
    private IReaderService readerService;

    @Autowired
    private MessageTemplateContext templateContext;

    @Autowired
    private TagSubscribeMessageHandler tagSubscribeMessageHandler;

    @Autowired
    private TagUnsubscribeMessageHandler tagUnsubscribeMessageHandler;

    private Map<CommandType, Function<CallbackCommand, CallbackMessageHandler.HandleResult>> cmdResultMap;

    @PostConstruct
    private void initCmdMapping() {
        cmdResultMap = ImmutableMap.of(
            CommandType.PICK, tagSubscribeMessageHandler::handle,
            CommandType.REMOVE, tagUnsubscribeMessageHandler::handle
        );
    }

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        Optional<String> key = validateKey(cc.getKey());
        if (key.isEmpty()) {
            ReaderDto reader = readerService.find(cc.getChatId());
            return TgMessageBuilder.msg(cc.getChatId(), templateContext.processTemplate(PICK_TAG_INSTRUCTION, reader.getLang()));
        }
        StatusReaderDto status = readerService.getStatus(cc.getChatId());
        String keyVal = key.get();
        CommandType cmd = status.getTags().contains(keyVal) ?
            CommandType.REMOVE :
            CommandType.PICK;

        CallbackCommand clc = CallbackCommand.of(cmd, EntityType.TAG, 1, keyVal);
        clc.setChatId(cc.getChatId());

        CallbackMessageHandler.HandleResult result = cmdResultMap.get(cmd).apply(clc);

        return TgMessageBuilder.noWebPreviewMsg(cc.getChatId(), result.getText(), result.getInline());
    }

    @Override
    public String trigger() {
        return "#";
    }
}
