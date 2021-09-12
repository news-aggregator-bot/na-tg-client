package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.nats.StartFinishMessagePublisher;
import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.common.data.DataTransformer;
import bepicky.common.domain.response.LanguageResponse;
import bepicky.common.msg.LanguageCommandMsg;
import bepicky.common.msg.MsgCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractLanguageMessageHandler extends AbstractCmdMessageHandler {

    @Autowired
    private DataTransformer<byte[]> byteTransformer;

    @Value("${topics.lang.cmd.start}")
    private String startSubject;

    @Value("${topics.lang.cmd.finish}")
    private String finishSubject;

    @Override
    public void start(CallbackCommand cc) {
        String lang = (String) cc.getId();
        LanguageCommandMsg cmd = new LanguageCommandMsg(
            cc.getChatId(),
            lang,
            MsgCommand.valueOf(commandType().name())
        );
        startFinishMessagePublisher.publish(startSubject(), finishSubject(), cmd);
    }

    @Override
    public void finish(byte[] msg) {
        LanguageResponse response = byteTransformer.transform(msg, LanguageResponse.class);
        answer(response, response.getLanguage().getLocalized());
    }

    @Override
    public String finishSubject() {
        return finishSubject + "." + entityType() + "." + commandType().name();
    }

    @Override
    public String startSubject() {
        return startSubject;
    }

    @Override
    public EntityType entityType() {
        return EntityType.LANGUAGE;
    }
}
