package bepicky.bot.client.message.handler;

import bepicky.bot.client.message.handler.common.HelpMessageHandler;
import bepicky.bot.client.message.handler.common.SettingsCommonMessageHandler;
import bepicky.bot.client.message.handler.common.tag.TagsMessageHandler;
import bepicky.bot.client.message.handler.util.SettingsMessageHandler;
import bepicky.bot.client.message.template.ButtonNames;
import bepicky.bot.core.cmd.CommandTranslator;
import bepicky.bot.core.message.LangUtils;
import bepicky.bot.core.message.template.MessageTemplateContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class MessageToCommandTranslator implements CommandTranslator {

    private final Map<String, String> msg2cmd = new HashMap<>();

    @Autowired
    private MessageTemplateContext t;

    @PostConstruct
    public void initContainer() {
        LangUtils.SUPPORTED.forEach(l -> {
                msg2cmd.put(t.processTemplate(ButtonNames.HELP, l), HelpMessageHandler.HELP);
                msg2cmd.put(
                    t.processTemplate(ButtonNames.TAGS, l),
                    TagsMessageHandler.TAGS
                );
            msg2cmd.put(
                t.processTemplate(ButtonNames.SETTINGS, l),
                SettingsCommonMessageHandler.SETTINGS
            );
            }
        );
    }

    @Override
    public String translate(String text) {
        return msg2cmd.get(text);
    }
}
