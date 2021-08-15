package bepicky.bot.client.config;

import bepicky.bot.core.BotRouter;
import bepicky.bot.core.cmd.CommandTranslator;
import bepicky.bot.core.message.CallbackMessageHandlerManager;
import bepicky.bot.core.message.MessageHandlerManager;
import bepicky.bot.core.message.handler.EntityCallbackMessageHandler;
import bepicky.bot.core.message.handler.IDefaultMessageHandler;
import bepicky.bot.core.message.handler.MessageHandler;
import bepicky.bot.core.message.handler.UtilMessageHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
public class PickyBotConfig {

    @Bean
    public BotRouter aggregationBot() {
        return new BotRouter();
    }

    @PostConstruct
    public void initBots() throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

        try {
            botsApi.registerBot(aggregationBot());
        } catch (TelegramApiException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    @Bean
    public MessageHandlerManager messageHandlerManager(
        List<MessageHandler> handlers,
        IDefaultMessageHandler defaultMessageHandler,
        CommandTranslator commandTranslator
    ) {
        return new MessageHandlerManager(handlers, defaultMessageHandler, commandTranslator);
    }

    @Bean
    public CallbackMessageHandlerManager callbackMessageHandlerManager(
        List<EntityCallbackMessageHandler> callbackMessageHandlers,
        List<UtilMessageHandler> utilMessageHandlers
    ) {
        return new CallbackMessageHandlerManager(callbackMessageHandlers, utilMessageHandlers);
    }

}
