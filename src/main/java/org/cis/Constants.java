package org.cis;

public class Constants {

    public static final String RELATIVE_PATH_CLONING_DIRECTORY = "\\risorse\\cacheCloneRepositories";

    public static final String RELATIVE_PATH_INPUT_CSV = "\\risorse\\GHLanguageDetection\\input.csv";
    public static final String RELATIVE_PATH_OUTPUT_CSV = "\\risorse\\GHLanguageDetection\\output.csv";

    public static final String RELATIVE_PATH_LANGUAGE_REPOSITORIES = "\\risorse\\lingua";
    public static final String RELATIVE_PATH_ENGLISH = RELATIVE_PATH_LANGUAGE_REPOSITORIES + "\\" + "english";
    public static final String RELATIVE_PATH_MIXED = RELATIVE_PATH_LANGUAGE_REPOSITORIES + "\\" + "mixed";
    public static final String RELATIVE_PATH_NOT_ENGLISH = RELATIVE_PATH_LANGUAGE_REPOSITORIES + "\\" + "not_english";
    public static final String RELATIVE_PATH_UNKNOWN = RELATIVE_PATH_LANGUAGE_REPOSITORIES + "\\" + "unknown";

    public static final String TASK_CLONE_REPOSITORIES = "TASK_CLONE_REPOSITORIES";

    public static final String MAP_REPOSITORY_PROGRAMMING_LANGUAGE = "MAP_REPOSITORY_LANGUAGE_PROGRAMMING";

    public static final String INDEX_LAST_CLONED_REPOSITORY = "INDEX_LAST_CLONED_REPOSITORY";
    public static final String LISTA_REPO = "lista repo";
    public static final String LISTA_REPO_AGGIORNATA = "lista repo aggiornata";

    public static final String[] LINGUA = {"Inglese", "Non-Inglese", "Misto"};

    public static final String PARAM_LANGUAGE = "Lingua";
    public static final String PARAM_PROGR_LANGUAGE = "Linguaggio";
    public static final String PARAM_DATE_COMMIT = "Data ultimo commit";
    public static final String PARAM_URL = "URL";
    public static final String PARAM_REPOSITORIES = "Repositories";
    public static final String PARAM_DIMENSION = "Dimensione";
    public static final String PARAM_STARS = "Stars";

    public static final String ULTIMO_CAMPO_KEY = "ultimo campo Q";

    public static final String THREAD_DOWNLOAD_REPO = "thread repo";
    public static final String THREAD_REPO_SEARCHER = "Thread tool";
    public static final String THREAD_LANGUAGE = "thread language";

    public static final String ACCEPT_DELETION_PROCESS = "delete json";
    public static final String MESSAGGIO_FINE_RICERCA = "mex tool";

    public static final String PROGRESS_BAR = "progress bar";
    public static final Float[] values = new Float[] {-1.0f, 0f, 0.25f, 0.5f, 0.75f, 1.0f};
    public static final String LABEL_PROGRESS = "label progress";

    public static final String PRIMARY_STAGE = "primary stage";
    public static final String SAVE_PATH = "save path";
    public static final String THREAD_SAVE = "thread save";

    public static final String[] LISTA_QUAL = {"in", "repo", "user", "org", "size", "followers", "fork",
            "stars", "pushed", "language", "topic", "topics", "license",
            "is", "mirror", "archived", "good-first-issues", "help-wanted-issues"};


    public static final String COLORE_PRIMARIO = "#E9ECEF";
    public static final String COLORE_SECONDARIO = "#1c2541";
    public static final String BUTTON_HOVER_COLOR = "#48CAE4";
    public static final String COLORE_BUTTON = "#90E0EF";
    public static final String COLOR_HOVER_TEXTFIELD = "#212529";
    public static final String COLOR_TEXTFIELD = "#6C757D";

}
