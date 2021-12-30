package bepicky.bot.client.message.handler.list;

import bepicky.bot.client.config.TemplateConfig;
import bepicky.bot.core.cmd.CommandManager;
import bepicky.bot.core.message.template.MessageTemplateContext;
import org.mockito.Spy;

public class ListHandlerTestSupport {

    protected TemplateConfig templateConfig = new TemplateConfig();

    @Spy
    protected MessageTemplateContext templateContext =
        new MessageTemplateContext(templateConfig.templateConfiguration());

    @Spy
    protected CommandManager commandManager = new CommandManager();
}
