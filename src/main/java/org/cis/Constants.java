package org.cis;

public class Constants {

    public static final String RELATIVE_PATH_CLONING_DIRECTORY = "\\risorse\\cacheCloneRepositories";

    public static final String RELATIVE_PATH_INPUT_CSV = "\\risorse\\GHLanguageDetection\\input.csv";
    public static final String RELATIVE_PATH_OUTPUT_CSV = "\\risorse\\GHLanguageDetection\\output.csv";

    public static final String RELATIVE_PATH_LANGUAGE_REPOSITORIES = "\\risorse\\lingua";

    public static final String RELATIVE_PATH_JSON = "\\risorse\\json";

    public static final String PROCESS_LANGUAGE_DETECTION= "Process language detection";
    public static final String MESSAGE_LANGUAGE_DETECTION = "messaggio language detection";


    public static final String GHREPO_SEARCHER_JAR = "\\risorse\\GHRepoSearcher\\jar";
    public static final String TOOL_LANGUAGE_DETECTION = "risorse\\GHLanguageDetection";

    public static final String TASK_CLONE_REPOSITORIES = "TASK_CLONE_REPOSITORIES";

    public static final String MESSAGE_NOT_EXISTS = "Not exists";
    public static final String MESSAGE_NOT_DETERMINED_YET = "Not determined (yet)";

    public static final String IS_SAVE_REPOSITORIES = "IS_SAVE_REPOSITORIES";
    public static final String IS_LANGUAGE_DETECTION = "IS_LANGUAGE_DETECTION";

    public static final String MAP_REPOSITORY_PROGRAMMING_LANGUAGE = "MAP_REPOSITORY_LANGUAGE_PROGRAMMING";
    public static final String MAP_REPOSITORY_LANGUAGE = "MAP_REPOSITORY_LANGUAGE";

    public static final String INDEX_LAST_CLONED_REPOSITORY = "INDEX_LAST_CLONED_REPOSITORY";
    public static final String LIST_REPO = "lista repo";
    public static final String LIST_REPO_UPDATED = "lista repo aggiornata";

    public static final String PARAM_LANGUAGE_GREATER = "Language >=";
    public static final String PARAM_LANGUAGE_SMALLER = "Language <";
    public static final String PARAM_PROGR_LANGUAGE_GREATER = "Progr. Language >=";
    public static final String PARAM_PROGR_LANGUAGE_SMALLER = "Progr. Language <";
    public static final String PARAM_DATE_COMMIT = "Last Commit Date";
    public static final String PARAM_URL = "URL";
    public static final String PARAM_REPOSITORIES = "Repositories";
    public static final String PARAM_DIMENSION_GREATER = "Dimension >=";
    public static final String PARAM_DIMENSION_SMALLER = "Dimension <";
    public static final String PARAM_STARS_GREATER = "Stars >=";
    public static final String PARAM_STARS_SMALLER = "Stars <";

    public static final String ULTIMO_CAMPO_KEY = "ultimo campo Q";

    public static final String THREAD_WARNING_PANEL = "thread repo";
    public static final String THREAD_REPO_SEARCHER = "Thread tool";

    public static final String ACCEPT_WARNING_MEX = "delete json";
    public static final String MESSAGE_END_SEARCH = "mex tool";

    public static final String PROGRESS_BAR = "progress bar";
    public static final Float[] values = new Float[] {-1.0f, 0f, 0.25f, 0.5f, 0.75f, 1.0f};
    public static final String LABEL_PROGRESS = "label progress";

    public static final String PRIMARY_STAGE = "primary stage";
    public static final String ROOT = "root";

    public static final String[] LISTA_QUAL = {"in", "repo", "user", "org", "size", "followers", "fork",
            "stars", "pushed", "language", "topic", "topics", "license",
            "is", "mirror", "archived", "good-first-issues", "help-wanted-issues"};


    public static final String COLOR_PRIMARY = "#E9ECEF";
    public static final String COLOR_SECONDARY = "#1c2541";
    public static final String BUTTON_HOVER_COLOR = "#212529";
    public static final String COLOR_BUTTON = "#343a40";
    public static final String COLOR_HOVER_TEXTFIELD = "#212529";
    public static final String COLOR_TEXTFIELD = "#6C757D";
    public static final String COLOR_BUTTON_CLEARER = "#6c757d";
    public static final String COLOR_BUTTON_CLEARER_HOVER = "#495057";


    //LEGACY COLORS
    public static final String LEGACY_MODE_ON = "legacy mode on";
    public static final String COLOR_BUTTON_LEGACY_PRIMARY = "#90E0EF";
    public static final String COLOR_BUTTON_LEGACY_HOVER = "#48CAE4";
    public static final String COLOR_BUTTON_LEGACY_GREEN = "#99ff33";
    public static final String COLOR_BUTTON_LEGACY_GREEN_HOVER = "#00ff00";
    public static final String COLOR_BUTTON_LEGACY_RED = "#cc3333";
    public static final String COLOR_BUTTON_LEGACY_RED_HOVER = "#ff0000";

}
