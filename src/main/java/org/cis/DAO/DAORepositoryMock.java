package org.cis.DAO;

import org.cis.modello.Repository;

import java.util.ArrayList;
import java.util.List;

public class DAORepositoryMock implements IDAORepository{

    @Override
    public List<Repository> loadRepositories(String nameFile) {
            List<Repository> repositories = new ArrayList<>();

            if (true) {
                repositories.add(new Repository("1", "repodriller", "bla bla", "https://github.com/mauricioaniche/repodriller", "https://github.com/mauricioaniche/repodriller.git", 25169));
                repositories.add(new Repository("2", "Flickable.js", "bla^3", "", "https://github.com/tomlongo/Flickable.js.git", 3215));
                repositories.add(new Repository("3", "es-dev-stack", "bla^3", "", "https://github.com/emergingstack/es-dev-stack.git", 3201));
                repositories.add(new Repository("4", "ctrlp-cmatcher", "bla^3", "", "https://github.com/JazzCore/ctrlp-cmatcher.git", 31));
                return repositories;
            }

            if (false) {
                repositories.add(new Repository("5", "pandas", "bla bla", "https://github.com/mauricioaniche/repodriller", "https://github.com/pandas-dev/pandas.git", 25169));
                repositories.add(new Repository("6", "spark", "bla^3", "", "https://github.com/apache/spark.git", 321));
                return repositories;
            }

            return repositories;
    }

    @Override
    public void saveRepositories(String directoryFiles, List<Repository> repositories) {}
}
