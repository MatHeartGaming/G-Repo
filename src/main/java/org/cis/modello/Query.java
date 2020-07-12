package org.cis.modello;

import java.util.List;

public class Query {

    private String token; // Non più Opzionale
    private List<Qualifier> qualifiers;
    private String sort;
    private String order;
    private String date;
    private String directoryFile; // File JSON scaricati da GHRepoSearcher.

    public Query(List<Qualifier> qualifiers) {
        this.qualifiers = qualifiers;
    }

    public List<Qualifier> getQualifiers() {
        return qualifiers;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getDirectoryFile() {
        return directoryFile;
    }

    public void setDirectoryFile(String directoryFile) {
        this.directoryFile = directoryFile;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
