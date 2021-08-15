package bepicky.bot.client.message.handler.list.region;

import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.cmd.CommandType;
import bepicky.bot.client.message.handler.list.AbstractSubCategoryListMessageHandler;
import bepicky.bot.core.message.template.TemplateUtils;
import bepicky.common.domain.dto.CategoryDto;
import bepicky.common.domain.response.CategoryListResponse;
import org.springframework.stereotype.Component;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.Map;

import static bepicky.bot.client.message.template.TemplateNames.LIST_SUBREGIONS;

@Component
public class RegionSubCategoryListMessageHandler extends AbstractSubCategoryListMessageHandler {

    @Override
    protected CategoryListResponse getSubCategories(long chatId, long parentId, int page) {
        return categoryService.list(chatId, parentId, page, PAGE_SIZE);
    }

    @Override
    protected Tuple2<String, Map<String, Object>> msgTextData(
        CategoryDto parent, int page
    ) {
        return Tuples.of(LIST_SUBREGIONS, TemplateUtils.params("name", parent.getName(), "page", page));
    }

    @Override
    public CommandType commandType() {
        return CommandType.SUBLIST;
    }

    @Override
    public EntityType entityType() {
        return EntityType.REGION;
    }
}
