package bepicky.bot.client.message.handler.list.common;

import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.list.AbstractCategoryListMessageHandler;
import bepicky.common.domain.response.CategoryListResponse;
import org.springframework.stereotype.Component;

import static bepicky.bot.client.message.template.TemplateNames.LIST_CATEGORIES;

@Component
public class CommonCategoryListMessageHandler extends AbstractCategoryListMessageHandler {

    @Override
    protected CategoryListResponse getCategories(long chatId, int page) {
        return categoryService.list(chatId, "COMMON", page, PAGE_SIZE);
    }

    @Override
    protected String msgTextKey() {
        return LIST_CATEGORIES;
    }

    @Override
    public CommandType commandType() {
        return CommandType.LIST;
    }

    @Override
    public EntityType entityType() {
        return EntityType.CATEGORY;
    }
}
