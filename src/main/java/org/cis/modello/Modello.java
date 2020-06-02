package org.cis.modello;

import java.util.*;

public class Modello {

    private Map<String, Object> mappa = new HashMap<>();

    public Map<String, Object> getMappa() {
        return mappa;
    }

    public void addObject(String k, Object o) {
        this.mappa.put(k, o);
    }

    public Object getObject(String k) {
        return this.mappa.get(k);
    }
}
