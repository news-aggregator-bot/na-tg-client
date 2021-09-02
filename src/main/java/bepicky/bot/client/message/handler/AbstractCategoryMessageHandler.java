package bepicky.bot.client.message.handler;

import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.cmd.CommandManager;
import bepicky.bot.client.message.handler.context.ChatChainManager;
import bepicky.bot.core.message.handler.EntityCallbackMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.ICategoryService;
import bepicky.bot.core.message.template.TemplateUtils;
import bepicky.common.domain.response.CategoryResponse;
import bepicky.common.msg.CategoryCommandMsg;
import io.nats.client.Connection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;

public abstract class AbstractCategoryMessageHandler implements EntityCallbackMessageHandler {

    @Autowired
    protected CommandManager cmdMngr;

    @Autowired
    protected MessageTemplateContext templateContext;

    @Autowired
    protected ChatChainManager chainManager;

    @Autowired
    private Connection connection;

    @Value("${topics.category.cmd.starter}")
    private String starterSubject;

    @Value("${topics.category.cmd.finisher}")
    private String finisherSubject;

    @Override
    public HandleResult handle(CallbackCommand cc) {
        long categoryId = (long) cc.getId();
        CategoryResponse response = handle(cc.getChatId(), categoryId);
        if (response.isError()) {
            String errorText = templateContext.errorTemplate(LangUtils.DEFAULT);
            return new HandleResult(errorText, null);
        }
        String msg = templateContext.processTemplate(
            TemplateNames.getTemplate(commandType(), entityType()),
            response.getReader().getLang(),
            TemplateUtils.name(response.getCategory().getLocalised())
        );
        return new HandleResult(msg, null);
    }

    protected abstract CategoryResponse handle(Long chatId, Long categoryId);

    public void start(CallbackCommand cc) {
        new CategoryCommandMsg()
        connection.publish(starterSubject(), finisherSubject(), "".getBytes(StandardCharsets.UTF_8));
    }

    public void finish(CategoryResponse response) {
        if (response.isError()) {
            String errorText = templateContext.errorTemplate(LangUtils.DEFAULT);
            return new HandleResult(errorText, null);
        }
        String msg = templateContext.processTemplate(
            TemplateNames.getTemplate(commandType(), entityType()),
            response.getReader().getLang(),
            TemplateUtils.name(response.getCategory().getLocalised())
        );
        return new HandleResult(msg, null);
    }

    public String starterSubject() {
        return starterSubject;
    }

    public String finisherSubject() {
        return finisherSubject;
    }
}
