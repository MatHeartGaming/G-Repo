package org.cis.modello;

public class Qualifier {

    private String key;
    private String value;

    public Qualifier(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }
}
