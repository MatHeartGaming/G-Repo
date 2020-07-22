package org.cis.controllo;

import org.cis.Applicazione;
import org.cis.modello.Qualifier;
import org.cis.modello.Query;
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
    CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();


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

    }

    @Test
    void testCreateConfigProperties() {
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


}