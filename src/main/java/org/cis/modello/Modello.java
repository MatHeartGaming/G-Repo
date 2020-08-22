package org.cis.modello;

import java.util.*;

public class Modello {

    private Map<String, Object> map = new HashMap<>();

    public Map<String, Object> getMap() {
        return map;
    }

    public void addObject(String k, Object o) {
        this.map.put(k, o);
    }

    public Object getObject(String k) {
        return this.map.get(k);
    }
}
