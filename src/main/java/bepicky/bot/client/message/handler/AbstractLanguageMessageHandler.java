package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.finisher.NatsMessageFinisher;
import bepicky.bot.client.message.nats.StartFinishMessagePublisher;
import bepicky.bot.core.BotRouter;
import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.cmd.CommandManager;
import bepicky.bot.core.message.handler.EntityCallbackMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.ILanguageService;
import bepicky.bot.core.message.template.TemplateUtils;
import bepicky.common.data.DataTransformer;
import bepicky.common.domain.response.LanguageResponse;
import bepicky.common.msg.LanguageCommandMsg;
import bepicky.common.msg.MsgCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractLanguageMessageHandler implements EntityCallbackMessageHandler,
    NatsMessageFinisher {

    @Autowired
    protected MessageTemplateContext templateContext;

    @Autowired
    private StartFinishMessagePublisher startFinishMessagePublisher;

    @Autowired
    private DataTransformer<byte[]> byteTransformer;

    @Autowired
    private BotRouter bot;

    @Value("${topics.lang.cmd.start}")
    private String startSubject;

    @Value("${topics.lang.cmd.finish}")
    private String finishSubject;

    @Override
    public HandleResult handle(CallbackCommand cc) {
        String lang = (String) cc.getId();
        LanguageCommandMsg cmd = new LanguageCommandMsg(
            cc.getChatId(),
            lang,
            MsgCommand.valueOf(commandType().name())
        );
        startFinishMessagePublisher.publish(startSubject, finishSubject, cmd);
        return null;
    }

    @Override
    public void finish(byte[] msg) {
        LanguageResponse response = byteTransformer.transform(msg, LanguageResponse.class);
        if (response.isError()) {
            String errorText = templateContext.errorTemplate(LangUtils.DEFAULT);
            // handling
            bot.sendNew(response.getReader().getChatId(), errorText);
            return;
        }
        String text = templateContext.processTemplate(
            TemplateNames.getTemplate(commandType(), entityType()),
            response.getReader().getLang(),
            TemplateUtils.name(response.getLanguage().getLocalized())
        );
        bot.sendNew(response.getReader().getChatId(), text);
    }

    @Override
    public String subject() {
        return finishSubject + "." + entityType() + "." + commandType().name();
    }

    @Override
    public EntityType entityType() {
        return EntityType.LANGUAGE;
    }
}
