package org.cis;

import org.cis.controllo.FileUtils;

public class Constants {

    static {
        root = FileUtils.getRootPath();
    }

    public static final String root;

    public static final String CLONING_DIRECTORY = root + "\\risorse\\cacheCloneRepositories";

    public static final String INDEX_LAST_CLONED_REPOSITORY = "INDEX_LAST_CLONED_REPOSITORY";
    public static final String LISTA_REPO = "lista repo";
    public static final String LISTA_REPO_AGGIORNATA = "lista repo aggiornata";

    public static final String[] LINGUA = {"Inglese", "Non-Inglese", "Misto"};

    public static final String PARAM_LINGUA = "Lingua";
    public static final String PARAM_LINGUAGGIO = "Linguaggio";
    public static final String PARAM_DATA_COMMIT = "Data ultimo commit";
    public static final String PARAM_URL = "URL";
    public static final String PARAM_REPOSITORIES = "Repositories";
    public static final String PARAM_VERSIONE = "Versione";
    public static final String PARAM_DIMENSIONE = "Dimensione";

    public static final String ULTIMO_CAMPO_KEY = "ultimo campo Q";

    public static final String THREAD_DOWNLOAD_REPO = "thread repo";
    public static final String THREAD_REPO_SEARCHER = "Thread tool";
    public static final String THREAD_LANGUAGE = "thread language";

    public static final String ACCEPT_DELETION_PROCESS = "delete json";
    public static final String MESSAGGIO_FINE_RICERCA = "mex tool";

    public static final String PROGRESS_BAR = "progress bar";
    public static final Float[] values = new Float[] {-1.0f, 0f, 0.25f, 0.5f, 0.75f, 1.0f};
    public static final String LABEL_PROGRESS = "label progress";

    public static final String[] LISTA_QUAL = {"in", "repo", "user", "org", "size", "followers", "fork",
            "stars", "pushed", "language", "topic", "topics", "license",
            "is", "mirror", "archived", "good-first-issues", "help-wanted-issues"};


    public static final String COLORE_PRIMARIO = "#0b132b";
    public static final String COLORE_SECONDARIO = "#1c2541";
    public static final String HOVER_COLOR = "#6fffe9";
    public static final String COLORE_BUTTON = "#5bc0be";

}