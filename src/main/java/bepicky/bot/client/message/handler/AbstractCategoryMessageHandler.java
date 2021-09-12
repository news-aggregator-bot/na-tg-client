package bepicky.bot.client.message.handler;

import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.common.data.DataTransformer;
import bepicky.common.domain.response.CategoryResponse;
import bepicky.common.msg.CategoryCommandMsg;
import bepicky.common.msg.MsgCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

public abstract class AbstractCategoryMessageHandler extends AbstractCmdMessageHandler {

    @Autowired
    private DataTransformer<byte[]> byteTransformer;

    @Value("${topics.category.cmd.start}")
    private String startSubject;

    @Value("${topics.category.cmd.finish}")
    private String finishSubject;

    @Override
    public void start(CallbackCommand cc) {
        long categoryId = (long) cc.getId();
        CategoryCommandMsg cmd = new CategoryCommandMsg(
            cc.getChatId(),
            categoryId,
            MsgCommand.valueOf(commandType().name())
        );
        startFinishMessagePublisher.publish(startSubject, finishSubject(), cmd);
    }

    @Override
    public void finish(byte[] msg) {
        CategoryResponse response = byteTransformer.transform(msg, CategoryResponse.class);
        answer(response, response.getCategory().getLocalised());
    }

    @Override
    public String finishSubject() {
        return finishSubject + "." + entityType() + "." + commandType().name();
    }

    @Override
    public String startSubject() {
        return startSubject;
    }
}
