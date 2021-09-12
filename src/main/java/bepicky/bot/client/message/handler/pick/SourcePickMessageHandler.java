package bepicky.bot.client.message.handler.pick;

import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.AbstractSourceMessageHandler;
import org.springframework.stereotype.Component;

@Component
public class SourcePickMessageHandler extends AbstractSourceMessageHandler {

    @Override
    public CommandType commandType() {
        return CommandType.PICK;
    }
}
