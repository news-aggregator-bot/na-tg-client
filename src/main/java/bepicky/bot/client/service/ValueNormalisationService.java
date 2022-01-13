package bepicky.bot.client.service;

import bepicky.bot.core.message.LangUtils;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

@Service
public class ValueNormalisationService implements IValueNormalisationService {

    private final Map<String, SimpleDateFormat> langDateFormats;

    public ValueNormalisationService() {
        SimpleDateFormat en = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        SimpleDateFormat ru = new SimpleDateFormat("dd MMM yyyy", new Locale("ru"));
        SimpleDateFormat ukr = new SimpleDateFormat("dd MMM yyyy", new Locale("uk"));

        langDateFormats = ImmutableMap.of(
            LangUtils.ENGLISH, en,
            LangUtils.RUSSIAN, ru,
            LangUtils.UKRAINIAN, ukr
        );
    }

    @Override
    public String normalise(String value) {
        return StringUtils.isBlank(value) ? "" : value;
    }

    @Override
    public String normaliseDate(Date date, String lang) {
        SimpleDateFormat langDateFormat = langDateFormats.getOrDefault(
            lang,
            langDateFormats.get(LangUtils.DEFAULT)
        );
        return langDateFormat.format(date);
    }
}
