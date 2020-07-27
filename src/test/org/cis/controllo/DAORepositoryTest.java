package org.cis.DAO;

import org.cis.PrimaryController;
import org.cis.controllo.FileUtils;
import org.cis.controllo.TaskCloneRepositories;
import org.cis.modello.Repository;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DAORepositoryTest {

    String separetor = FileUtils.PATH_SEPARATOR;
    String relPath = separetor + "src" + separetor +"test" + separetor +"org" + separetor + "cis" + separetor + "controllo" + separetor +"resources" + separetor + "Json";
    String relPathSave = separetor + "src" + separetor +"test" + separetor +"org" + separetor + "cis" + separetor + "controllo" + separetor +"resources" + separetor + "savePath";

    String path = FileUtils.createAbsolutePath(relPath).toString();
    String savePath = FileUtils.createAbsolutePath(relPath).toString();


    @Test
    void loadRepositoriesListTest() {
        DAORepositoryJSON daoRepositoryJSON = new DAORepositoryJSON();
        List<Repository> listaRepo = daoRepositoryJSON.loadRepositories(path);
        assertEquals(12,listaRepo.size());
    }

    @Test
    void loadRepositoriesItemsTest() {
        DAORepositoryJSON daoRepositoryJSON = new DAORepositoryJSON();
        List<Repository> listaRepo = daoRepositoryJSON.loadRepositories(path);
        Repository repository = listaRepo.get(0);
        assertEquals(repository.getId(),"209921402");
        assertEquals(repository.getName(),"newbee-mall");
        assertEquals(repository.getLanguageProperty(), "Not determined (yet)");
    }

    @Test
    void saveRepositoriesTest(){
        DAORepositoryJSON daoRepositoryJSON = new DAORepositoryJSON();
        List<Repository> listaRepo = daoRepositoryJSON.loadRepositories(path);

        listaRepo.get(0).setLanguageProperty("java");
        daoRepositoryJSON.saveRepositories(savePath,listaRepo);

        //Json Repository salvata read
        List<Repository> listaRepoAggiornata = daoRepositoryJSON.loadRepositories(path);
        Repository repository = listaRepo.get(0);

        assertEquals(repository.getId(),"209921402");
        assertEquals(repository.getName(),"newbee-mall");
        assertEquals(repository.getLanguageProperty(), "java");
    }

}
