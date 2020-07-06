package org.cis.controllo;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressBar;
import org.cis.Applicazione;
import org.cis.Costanti;
import org.cis.modello.Qualifier;
import org.cis.modello.Query;
import org.cis.modello.Repository;
import org.cis.modello.Session;

import java.io.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Operatore {

    public static ObservableList<Repository> cercaPerNome(ObservableList<Repository> lista, String daCercare, String parametro, boolean strict) {
        ObservableList<Repository> risultato = FXCollections.observableArrayList();
        for(Repository repo : lista) {
            if(strict) {
                if(confrontaElemConParametriStrict(repo, daCercare, parametro)) {
                    risultato.add(repo);
                }
            } else {
                if(confrontaElemConParametriNotStrict(repo, daCercare, parametro)) {
                    risultato.add(repo);
                }
            }

        }
        return risultato;
    }

    private static boolean confrontaElemConParametriStrict(Repository repo, String daCercare, String parametro) {
        if(daCercare.isEmpty()) {
            return true;
        }
        if(parametro.equals(Costanti.PARAM_LINGUA)) {
            if(repo.getLingua().toLowerCase().equals(daCercare.toLowerCase().trim())) {
                return true;
            }
        } else if(parametro.equals(Costanti.PARAM_LINGUAGGIO)) {
            if(repo.getProgrammingLanguage().toLowerCase().equals(daCercare.toLowerCase().trim())) {
                return true;
            }
        } else if(parametro.equals(Costanti.PARAM_DATA_COMMIT)) {
            if(repo.getLastCommitDate().toString().toLowerCase().equals(daCercare.toLowerCase().trim())) {
                return true;
            }
        } else if(parametro.equals(Costanti.PARAM_URL)){
            if(repo.getUrlProject().toLowerCase().equals(daCercare.toLowerCase().trim())) {
                return true;
            }
        } else if (parametro.equals(Costanti.PARAM_DIMENSIONE)) {
            String dimensione = repo.turnIntToStringProperty().get();
            if(dimensione.equals(daCercare.trim())) {
                return true;
            }
        } else {
            if(repo.getName().toLowerCase().equals(daCercare.toLowerCase().trim())) {
                return true;
            }
        }
        return false;
    }

    private static boolean confrontaElemConParametriNotStrict(Repository repo, String daCercare, String parametro) {
        if(parametro.equals(Costanti.PARAM_LINGUA)) {
            if(repo.getLingua().toLowerCase().contains(daCercare.toLowerCase().trim())) {
                return true;
            }
        } else if(parametro.equals(Costanti.PARAM_LINGUAGGIO)) {
            if(repo.getProgrammingLanguage().toLowerCase().contains(daCercare.toLowerCase().trim())) {
                return true;
            }
        } else if(parametro.equals(Costanti.PARAM_DATA_COMMIT)) {
            if(repo.getLastCommitDate().toString().toLowerCase().contains(daCercare.toLowerCase().trim())) {
                return true;
            }
        } else if(parametro.equals(Costanti.PARAM_URL)){
            if(repo.getUrlProject().toLowerCase().contains(daCercare.toLowerCase().trim())) {
                return true;
            }
        } else if (parametro.equals(Costanti.PARAM_DIMENSIONE)) {
            String dimensione = repo.turnIntToStringProperty().get();
            if(dimensione.contains(daCercare.trim())) {
                return true;
            }
        } else {
            if(repo.getName().toLowerCase().contains(daCercare.toLowerCase().trim())) {
                return true;
            }
        }
        return false;
    }


    public static void createConfigProperties(){
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("Creazione file properties", 1);}});


        System.out.println("Avvio Creazione File Properties!");

        Session session = Applicazione.getInstance().getSessionManager().getCurrentSession();
        Query query = session.getQuery();
        List<Qualifier> listaQualificatori = query.getQualifiers();

        try (OutputStream output = new FileOutputStream("risorse/GHRepoSearcher/jar/config.properties")) {


            PrintStream write = new PrintStream(output);
            write.println("output_path=../../json" );
            write.println("username=" + query.getToken());
            write.println("q1=created:"+ query.getDate());

            Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("Procede bene :)", 2);}});
            int j = 2;

            for (int i=0; i < listaQualificatori.size(); i++){

                Qualifier q = listaQualificatori.get(i);
                write.println("q" + j + "=" + q.getKey() +":"+ q.getValue());
                j = j +1;
                if(i == Math.floor(listaQualificatori.size() / 2)) {
                    Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("Siamo a metà!", 3);}});
                }
            }

            Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("Ci siamo quasi!! :D", 4);}});

            if(query.getSort() != null) {
                write.println("sort=" + query.getSort());
            }
            if(query.getOrder() != null) {
                write.println("order=" + query.getOrder());
            }


            write.println("per_page=100");


        } catch (IOException io) {
            Applicazione.getInstance().getCommonEvents().showExceptionDialog(io);
            io.printStackTrace();
        }

        Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("File properties creato! E' stato un piacere lavorare per lei!", 5);}});

    }


    public static boolean avvioGHRepoSearcher(){
        System.out.println("avvio GHRepoSearcher!");
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("Avvio GHRepoSearcher...", 1);}});

        try {

            String path = new java.io.File("").getAbsolutePath();
            path = path + "\\risorse\\GHRepoSearcher\\jar";
            //System.out.println(path);

            File dir = new File(path);
            //System.out.println(dir);

            // ripulisco la cartella Json
            Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("Ripulisco la cartella dei file JSON...", 1);}});

            String pathJson = new java.io.File("").getAbsolutePath();
            pathJson = pathJson + "\\risorse\\json";
            File dir2 = new File(pathJson);
            File[] files = dir2.listFiles();
            int i = 0;
            for (File f : files) {
                f.delete();
                if(i == Math.floor(files.length / 2)) {
                    Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("Siamo a metà pulizia e non accenno a fermarmi! :)", 2);}});
                }
                i++;
            }

            Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("Pulizia terminata!", 3);}});

            //setto il command
            String cmd = "java -jar GHRepoSearcher.jar config.properties";
            Process process = Runtime.getRuntime().exec(cmd, null, dir);
            Applicazione.getInstance().getModello().addObject(Costanti.THREAD_REPO_SEARCHER, process);

            Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("Lavorando dietro le quinte...", 3);}});

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));

            // Read the output from the command
            String s = null;

            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }
            // Read any errors from the attempted command


            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
                return false;
            }

        } catch (IOException ex) {
            Applicazione.getInstance().getCommonEvents().showExceptionDialog(ex);
            ex.printStackTrace();
        }

        return true;
    }

    public static String filterText(String text) {
        //removes html tags
        text = text.replaceAll("\\<.*?\\>", "");
        //removes markdown code snippets
        String regex = "(```.+?```)";
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(text);
        text = matcher.replaceAll("");
        //removes markdown images
        String regex2 = "!\\[[^\\]]+\\]\\([^)]+\\)";
        Pattern pattern2 = Pattern.compile(regex2);
        Matcher matcher2 = pattern2.matcher(text);
        text = matcher2.replaceAll("");
        //removes markdown links
        String regex3 = "\\[(.*?)\\]\\(.*?\\)";
        Pattern pattern3 = Pattern.compile(regex3);
        Matcher matcher3 = pattern3.matcher(text);
        while(matcher3.find() == true) {
            String replaceString = matcher3.group(1);
            String toBeReaplacedString = matcher3.group();
            text = text.replace(toBeReaplacedString, replaceString);
        }
        //System.out.println(text);
        return text;
    }


}
