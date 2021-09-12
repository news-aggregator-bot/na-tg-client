package bepicky.bot.client.message.handler.rm;

import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.AbstractCategoryMessageHandler;
import org.springframework.stereotype.Component;

@Component
public class CategoryRemoveMessageHandler extends AbstractCategoryMessageHandler {

    @Override
    public CommandType commandType() {
        return CommandType.REMOVE;
    }

    @Override
    public EntityType entityType() {
        return EntityType.CATEGORY;
    }
}
