package org.cis.DAO;

import org.cis.Costanti;
import org.cis.modello.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DAORepositoryMock implements IDAORepository{

    @Override
    public List<Repository> loadRepositories(String nameFile) {
        List<Repository> repositories = new ArrayList<>();
        String url = "https://sonoUnLinkAUnaRepo.com";
        repositories.add(new Repository("Hello World", "1.0", LocalDate.of(2016, 9, 27), url, 101, Costanti.LINGUA[0], "Java"));
        repositories.add(new Repository("Bruno Mars", "2.0.3", LocalDate.of(2016, 10, 28), url, 89, Costanti.LINGUA[0], "Java"));
        repositories.add(new Repository("Coldplay", "1.0", LocalDate.of(2013, 12, 22), url, 900, Costanti.LINGUA[0], "C++"));
        repositories.add(new Repository("Travis Scott", "1.2.3", LocalDate.of(2011, 3, 25), url, 340, Costanti.LINGUA[0], "Java"));
        repositories.add(new Repository("Mirko Gisonte", "5.0.1",LocalDate.of(2016, 4, 15), url, 823, Costanti.LINGUA[0], "Java"));
        repositories.add(new Repository("CHVRCHES", "1.0", LocalDate.of(2015, 9, 28), url, 129, Costanti.LINGUA[0], "Java"));
        repositories.add(new Repository("Queen", "1.0",LocalDate.of(2014, 9, 28), url, 89, Costanti.LINGUA[0], "Java"));
        repositories.add(new Repository("Vasco Rossi", "1.0", LocalDate.of(2020, 7, 12), url, 76, Costanti.LINGUA[0], "C"));
        repositories.add(new Repository("Post Malone", "1.0", LocalDate.of(2016, 9, 28), url, 45, Costanti.LINGUA[0], "Java"));
        repositories.add(new Repository("Repo 3", "1.0", LocalDate.of(2010, 9, 11), url, 241, Costanti.LINGUA[2], "Java"));
        repositories.add(new Repository("Repo 4", "1.0", LocalDate.of(2019, 8, 28), url, 77, Costanti.LINGUA[0], "C#"));
        repositories.add(new Repository("Repo 5", "1.0", LocalDate.of(2011, 1, 23), url, 108, Costanti.LINGUA[1], "Java"));
        repositories.add(new Repository("Repo 6", "1.0", LocalDate.of(2012, 2, 28), url, 775, Costanti.LINGUA[2], "Java"));
        repositories.add(new Repository("Repo 7", "1.0.1", LocalDate.of(2016, 9, 24), url, 701, Costanti.LINGUA[0], "PHP"));
        repositories.add(new Repository("Repo 8", "1.0.4", LocalDate.of(2014, 3, 28), url, 126, Costanti.LINGUA[0], "Java"));
        repositories.add(new Repository("Repo 1", "2.1.3", LocalDate.of(2016, 5, 3), url, 432, Costanti.LINGUA[1], "PHP"));
        repositories.add(new Repository("Repo 2", "3.0", LocalDate.of(2016, 4, 7), url, 4002, Costanti.LINGUA[0], "Java"));
        repositories.add(new Repository("Repo 3", "1.2", LocalDate.of(2019, 7, 18), url, 690, Costanti.LINGUA[1], "Java"));
        repositories.add(new Repository("Repo 4", "1.6.1", LocalDate.of(2020, 6, 5), url, 678, Costanti.LINGUA[0], "C++"));
        repositories.add(new Repository("Repo 5", "5.1.9", LocalDate.of(2015, 6, 28), url, 450, Costanti.LINGUA[0], "Java"));
        repositories.add(new Repository("AR.js", "3.0", LocalDate.of(1995, 1, 18), url, 3491, Costanti.LINGUA[2], "JavaScript"));
        repositories.add(new Repository("Repo 7", "1.0", LocalDate.of(2016, 6, 30), url, 210, Costanti.LINGUA[0], "C"));
        repositories.add(new Repository("Repo 8", "1.0", LocalDate.of(2019, 8, 21), url, 21, Costanti.LINGUA[1], "PHP"));
        return repositories;
    }

    @Override
    public void saveRepositories(String directoryFiles, List<Repository> repositories) {}
}
