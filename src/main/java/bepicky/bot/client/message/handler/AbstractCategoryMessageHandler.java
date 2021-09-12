package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.finisher.NatsMessageFinisher;
import bepicky.bot.client.message.nats.StartFinishMessagePublisher;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.core.BotRouter;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.message.handler.EntityCallbackMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.core.message.template.TemplateUtils;
import bepicky.common.data.DataTransformer;
import bepicky.common.domain.response.CategoryResponse;
import bepicky.common.msg.CategoryCommandMsg;
import bepicky.common.msg.MsgCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractCategoryMessageHandler implements EntityCallbackMessageHandler,
    NatsMessageFinisher {

    @Autowired
    protected MessageTemplateContext templateContext;

    @Autowired
    private StartFinishMessagePublisher startFinishMessagePublisher;

    @Autowired
    private DataTransformer<byte[]> byteTransformer;

    @Autowired
    private BotRouter bot;

    @Value("${topics.category.cmd.start}")
    private String startSubject;

    @Value("${topics.category.cmd.finish}")
    private String finishSubject;

    @Override
    public HandleResult handle(CallbackCommand cc) {
        long categoryId = (long) cc.getId();
        CategoryCommandMsg cmd = new CategoryCommandMsg(
            cc.getChatId(),
            categoryId,
            MsgCommand.valueOf(commandType().name())
        );
        startFinishMessagePublisher.publish(startSubject, subject(), cmd);
        return null;
    }

    @Override
    public void finish(byte[] msg) {
        CategoryResponse response = byteTransformer.transform(msg, CategoryResponse.class);
        if (response.isError()) {
            String errorText = templateContext.errorTemplate(LangUtils.DEFAULT);
            bot.sendNew(response.getReader().getChatId(), errorText);
        }
        String txtmsg = templateContext.processTemplate(
            TemplateNames.getTemplate(commandType(), entityType()),
            response.getReader().getLang(),
            TemplateUtils.name(response.getCategory().getLocalised())
        );
        bot.sendNew(response.getReader().getChatId(), txtmsg);
    }

    @Override
    public String subject() {
        return finishSubject + "." + entityType() + "." + commandType().name();
    }
}
