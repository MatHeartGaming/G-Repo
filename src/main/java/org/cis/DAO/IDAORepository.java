package org.cis.DAO;

import org.cis.modello.Repository;

import java.util.List;

public interface IDAORepository {

    public List<Repository> loadRepositories(String nameFile);
    public void saveRepositories (String directoryFiles, List<Repository> repositories);

}
