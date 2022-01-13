package bepicky.bot.client.message.handler.pick;

import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.message.template.TemplateNewsNote;
import bepicky.bot.client.service.ITagService;
import bepicky.bot.client.service.IValueNormalisationService;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.message.handler.EntityCallbackMessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import com.google.common.collect.ImmutableMap;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TagSubscribeMessageHandler implements EntityCallbackMessageHandler {

    private final ITagService tagService;
    private final MessageTemplateContext templateContext;
    private final IValueNormalisationService valueNormalisationService;

    public TagSubscribeMessageHandler(
        ITagService tagService,
        MessageTemplateContext templateContext,
        IValueNormalisationService valueNormalisationService
    ) {
        this.tagService = tagService;
        this.templateContext = templateContext;
        this.valueNormalisationService = valueNormalisationService;
    }

    @Override
    public HandleResult handle(CallbackCommand command) {
        var key = (String) command.getId();
        var tag = tagService.subscribe(command.getChatId(), key);
        if (tag.isError()) {
            if (tag.getReader() == null) {
                var errorText = templateContext.errorTemplate(LangUtils.DEFAULT);
                return new HandleResult(errorText, null);
            }
            if (tag.getError().getCode() == 403) {
                var errorText =
                    templateContext.processTemplate(TemplateNames.STATUS_TAG_LIMIT, tag.getReader().getLang());
                return new HandleResult(errorText, null);
            }
        }

        var templateDtos = tag.getNews()
            .stream()
            .map(dto -> {
                var normalisedDate = valueNormalisationService.normaliseDate(
                    dto.getDate(),
                    tag.getReader().getLang()
                );
                return new TemplateNewsNote(dto, normalisedDate);
            })
            .collect(Collectors.toList());

        var txt = templateContext.processTemplate(
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
