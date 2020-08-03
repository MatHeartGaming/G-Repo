package org.cis.modello;

import javafx.collections.ObservableList;

import java.time.LocalDateTime;
import java.util.List;

public class Session {

    private LocalDateTime date = LocalDateTime.now();
    private Query query; // Optional.
    private ObservableList<Repository> repositories;

    public Session(ObservableList<Repository> repositories) {
        this.repositories = repositories;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<Repository> getRepositories() {
        return repositories;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }
}
