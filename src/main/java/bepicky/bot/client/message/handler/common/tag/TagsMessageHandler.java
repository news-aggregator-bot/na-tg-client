package bepicky.bot.client.message.handler.common.tag;

import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.ITagService;
import bepicky.bot.core.cmd.ChatCommand;
import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.message.builder.SendMessageBuilder;
import bepicky.bot.core.message.handler.MessageHandler;
import bepicky.bot.core.message.template.MessageTemplateContext;
import bepicky.common.domain.dto.TagDto;
import bepicky.common.domain.response.TagListResponse;
import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.stream.Collectors;


@Component
public class TagsMessageHandler implements MessageHandler {

    public static final String TAGS = "/tags";

    @Autowired
    private ITagService tagService;

    @Autowired
    private MessageTemplateContext templateContext;

    @Override
    public BotApiMethod<Message> handle(ChatCommand cc) {
        TagListResponse r = tagService.getAllBy(cc.getChatId());
        if (r.isError()) {
            String errorText = templateContext.errorTemplate(LangUtils.DEFAULT);
            return new SendMessageBuilder(cc.getChatId(), errorText).build();
        }

        String text = templateContext.processTemplate(
            TemplateNames.STATUS_TAG, r.getReader().getLang(),
            ImmutableMap.<String, Object>builder()
                .put("total", r.getList().size())
                .put("tags", r.getList().stream().map(TagDto::getValue).collect(Collectors.toList()))
                .build()
        );

        return new SendMessageBuilder(cc.getChatId(), text).build();
    }

    @Override
    public String trigger() {
        return TAGS;
    }
}
