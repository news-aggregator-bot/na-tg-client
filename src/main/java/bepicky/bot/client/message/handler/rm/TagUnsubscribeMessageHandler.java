package bepicky.bot.client.message.handler.rm;

import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.ITagService;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.message.handler.EntityCallbackMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.core.message.template.TemplateUtils;
import bepicky.common.domain.response.TagResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TagUnsubscribeMessageHandler implements EntityCallbackMessageHandler {

    @Autowired
    private ITagService tagService;

    @Autowired
    protected MessageTemplateContext templateContext;

    @Override
    public HandleResult handle(CallbackCommand command) {
        String key = (String) command.getId();
        TagResponse tag = tagService.unsubscribe(command.getChatId(), key);
        if (tag.isError()) {
            String errorText = templateContext.errorTemplate(LangUtils.DEFAULT);
            return new HandleResult(errorText, null);
        }
        String txt = templateContext.processTemplate(
            TemplateNames.RM_TAG_SUCCESS,
            tag.getReader().getLang(),
            TemplateUtils.name(tag.getTag().getValue())
        );

        return new HandleResult(txt, new InlineMarkupBuilder().build());
    }

    @Override
    public CommandType commandType() {
        return CommandType.REMOVE;
    }

    @Override
    public EntityType entityType() {
        return EntityType.TAG;
    }
}
