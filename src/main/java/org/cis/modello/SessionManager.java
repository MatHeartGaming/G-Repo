package org.cis.modello;

import java.util.ArrayList;
import java.util.List;

public class SessionManager {

    private List<Session> sessions = new ArrayList<>(); // Accumula le sessioni per salvarle alla chiusura del tool?.

    public List<Session> getSessions() {
        return sessions;
    }

    public boolean addSession (Session session) {
        if (session == null) {
            throw new IllegalArgumentException("the session cannot be null");
        }
        return this.getSessions().add(session);
    }

    public Session getCurrentSession () {
        return this.getSessions().size() == 0 ? null : this.getSessions().get(this.getSessions().size() - 1);
    }
}
