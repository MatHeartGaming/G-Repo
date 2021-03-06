package org.cis;

import javafx.fxml.FXML;
import org.cis.DAO.*;
import org.cis.DAO.DAORepositoryMock;
import org.cis.controllo.*;
import org.cis.modello.Modello;
import org.cis.modello.SessionManager;

public class Applicazione {

    @FXML
    private static Applicazione applicazione = new Applicazione();
    private CommonEvents commonEvents = new CommonEvents();
    private Modello modello = new Modello();
    private DAORepositoryMock daoRepositoryMock = new DAORepositoryMock();
    private SessionManager sessionManager = new SessionManager();
    private DAORepositoryJSON daoRepositoryJSON = new DAORepositoryJSON();
    private DAORepositoryCSV daoRepositoryCSV = new DAORepositoryCSV();

    public DAORepositoryCSV getDaoRepositoryCSV() {
        return daoRepositoryCSV;
    }

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

}
