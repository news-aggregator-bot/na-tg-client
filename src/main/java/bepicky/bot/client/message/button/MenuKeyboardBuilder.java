package bepicky.bot.client.message.button;

import bepicky.bot.client.message.template.ButtonNames;
import bepicky.bot.core.message.template.MessageTemplateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MenuKeyboardBuilder {

    @Autowired
    private MessageTemplateContext templateContext;

    public ReplyMarkupBuilder getMenu(String lang) {
        ReplyMarkupBuilder replyMarkup = new ReplyMarkupBuilder();
        String helpText = templateContext.processTemplate(ButtonNames.HELP, lang);
        String tags = templateContext.processTemplate(ButtonNames.TAGS, lang);
        String settings = templateContext.processTemplate(ButtonNames.SETTINGS, lang);
        replyMarkup.addButtons(Arrays.asList(tags, settings));
        replyMarkup.addButtons(Arrays.asList(helpText));
        return replyMarkup;
    }
}
