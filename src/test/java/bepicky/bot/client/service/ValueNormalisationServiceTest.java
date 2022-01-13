package bepicky.bot.client.service;

import bepicky.bot.core.message.LangUtils;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ValueNormalisationServiceTest {

    private IValueNormalisationService valueNormalisationService = new ValueNormalisationService();

    static Stream<Arguments> provideValidEnDates() {

        return Stream.of(
            Arguments.of(new Date(100500), "01 Jan 1970"),
            Arguments.of(new Date(1642075786169L), "13 Jan 2022"),
            Arguments.of(new Date(1639137600000L), "10 Dec 2021"),
            Arguments.of(new Date(1636545600000L), "10 Nov 2021"),
            Arguments.of(new Date(1633863600000L), "10 Oct 2021"),
            Arguments.of(new Date(1631271600000L), "10 Sep 2021"),
            Arguments.of(new Date(1628593200000L), "10 Aug 2021"),
            Arguments.of(new Date(1625914800000L), "10 Jul 2021"),
            Arguments.of(new Date(1623322800000L), "10 Jun 2021"),
            Arguments.of(new Date(1620644400000L), "10 May 2021"),
            Arguments.of(new Date(1618052400000L), "10 Apr 2021"),
            Arguments.of(new Date(1615377600000L), "10 Mar 2021"),
            Arguments.of(new Date(1612958400000L), "10 Feb 2021")
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidEnDates")
    public void normaliseDate_ShouldNormaliseEnDateCorrectly(Date date, String normalisedDate) {
        String actualDate = valueNormalisationService.normaliseDate(date, LangUtils.ENGLISH);

        assertEquals(normalisedDate, actualDate);
    }

    static Stream<Arguments> provideValidRuDates() {
        return Stream.of(
            Arguments.of(new Date(100500), "01 янв. 1970"),
            Arguments.of(new Date(1642075786169L), "13 янв. 2022"),
            Arguments.of(new Date(1639137600000L), "10 дек. 2021"),
            Arguments.of(new Date(1636545600000L), "10 нояб. 2021"),
            Arguments.of(new Date(1633863600000L), "10 окт. 2021"),
            Arguments.of(new Date(1631271600000L), "10 сент. 2021"),
            Arguments.of(new Date(1628593200000L), "10 авг. 2021"),
            Arguments.of(new Date(1625914800000L), "10 июл. 2021"),
            Arguments.of(new Date(1623322800000L), "10 июн. 2021"),
            Arguments.of(new Date(1620644400000L), "10 мая 2021"),
            Arguments.of(new Date(1618052400000L), "10 апр. 2021"),
            Arguments.of(new Date(1615377600000L), "10 мар. 2021"),
            Arguments.of(new Date(1612958400000L), "10 февр. 2021")
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidRuDates")
    public void normaliseDate_ShouldNormaliseRuDateCorrectly(Date date, String normalisedDate) {
        String actualDate = valueNormalisationService.normaliseDate(date, LangUtils.RUSSIAN);

        assertEquals(normalisedDate, actualDate);
    }

    static Stream<Arguments> provideValidUkrDates() {
        return Stream.of(
            Arguments.of(new Date(100500), "01 січ. 1970"),
            Arguments.of(new Date(1642075786169L), "13 січ. 2022"),
            Arguments.of(new Date(1639137600000L), "10 груд. 2021"),
            Arguments.of(new Date(1636545600000L), "10 лист. 2021"),
            Arguments.of(new Date(1633863600000L), "10 жовт. 2021"),
            Arguments.of(new Date(1631271600000L), "10 вер. 2021"),
            Arguments.of(new Date(1628593200000L), "10 серп. 2021"),
            Arguments.of(new Date(1625914800000L), "10 лип. 2021"),
            Arguments.of(new Date(1623322800000L), "10 черв. 2021"),
            Arguments.of(new Date(1620644400000L), "10 трав. 2021"),
            Arguments.of(new Date(1618052400000L), "10 квіт. 2021"),
            Arguments.of(new Date(1615377600000L), "10 бер. 2021"),
            Arguments.of(new Date(1612958400000L), "10 лют. 2021")
        );
    }

    @ParameterizedTest
    @MethodSource("provideValidUkrDates")
    public void normaliseDate_ShouldNormaliseUkrDateCorrectly(Date date, String normalisedDate) {
        String actualDate = valueNormalisationService.normaliseDate(date, LangUtils.UKRAINIAN);

        assertEquals(normalisedDate, actualDate);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "invalid", "null"})
    public void normaliseDate_InvalidLang_ShouldUseDefaultLangForTranslation(String lang) {
        Date date = new Date(100500);

        String actualDate = valueNormalisationService.normaliseDate(date, "null".equals(lang) ? null : lang);

        assertEquals("01 Jan 1970", actualDate);
    }

}