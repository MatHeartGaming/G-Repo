package org.cis.controllo;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.cis.Applicazione;
import org.cis.Constants;
import org.cis.modello.*;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Operator {



    public static ObservableList<Repository> cercaPerNome(ObservableList<Repository> lista, String daCercare, String parametro, boolean strict, String percent) {
        ObservableList<Repository> risultato = FXCollections.observableArrayList();
        if(lista == null) {
            return risultato;
        }
        for(Repository repo : lista) {
            if(strict) {
                if(confrontaElemConParametriStrict(repo, daCercare, parametro, percent)) {
                    risultato.add(repo);
                }
            } else {
                if(confrontaElemConParametriNotStrict(repo, daCercare, parametro, percent)) {
                    risultato.add(repo);
                }
            }

        }
        return risultato;
    }

    public static boolean confrontaElemConParametriStrict(Repository repo, String daCercare, String parametro, String percent) {
        daCercare = daCercare.trim();
        if (daCercare.isEmpty()) {
            return true;
        }
        if (parametro.equals(Constants.PARAM_LANGUAGE)) {
            // TODO: 20/07/2020 gestire la ricerca con l'oggetto RepositoryLanguage e quindi usare la mappa Constants.MAP_REPOSITORY_LANGUAGE.
            if (repo.getLanguageProperty() != null && repo.getLanguageProperty().equalsIgnoreCase(daCercare)) {
                return true;
            }
        } else if (parametro.equals(Constants.PARAM_REPOSITORIES)) {
            if (repo.getName().equalsIgnoreCase(daCercare)) {
                if (repo.getLanguageProperty() != null && repo.getLanguageProperty().toLowerCase().equals(daCercare.toLowerCase().trim())) {
                    return true;
                }
            }
        } else if (parametro.equals(Constants.PARAM_PROGR_LANGUAGE)) {
                Map<String, StatisticsProgrammingLanguage> languageProgrammingMap =
                        (Map<String, StatisticsProgrammingLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_PROGRAMMING_LANGUAGE);
                StatisticsProgrammingLanguage statisticsProgrammingLanguage = languageProgrammingMap.get(repo.getId());
                String finalDaCercare = daCercare;
                if (statisticsProgrammingLanguage != null && statisticsProgrammingLanguage.existsProgrammingLanguage(language -> language.toLowerCase().equals(finalDaCercare.toLowerCase().trim()))) {
                    return true;
                }
        } else if (parametro.equals(Constants.PARAM_DATE_COMMIT)) {
                if (repo.getLastCommitDate() != null && repo.getLastCommitDate().toString().toLowerCase().equals(daCercare.toLowerCase().trim())) {
                    return true;
                }
        } else if (parametro.equals(Constants.PARAM_URL)) {
                if (repo.getUrlProject().equalsIgnoreCase(daCercare)) {
                    return true;
                }
        } else if (parametro.equals(Constants.PARAM_DIMENSION_GREATER)) {
                String dimensione = repo.getSizeString();
                Sorter.SortByDimension sorter = new Sorter().new SortByDimension();
                if (sorter.compare(dimensione, daCercare) == 0) {
                    return true;
                }
        } else if (parametro.equals(Constants.PARAM_DIMENSION_SMALLER)) {
                String dimensione = repo.getSizeString();
                Sorter.SortByDimension sorter = new Sorter().new SortByDimension();

                if (sorter.compare(dimensione, daCercare) < 0) {
                    return true;
                }
        } else if (parametro.equals(Constants.PARAM_STARS_GREATER)) {
                String stars = repo.starsProperty().get().trim();
                Sorter.SortByStars sorter = new Sorter().new SortByStars();

                if (sorter.compare(stars, daCercare) == 0) {
                    return true;
                }
        } else if (parametro.equals(Constants.PARAM_STARS_SMALLER)) {
                String stars = repo.starsProperty().get().trim();
                Sorter.SortByStars sorter = new Sorter().new SortByStars();

                if (sorter.compare(stars, daCercare) < 0) {
                    return true;
                }
        } else {
                if (repo.getName().equalsIgnoreCase(daCercare)) {
                    return true;
                }
        }
        return false;
    }

    public static boolean confrontaElemConParametriNotStrict(Repository repo, String daCercare, String parametro, String percent) {
        daCercare = daCercare.trim();
        if(daCercare.isEmpty()) {
            return true;
        }
        if(parametro.equals(Constants.PARAM_LANGUAGE)) {
            if(repo.getLanguageProperty() != null && repo.getLanguageProperty().toLowerCase().contains(daCercare.toLowerCase().trim())) {
                return true;
            }
        } else if(parametro.equals(Constants.PARAM_PROGR_LANGUAGE)) {
            Map<String, StatisticsProgrammingLanguage> languageProgrammingMap =
                    (Map<String, StatisticsProgrammingLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_PROGRAMMING_LANGUAGE);
            StatisticsProgrammingLanguage statisticsProgrammingLanguage = languageProgrammingMap.get(repo.getId());
            String finalDaCercare = daCercare;
            if(statisticsProgrammingLanguage != null && statisticsProgrammingLanguage.existsProgrammingLanguage(language -> language.toLowerCase().contains(finalDaCercare.toLowerCase().trim()))) {
                return true;
            }
        } else if(parametro.equals(Constants.PARAM_DATE_COMMIT)) {
            if(repo.getLastCommitDate() != null && repo.getLastCommitDate().toString().toLowerCase().contains(daCercare.toLowerCase().trim())) {
                return true;
            }
        } else if(parametro.equals(Constants.PARAM_URL)){
            if(repo.getUrlProject().toLowerCase().contains(daCercare.toLowerCase().trim())) {
                return true;
            }
        } else if (parametro.equals(Constants.PARAM_DIMENSION_GREATER)) {
            if(daCercare.equals("")) {
                return true;
            }
            String dimensione = repo.getSizeString();
            Sorter.SortByDimension sorter = new Sorter().new SortByDimension();

            if(sorter.compare(dimensione, daCercare) >= 0) {
                return true;
            }
        } else if(parametro.equals(Constants.PARAM_DIMENSION_SMALLER)) {
            String dimensione = repo.getSizeString();
            Sorter.SortByDimension sorter = new Sorter().new SortByDimension();

            if(sorter.compare(dimensione, daCercare) < 0) {
                return true;
            }
        }  else if(parametro.equals(Constants.PARAM_STARS_GREATER)) {
            String stars = repo.starsProperty().get().trim();
            Sorter.SortByStars sorter = new Sorter().new SortByStars();

            if(sorter.compare(stars, daCercare) >= 0) {
                return true;
            }
        } else if(parametro.equals(Constants.PARAM_STARS_SMALLER)) {
            String stars = repo.starsProperty().get().trim();
            Sorter.SortByStars sorter = new Sorter().new SortByStars();

            if(sorter.compare(stars, daCercare) < 0) {
                return true;
            }
        } else {
            if (repo.getName().toLowerCase().contains(daCercare.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


    private static boolean netIsAvailable() {
        try {
            final URL url = new URL("https://www.github.com");
            final URLConnection conn = url.openConnection();
            conn.connect();
            conn.getInputStream().close();
            return true;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return false;
        }
    }

    public static void createConfigProperties(){

        System.out.println("Avvio Creazione File Properties!");

        Session session = Applicazione.getInstance().getSessionManager().getCurrentSession();
        Query query = session.getQuery();
        List<Qualifier> listaQualificatori = query.getQualifiers();

        String separator = FileUtils.PATH_SEPARATOR;
        String relativePath = "risorse" + separator + "GHRepoSearcher" + separator + "jar" + separator +"config.properties";
        System.out.println(relativePath);


        try (OutputStream output = new FileOutputStream(relativePath)) {


            PrintStream write = new PrintStream(output);
            write.println("output_path=../../json" );
            write.println("username=" + query.getToken());
            write.println("q1=created:"+ query.getDate());

            int j = 2;

            for (int i=0; i < listaQualificatori.size(); i++){

                Qualifier q = listaQualificatori.get(i);
                if(q.getValue().equals("C#")) {
                    q.setValue("Csharp");
                }
                write.println("q" + j + "=" + q.getKey() .trim()+":"+ q.getValue().trim());
                j = j +1;

            }


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


    }


    public static boolean avvioGHRepoSearcher(){

        //Controllo la connessione ad internet
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        System.out.println("Checking internet connection!");

        boolean connect = netIsAvailable();

        if(connect == false){
            Applicazione.getInstance().getModello().addObject(Constants.MESSAGGIO_FINE_RICERCA,"Connection error!");
            return false ;
        }

        System.out.println("avvio GHRepoSearcher!");
        Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("Launching GHRepoSearcher...", 1);}});

        String separetor = FileUtils.PATH_SEPARATOR;
        try {

            String relativePath = separetor +"risorse" + separetor + "GHRepoSearcher" + separetor + "jar";
            Path path = FileUtils.createAbsolutePath(relativePath);
            System.out.println(path.toString());

            File dir = new File(path.toString());
            //System.out.println(dir);

            // ripulisco la cartella Json
            Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("Cleaning up JSON folder...", 1);}});

            String relativePathJson = separetor +"risorse" + separetor + "json";
            Path pathJson = FileUtils.createAbsolutePath(relativePathJson);
            System.out.println(pathJson.toString());

            File dir2 = new File(pathJson.toString());
            File[] files = dir2.listFiles();
            int i = 0;
            for (File f : files) {
                f.delete();
                if(i == Math.floor(files.length / 2)) {
                    Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("We're half way through the cleaning process and I'm not gonna stop! :)", 2);}});
                }
                i++;
            }

            Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("Cleaning up terminated!", 3);}});

            //setto il command
            String cmd = "java -jar GHRepoSearcher.jar config.properties";
            Process process = Runtime.getRuntime().exec(cmd, null, dir);
            Applicazione.getInstance().getModello().addObject(Constants.THREAD_REPO_SEARCHER, process);

            Platform.runLater(new Runnable() {@Override public void run() {commonEvents.setProgressBar("Working behind the scenes...", 3);}});

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));

            // Read the output from the command
            String s = null;

            while ((s = stdInput.readLine()) != null) {
                if (s.contains("ERROR")){
                    System.out.println(s);
                    Applicazione.getInstance().getModello().addObject(Constants.MESSAGGIO_FINE_RICERCA,"Token non valido");
                    return false;
                }
                System.out.println(s);
            }
            // Read any errors from the attempted command


            while ((s = stdError.readLine()) != null) {
                Applicazione.getInstance().getModello().addObject(Constants.MESSAGGIO_FINE_RICERCA,s);
                System.out.println(s);
                return false;
            }

        } catch (IOException ex) {
            Applicazione.getInstance().getCommonEvents().showExceptionDialog(ex);

            ex.printStackTrace();
        }

        return true;
    }

    public static boolean actionDetectIdiom() {
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        System.out.println("Avvio processo di language detection");
        try {

            String separetor = FileUtils.PATH_SEPARATOR;

            String savePath = "risorse" + separetor + "lingua";
            System.out.println("Path salvataggio lingua: " + FileUtils.createAbsolutePath(savePath));

            String pathEnRel = savePath + separetor + "english";
            Path pathEn = FileUtils.createAbsolutePath(pathEnRel);
            System.out.println("Path salvataggio lingua english delete: " + pathEn);
            Files.list(pathEn).forEach(FileUtils::deleteDirTree);


            String pathNotEnRel = savePath + separetor + "not_english";
            Path pathNotEn = FileUtils.createAbsolutePath(pathNotEnRel);
            System.out.println("Path salvataggio lingua not english delete: " + pathNotEn);
            Files.list(pathNotEn).forEach(FileUtils::deleteDirTree);

            String pathMixRel = savePath + separetor + "mixed";
            Path pathMix = FileUtils.createAbsolutePath(pathMixRel);
            System.out.println("Path salvataggio lingua mixed delete: " + pathMix);
            Files.list(pathMix).forEach(FileUtils::deleteDirTree);

            String pathUnRel = savePath + separetor + "unknown";
            Path pathUn = FileUtils.createAbsolutePath(pathUnRel);
            System.out.println("Path salvataggio lingua unknown delete: " + pathUn);
            Files.list(pathUn).forEach(FileUtils::deleteDirTree);

            String pythonCommand = Utils.isWindows() ? "python" : "python3";
            String cmd = pythonCommand + " " + "detector.py";
            String toolPathRel = "risorse" + separetor + "GHLanguageDetection";
            Path toolPath = FileUtils.createAbsolutePath(toolPathRel);
            File dir = new File(toolPath.toString());
            System.out.println("Path tool: " + toolPath);

            Process process = Runtime.getRuntime().exec(cmd, null, dir);
            Applicazione.getInstance().getModello().addObject(Constants.PROCESS_LANGUAGE_DETECTION, process);

            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(process.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(process.getErrorStream()));

            // Read the output from the command

            String s;

            while ((s = stdInput.readLine()) != null) {
                if (s.contains("ERROR")){
                    System.out.println(s);
                    Applicazione.getInstance().getModello().addObject(Constants.MESSAGGIO_LANGUAGE_DETECTION,s);
                    return false;
                }
                System.out.println(s);
            }
            // Read any errors from the attempted command

            while ((s = stdError.readLine()) != null) {
                Applicazione.getInstance().getModello().addObject(Constants.MESSAGGIO_LANGUAGE_DETECTION,s);
                System.out.println(s);
                return false;
            }


        } catch (Exception ex) {
            Applicazione.getInstance().getCommonEvents().showExceptionDialog(ex);
            ex.printStackTrace();
        }
        return true;
    }


}
