package bepicky.bot.client.message.handler;

import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.message.EntityType;
import bepicky.common.data.DataTransformer;
import bepicky.common.domain.response.SourceResponse;
import bepicky.common.msg.MsgCommand;
import bepicky.common.msg.SourceCommandMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import static bepicky.bot.core.message.EntityType.SOURCE;

public abstract class AbstractSourceMessageHandler extends AbstractCmdMessageHandler {

    @Autowired
    private DataTransformer<byte[]> byteTransformer;

    @Value("${topics.src.cmd.start}")
    private String startSubject;

    @Value("${topics.src.cmd.finish}")
    private String finishSubject;

    @Override
    public void start(CallbackCommand cc) {
        long srcId = (long) cc.getId();
        SourceCommandMsg cmd = new SourceCommandMsg(
            cc.getChatId(),
            srcId,
            MsgCommand.valueOf(commandType().name())
        );
        startFinishMessagePublisher.publish(startSubject, finishSubject(), cmd);
    }

    @Override
    public void finish(byte[] msg) {
        SourceResponse response = byteTransformer.transform(msg, SourceResponse.class);
        answer(response, response.getSource().getName());
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
        return SOURCE;
    }
}
