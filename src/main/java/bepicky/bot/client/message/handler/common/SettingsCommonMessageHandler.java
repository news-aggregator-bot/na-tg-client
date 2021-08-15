package bepicky.bot.client.message.handler.common;

import bepicky.bot.client.message.handler.util.SettingsMessageHandler;
import bepicky.bot.client.message.handler.util.StatusMessageHandler;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.core.message.builder.TgMessageBuilder;
import bepicky.bot.core.message.handler.CallbackMessageHandler;
import bepicky.bot.core.message.handler.MessageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class SettingsCommonMessageHandler implements MessageHandler {

    public static final String SETTINGS = "/settings";

    @Autowired
    private SettingsMessageHandler settingsMessageHandler;

    @Autowired
    private StatusMessageHandler statusMessageHandler;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        CallbackCommand callbackCmd = CallbackCommand.of(CommandType.SETTINGS);
        callbackCmd.setChatId(cc.getChatId());
        CallbackMessageHandler.HandleResult status = statusMessageHandler.handle(callbackCmd);
        CallbackMessageHandler.HandleResult settings = settingsMessageHandler.handle(callbackCmd);

        return TgMessageBuilder.msg(cc.getChatId(), status.getText() + settings.getText(), settings.getInline());
    }

    @Override
    public String trigger() {
        return SETTINGS;
    }

}
