package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.async.NatsMessageFinisher;
import bepicky.bot.client.message.async.NatsMessageStarter;
import bepicky.bot.client.message.nats.StartFinishMessagePublisher;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.core.BotRouter;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.message.handler.EntityCallbackMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.core.message.template.TemplateUtils;
import bepicky.common.domain.response.AbstractResponse;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractCmdMessageHandler implements EntityCallbackMessageHandler,
    NatsMessageStarter, NatsMessageFinisher {

    @Autowired
    protected MessageTemplateContext templateContext;

    @Autowired
    protected BotRouter bot;

    @Autowired
    protected StartFinishMessagePublisher startFinishMessagePublisher;

    @Override
    public HandleResult handle(CallbackCommand cc) {
        start(cc);
        return null;
    }

    protected void answer(AbstractResponse response, String name) {
        String text = response.isError() ? templateContext.errorTemplate(LangUtils.DEFAULT)
            : templateContext.processTemplate(
            TemplateNames.getTemplate(commandType(), entityType()),
            response.getReader().getLang(),
            TemplateUtils.name(name)
        );
        bot.sendNew(response.getReader().getChatId(), text);
    }
}
