package bepicky.bot.client.message.handler.common;

import bepicky.bot.client.service.ICategoryService;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.message.handler.MessageHandler;
import bepicky.common.domain.response.CategoryListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class AllCategoriesMessageHandler implements MessageHandler {

    @Autowired
    private ICategoryService categoryService;

    @Override
    public BotApiMethod<Message> handle(ChatCommand message) {
        Long chatId = message.getChatId();
        CategoryListResponse common = categoryService.list(chatId, "COMMON");
        return null;
    }

    @Override
    public String trigger() {
        return "/category:list";
    }
}
