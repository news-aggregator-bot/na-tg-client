package bepicky.bot.client.message.handler.rm;

import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.AbstractSourceMessageHandler;
import org.springframework.stereotype.Component;

@Component
public class SourceRemoveMessageHandler extends AbstractSourceMessageHandler {

    @Override
    public CommandType commandType() {
        return CommandType.REMOVE;
    }
}
