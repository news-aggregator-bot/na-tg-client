package bepicky.bot.client.message.template;

import bepicky.bot.core.message.EntityType;
import bepicky.bot.core.cmd.CommandType;
import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class TemplateNames {


    private TemplateNames() {}

    public static final String HELP = "help";
    public static final String SETTINGS = "settings";
    public static final String WELCOME = "welcome";

    private static final String PICK = "pick";
    private static final String REMOVE = "rm";
    private static final String STATUS = "status";

    private static final String TAG = "/tag";
    private static final String READER = "/reader";
    private static final String LANG = "/lang";
    private static final String SUCCESS = "/success";
    private static final String INSTRUCTION = "/instruction";

    public static final String PICK_REGION_SUCCESS = "pick_region_success";
    public static final String PICK_CATEGORY_SUCCESS = "pick_category_success";
    public static final String PICK_LANGUAGE_SUCCESS = "pick_language_success";
    public static final String PICK_SOURCE_SUCCESS = "pick_source_success";
    public static final String PICK_TAG_SUCCESS = PICK + TAG + SUCCESS;
    public static final String PICK_TAG_INSTRUCTION = PICK + TAG + INSTRUCTION;
    public static final String PICK_READER_LANG = PICK + READER + LANG;
    public static final String PICK_ALL_SUBCATEGORIES = "pick_all_subcategories";

    public static final String REMOVE_REGION_SUCCESS = "rm_region_success";
    public static final String REMOVE_CATEGORY_SUCCESS = "rm_category_success";
    public static final String REMOVE_LANGUAGE_SUCCESS = "rm_language_success";
    public static final String REMOVE_SOURCE_SUCCESS = "rm_source_success";
    public static final String REMOVE_ALL_SUBCATEGORIES = "rm_all_subcategories";

    public static final String RM_TAG_INSTRUCTION = REMOVE + TAG + INSTRUCTION;
    public static final String RM_TAG_SUCCESS = REMOVE + TAG + SUCCESS;

    public static final String LIST_CATEGORIES = "list_categories";
    public static final String LIST_REGIONS = "list_regions";
    public static final String LIST_SUBCATEGORIES = "list_subcategories";
    public static final String LIST_SUBREGIONS = "list_subregions";
    public static final String LIST_LANGUAGES = "list_languages";
    public static final String LIST_SUBCATEGORY = "list_subcategory";
    public static final String LIST_SOURCES = "list_sources";

    private static final String NOTIFICATION = "notification/";
    private static final String NEWS_NOTE = NOTIFICATION + "news_note/";
    public static final String N_NN_C = NEWS_NOTE + "category";
    public static final String N_NN_T = NEWS_NOTE + "tag";

    public static final String ENABLE = "enable";
    public static final String ENABLE_READER = "enable_reader";
    public static final String PAUSE_READER = "pause_reader";

    public static final String STATUS_READER = STATUS + READER;
    public static final String STATUS_TAG = STATUS + TAG;
    public static final String STATUS_TAG_LIMIT = STATUS + TAG + "/limit";

    private static final String SEARCH = "search";
    public static final String SEARCH_NOTE = SEARCH + "/news_note";
    public static final String SEARCH_TAG = SEARCH + "/tag";
    public static final String SEARCH_NOTE_INSTRUCTION = SEARCH_NOTE + INSTRUCTION;
    public static final String SEARCH_TAG_INSTRUCTION = SEARCH_TAG + INSTRUCTION;

    private static final String FAILURE = "failure";
    private static final String SUBSCRIPTION = "/subscription";

    public static final Map<EntityType, String> RM_ENTITY_CONTAINER = Map.of(
        EntityType.REGION,
        REMOVE_REGION_SUCCESS,
        EntityType.CATEGORY,
        REMOVE_CATEGORY_SUCCESS,
        EntityType.LANGUAGE,
        REMOVE_LANGUAGE_SUCCESS,
        EntityType.SOURCE,
        REMOVE_SOURCE_SUCCESS
    );

    public static final Map<EntityType, String> PICK_ENTITY_CONTAINER = Map.of(
        EntityType.REGION,
        PICK_REGION_SUCCESS,
        EntityType.CATEGORY,
        PICK_CATEGORY_SUCCESS,
        EntityType.LANGUAGE,
        PICK_LANGUAGE_SUCCESS,
        EntityType.SOURCE,
        PICK_SOURCE_SUCCESS
    );

    private static final Map<CommandType, Map<EntityType, String>> UPDATE_MSG_CONTAINER =
        ImmutableMap.<CommandType, Map<EntityType, String>>builder()
            .put(CommandType.PICK, PICK_ENTITY_CONTAINER)
            .put(CommandType.PICK_ALL, PICK_ENTITY_CONTAINER)
            .put(CommandType.REMOVE, RM_ENTITY_CONTAINER)
            .put(CommandType.REMOVE_ALL, RM_ENTITY_CONTAINER)
            .build();

    public static String getTemplate(CommandType c, EntityType e) {
        return UPDATE_MSG_CONTAINER.get(c).get(e);
    }

}
