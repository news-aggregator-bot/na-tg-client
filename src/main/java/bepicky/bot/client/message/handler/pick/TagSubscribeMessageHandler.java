package bepicky.bot.client.message.handler.pick;

import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.message.template.TemplateNewsNote;
import bepicky.bot.client.service.ITagService;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.message.handler.EntityCallbackMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.bot.core.message.template.TemplateUtils;
import bepicky.common.domain.response.TagResponse;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TagSubscribeMessageHandler implements EntityCallbackMessageHandler {

    @Autowired
    private ITagService tagService;

    @Autowired
    protected MessageTemplateContext templateContext;

    @Override
    public HandleResult handle(CallbackCommand command) {
        String key = (String) command.getId();
        TagResponse tag = tagService.subscribe(command.getChatId(), key);
        if (tag.isError()) {
            if (tag.getReader() == null) {
                String errorText = templateContext.errorTemplate(LangUtils.DEFAULT);
                return new HandleResult(errorText, null);
            }
            if (tag.getError().getCode() == 403) {
                String errorText = templateContext.processTemplate(TemplateNames.STATUS_TAG_LIMIT, tag.getReader().getLang());
                return new HandleResult(errorText, null);
            }
        }

        List<TemplateNewsNote> templateDtos = tag.getNews().stream()
            .map(TemplateNewsNote::new)
            .collect(Collectors.toList());

        String txt = templateContext.processTemplate(
            TemplateNames.PICK_TAG_SUCCESS,
            tag.getReader().getLang(),
            ImmutableMap.of(
                "name", tag.getTag().getValue(),
                "total", tag.getNews().size(),
                "notes", templateDtos
            )
        );

        return new HandleResult(txt, new InlineMarkupBuilder().build());
    }

    @Override
    public CommandType commandType() {
        return CommandType.PICK;
    }

    @Override
    public EntityType entityType() {
        return EntityType.TAG;
    }
}
