package bepicky.bot.client.message.handler.list;

import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.message.LangUtils;
import bepicky.bot.client.message.button.InlineMarkupBuilder;
import bepicky.bot.core.cmd.CallbackCommand;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.context.ChatChainManager;
import bepicky.bot.client.message.template.ButtonNames;
import bepicky.bot.client.message.template.TemplateNames;
import bepicky.bot.client.service.ISourceService;
import bepicky.bot.core.message.template.TemplateUtils;
import bepicky.common.domain.dto.SourceDto;
import bepicky.common.domain.response.SourceListResponse;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.vdurmont.emoji.EmojiParser.parseToUnicode;

@Component
public class SourceListMessageHandler extends AbstractListMessageHandler {

    @Autowired
    private ISourceService sourceService;

    @Override
    public HandleResult handle(CallbackCommand cc) {
        int page = cc.getPage();
        SourceListResponse response = sourceService.list(cc.getChatId(), page, PAGE_SIZE);
        if (response.isError()) {
            return error(response.getError().getEntity());
        }
        List<SourceDto> sources = response.getList();

        InlineMarkupBuilder markup = new InlineMarkupBuilder();
        List<InlineMarkupBuilder.InlineButton> buttons = sources.stream()
            .map(s -> buildButton(s, page))
            .collect(Collectors.toList());

        List<InlineMarkupBuilder.InlineButton> pagination = pagination(page, response, markup);
        List<InlineMarkupBuilder.InlineButton> navigation = navigation(markup, response.getReader());
        List<List<InlineMarkupBuilder.InlineButton>> partition = Lists.partition(buttons, 2);
        partition.forEach(markup::addButtons);
        markup.addButtons(pagination);
        markup.addButtons(navigation);

        String listSourcesText = parseToUnicode(templateContext.processTemplate(
            TemplateNames.LIST_SOURCES,
            response.getReader().getLang(),
            TemplateUtils.page(page)
        ));
        return new HandleResult(listSourcesText, markup.build());
    }

    private InlineMarkupBuilder.InlineButton buildButton(SourceDto s, int page) {
        String textKey = s.isPicked() ? ButtonNames.REMOVE : ButtonNames.PICK;
        String command = s.isPicked() ?
            cmdMngr.remove(entityType(), s.getId()) :
            cmdMngr.pick(entityType(), s.getId());
        String list = cmdMngr.list(entityType(), page);
        return new InlineMarkupBuilder.InlineButton(buildText(s, textKey), Arrays.asList(command, list));
    }

    private String buildText(SourceDto s, String textKey) {
        return parseToUnicode(templateContext.processTemplate(
            textKey,
            LangUtils.DEFAULT,
            TemplateUtils.name(s.getName())
        ));
    }

    @Override
    public CommandType commandType() {
        return CommandType.LIST;
    }

    @Override
    public EntityType entityType() {
        return EntityType.SOURCE;
    }
}
