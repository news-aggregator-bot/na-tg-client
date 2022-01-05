package bepicky.bot.client;

import bepicky.common.domain.dto.NewsNoteDto;
import bepicky.common.domain.dto.SourcePageDto;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestEntitiesGenerator {

    public static List<NewsNoteDto> generateArticles(int amount, List<SourcePageDto> pages) {
        return IntStream.range(0, amount)
            .mapToObj(i -> TestEntities.newsArticle((long) i, pages))
            .collect(Collectors.toList());
    }
}
