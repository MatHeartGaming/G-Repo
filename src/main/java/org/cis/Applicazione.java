package org.cis;

import javafx.fxml.FXML;
import org.cis.DAO.DAORepositoryJSON;
import org.cis.DAO.DAORepositoryMock;
import org.cis.controllo.CommonEvents;
import org.cis.controllo.SingleThread;
import org.cis.modello.Modello;
import org.cis.modello.SessionManager;

public class Applicazione {

    @FXML
    private static Applicazione applicazione = new Applicazione();
    private CommonEvents commonEvents = new CommonEvents();
    private Modello modello = new Modello();
    private DAORepositoryMock daoRepositoryMock = new DAORepositoryMock();
    private SessionManager sessionManager = new SessionManager();
    private SingleThread singleThread = new SingleThread();
    private DAORepositoryJSON daoRepositoryJSON = new DAORepositoryJSON();

    public DAORepositoryJSON getDaoRepositoryJSON() {
        return daoRepositoryJSON;
    }

    public static Applicazione getInstance() {
        return applicazione;
    }

    public CommonEvents getCommonEvents() {
        return commonEvents;
    }

    public Modello getModello() {
        return modello;
    }

    public DAORepositoryMock getDaoRepositoryMock() {
        return daoRepositoryMock;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public SingleThread getSingleThread() { return singleThread; }
}
