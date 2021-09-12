package bepicky.bot.client.message.handler.rm;

import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.AbstractCategoryMessageHandler;
import org.springframework.stereotype.Component;

@Component
public class CategoryRemoveAllMessageHandler extends AbstractCategoryMessageHandler {

    @Override
    public CommandType commandType() {
        return CommandType.REMOVE_ALL;
    }

    @Override
    public EntityType entityType() {
        return EntityType.CATEGORY;
    }
}
