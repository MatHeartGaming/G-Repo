package org.cis.controllo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.cis.Applicazione;
import org.cis.Constants;
import org.cis.modello.Qualifier;
import org.cis.modello.Query;
import org.cis.modello.Repository;
import org.cis.modello.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class OperatorTest {

    File dir;
    Path path;
    String separetor = FileUtils.PATH_SEPARATOR;
    boolean trovato = false;
    List<Repository> listaRep = new ArrayList<>();
    Repository repositories,repositories2,repositories3,repositories4;


    @BeforeEach
    void setUp() {

        Qualifier q2 = new Qualifier("fork","false");
        Qualifier q3 = new Qualifier("language","C#");
        List<Qualifier> listQual = new ArrayList<>();
        listQual.add(q2);
        listQual.add(q3);
        Query query = new Query(listQual);
        query.setDate("2007-10-29T00:00:00..2020-07-21T00:00:00");
        query.setToken("ed777d2a751c7f1d1fffe59a8d037d7f769af888\n");
        Session session = new Session(null);
        session.setQuery(query);
        Applicazione.getInstance().getSessionManager().addSession(session);
        String relativePath = separetor +"risorse" + separetor + "GHRepoSearcher" + separetor + "jar";
         path = FileUtils.createAbsolutePath(relativePath);
        dir = new File(path.toString());

        repositories = new Repository("1", "repodriller", "bla bla", "https://github.com/mauricioaniche/repodriller", "https://github.com/mauricioaniche/repodriller.git", 25169);
        repositories2 = new Repository("2", "top", "bla^3", "", "https://github.com/tomlongo/Flickable.js.git", 2000);
        repositories3 = new Repository("3", "456", "bla^3", "", "https://github.com/emergingstack/es-dev-stack.git", 5000);
        repositories4 = new Repository("4", "GRTFGR", "bla^3", "", "https://github.com/JazzCore/ctrlp-cmatcher.git", 8800);
        repositories.setStars(1700);
        repositories2.setStars(1800);
        repositories3.setStars(1500);
        repositories3.setStars(1400);

        listaRep.add(repositories);
        listaRep.add(repositories2);
        listaRep.add(repositories3);
        listaRep.add(repositories4);

        ObservableList<Repository> tabList = FXCollections.observableArrayList(listaRep);
        Applicazione.getInstance().getModello().addObject(Constants.LIST_REPO_UPDATED, tabList);
        Applicazione.getInstance().getModello().addObject(Constants.LIST_REPO, tabList);


    }

    @Test
    void testCreateConfigProperties() {
        System.out.println("Test Creazione del File Properties");

        Operator.createConfigProperties();
        File[] files = dir.listFiles();
        trovato= false;
        for (File f : files) {
            if(f.getName().equals("config.properties")){
                trovato = true;
            }
        }
        assertEquals(true,trovato);
    }

    @Test
    void testConfigToken() throws IOException {
        System.out.println("Test Token in file Properties");

        Operator.createConfigProperties();
        Properties props = new Properties();
        Object obj = null;
        String value = "";
        String key = "";
        String result= "";
        String pathConfig = path.toString()+ separetor +"config.properties";
        props.load(new FileInputStream(pathConfig));
        Enumeration e = props.keys();
        List ris = new ArrayList();
        trovato = false;
        while (e.hasMoreElements()) {
            obj = e.nextElement();
            value = props.getProperty(obj.toString());
            key = obj.toString();
            result = key + "=" + value;
            if(result.equals("username=ed777d2a751c7f1d1fffe59a8d037d7f769af888")){
                trovato = true;
            }
        }
        assertEquals(true,trovato);
    }

    @Test
    void testConfigLanguageAttributeSpecialCharacter() throws IOException  {
        System.out.println("Test Language con # in file Properties");
        Operator.createConfigProperties();
        Properties props = new Properties();
        Object obj = null;
        String value = "";
        String key = "";
        String result= "";
        String pathConfig = path.toString()+ separetor +"config.properties";
        props.load(new FileInputStream(pathConfig));
        Enumeration e = props.keys();
        List ris = new ArrayList();
        trovato = false;
        while (e.hasMoreElements()) {
            obj = e.nextElement();
            value = props.getProperty(obj.toString());
            key = obj.toString();
            result = key + "=" + value;
            if(result.equals("q3=language:Csharp")){
                trovato = true;
            }
            ris.add(result);
        }
        assertEquals(true,trovato);
    }


    @Test
    void testConfigPerPageAttribute() throws IOException{
        System.out.println("Test per_page attributes in file Properties");
        Operator.createConfigProperties();
        Properties props = new Properties();
        Object obj = null;
        String value = "";
        String key = "";
        String result= "";
        String pathConfig = path.toString()+ separetor +"config.properties";
        props.load(new FileInputStream(pathConfig));
        Enumeration e = props.keys();
        List ris = new ArrayList();
        trovato = false;
        while (e.hasMoreElements()) {
            obj = e.nextElement();
            value = props.getProperty(obj.toString());
            key = obj.toString();
            result = key + "=" + value;
            if(result.equals("per_page=100")){
                trovato = true;
            }
            ris.add(result);
        }
        assertEquals(true,trovato);
    }

    @Test
    void testconfrontaElemConParametriNotStrictNome(){
        System.out.println("Test confronta Repo per Nome");

        Repository repository = new Repository("1", "repodriller", "bla bla", "https://github.com/mauricioaniche/repodriller", "https://github.com/mauricioaniche/repodriller.git", 25169);
        String daCercare = "repodriller";
        String parametro = Constants.PARAM_REPOSITORIES;
        boolean ris = Operator.confrontaElemConParametriNotStrict(repository,daCercare,parametro, "");
        assertEquals(true,ris);
    }

    @Test
    void testconfrontaElemNomeNotFound(){
        System.out.println("Test confronta Repo per Nome fallito");

        Repository repository = new Repository("1", "repodriller", "bla bla", "https://github.com/mauricioaniche/repodriller", "https://github.com/mauricioaniche/repodriller.git", 25169);
        String daCercare = "ciao";
        String parametro = Constants.PARAM_REPOSITORIES;
        boolean ris = Operator.confrontaElemConParametriNotStrict(repository,daCercare,parametro, "");
        assertEquals(false,ris);
    }

    @Test
    void testconfrontaElemDimensioneSogliaInferiore(){
        System.out.println("Test dimensione Repo inferiore a soglia");

        Repository repository = new Repository("1", "repodriller", "bla bla", "https://github.com/mauricioaniche/repodriller", "https://github.com/mauricioaniche/repodriller.git", 25169);
        String daCercare = "30000";
        String parametro = Constants.PARAM_DIMENSION_SMALLER;
        boolean ris = false;
        ris = Operator.confrontaElemConParametriNotStrict(repository,daCercare,parametro, "");
        assertEquals(true, ris);
    }

    @Test
    void testconfrontaElemDimensioneSogliaSuperiore(){
        System.out.println("Test dimensione Repo superiore a soglia");

        Repository repository = new Repository("1", "repodriller", "bla bla", "https://github.com/mauricioaniche/repodriller", "https://github.com/mauricioaniche/repodriller.git", 25169);
        String daCercare = "30000";
        String parametro = Constants.PARAM_DIMENSION_GREATER;
        boolean ris = false;
        ris = Operator.confrontaElemConParametriNotStrict(repository, daCercare, parametro, "");
        assertEquals(false,ris);
    }

    @Test
    void testconfrontaElemStarsSogliaSuperiore(){
        System.out.println("Test Stars Repo superiore a soglia");

        Repository repository = new Repository("1", "repodriller", "bla bla", "https://github.com/mauricioaniche/repodriller", "https://github.com/mauricioaniche/repodriller.git", 25169);
        repository.setStars(1700);
        String daCercare = "1700";
        String parametro = Constants.PARAM_STARS_GREATER;
        boolean ris = false;
        ris = Operator.confrontaElemConParametriNotStrict(repository,daCercare,parametro, "");
        assertEquals(true,ris);
    }

    @Test
    void testconfrontaElemStarsSogliaInferiore(){
        System.out.println("Test Stars Repo inferiore a soglia");

        Repository repository = new Repository("1", "repodriller", "bla bla", "https://github.com/mauricioaniche/repodriller", "https://github.com/mauricioaniche/repodriller.git", 25169);
        repository.setStars(1700);
        String daCercare = "1700";
        String parametro = Constants.PARAM_STARS_SMALLER;
        boolean ris = false;
        ris = Operator.confrontaElemConParametriNotStrict(repository, daCercare, parametro, "");
        assertEquals(false,ris);
    }

    @Test
    void testCercaPerNomeStarsMinori(){

        System.out.println("Test Stars ListaRepo inferiori a soglia");

        String daCercare = "1700";
        String parametro = Constants.PARAM_STARS_SMALLER;
        boolean strict = false;

        ObservableList<Repository> listaRis = Operator.searchByName((ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO), daCercare, parametro, strict, "");
        assertEquals(2,listaRis.size());

    }

    @Test
    void testCercaPerNomeStarsMaggiori(){

        System.out.println("Test Stars ListaRepo superiori a soglia");

        String daCercare = "1700";
        String parametro = "Stars >=";

        daCercare = "1700";
        parametro = "Stars >=";
        boolean strict = false;

        ObservableList<Repository> listaRis = Operator.searchByName((ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO), daCercare, parametro, strict, "");
        assertEquals(2,listaRis.size());

    }

    @Test
    void testCercaPerNomeStarsMaggioriNome(){

        System.out.println("Test Stars ListaRepo superiore a soglia nome Repo");

        String daCercare = "1700";
        String parametro = Constants.PARAM_STARS_GREATER;

        boolean strict = false;

        ObservableList<Repository> listaRis = Operator.searchByName((ObservableList<Repository>) Applicazione.getInstance().getModello().getObject(Constants.LIST_REPO), daCercare, parametro, strict, "");
        assertEquals("repodriller",listaRis.get(0).getName());

    }
}