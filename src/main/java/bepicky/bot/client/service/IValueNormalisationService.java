package bepicky.bot.client.service;

import java.util.Date;

public interface IValueNormalisationService {

    String normalise(String value);

    String normaliseDate(Date date, String lang);
}
