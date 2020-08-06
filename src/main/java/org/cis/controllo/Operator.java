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
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class Operator {



    public static ObservableList<Repository> searchByName(ObservableList<Repository> list, String daCercare, String parametro, boolean strict, String percent) {
        ObservableList<Repository> risultato = FXCollections.observableArrayList();
        if(list == null) {
            return risultato;
        }
        for(Repository repo : list) {
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

    public static boolean cacheRepositoriesIsEmpty() throws IOException {
        Path pathCloneDirectory = FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_CLONING_DIRECTORY);
        return FileUtils.isDirEmpty(pathCloneDirectory);
    }

    public static boolean confrontaElemConParametriStrict(Repository repo, String daCercare, String parametro, String percent) {
        daCercare = daCercare.trim();
        if (daCercare.isEmpty()) {
            return true;
        }
        if (parametro.equals(Constants.PARAM_LANGUAGE_GREATER)) {
            Map<String, RepositoryLanguage> repositoryLanguageMap =
                    (Map<String, RepositoryLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_LANGUAGE);
            RepositoryLanguage  repositoryLanguage = repositoryLanguageMap.get(repo.getId());
            double percentage = 0;
            if(!percent.isEmpty()) {
                percentage = Double.valueOf(percent);
            }
            double lanuguagePercentage = 100;
            if(repositoryLanguage.getDetection1() != null) {
                lanuguagePercentage = repositoryLanguage.getDetection1().getPercentage();
            }
            if(repositoryLanguage != null && repositoryLanguage.getLanguage().equalsIgnoreCase(daCercare) && percent.isEmpty()) {
                System.out.println(repositoryLanguage.toString());
                return true;
            } else if(repositoryLanguage != null && repositoryLanguage.getLanguage().equalsIgnoreCase(daCercare) && lanuguagePercentage >= percentage) {
                return true;
            }
        } else if(parametro.equals(Constants.PARAM_LANGUAGE_SMALLER)) {
            Map<String, RepositoryLanguage> repositoryLanguageMap =
                    (Map<String, RepositoryLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_LANGUAGE);
            RepositoryLanguage  repositoryLanguage = repositoryLanguageMap.get(repo.getId());
            double percentage = 0;
            if(!percent.isEmpty()) {
                percentage = Double.valueOf(percent);
            }
            double lanuguagePercentage = 100;
            if(repositoryLanguage.getDetection1() != null) {
                lanuguagePercentage = repositoryLanguage.getDetection1().getPercentage();
            }
            if(repositoryLanguage != null && repositoryLanguage.getLanguage().equalsIgnoreCase(daCercare) && percent.isEmpty()) {
                System.out.println(repositoryLanguage.toString());
                return true;
            } else if(repositoryLanguage != null && repositoryLanguage.getLanguage().equalsIgnoreCase(daCercare) && lanuguagePercentage < percentage) {
                return true;
            }
        } else if (parametro.equals(Constants.PARAM_REPOSITORIES)) {
            if (repo.getName().equalsIgnoreCase(daCercare)) {
                if (repo.getLanguageProperty() != null && repo.getLanguageProperty().toLowerCase().equals(daCercare.toLowerCase().trim())) {
                    return true;
                }
            }
        } else if (parametro.equals(Constants.PARAM_PROGR_LANGUAGE_GREATER)) {
            Map<String, StatisticsProgrammingLanguage> languageProgrammingMap =
                    (Map<String, StatisticsProgrammingLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_PROGRAMMING_LANGUAGE);
            StatisticsProgrammingLanguage statisticsProgrammingLanguage = languageProgrammingMap.get(repo.getId());
            String finalDaCercare = daCercare;

            double percentDouble;
            boolean predicatePercentage = true;
            try {
                percentDouble = Double.parseDouble(percent);
                predicatePercentage = statisticsProgrammingLanguage.getPercentage() == percentDouble;
            } catch (NumberFormatException e) {}

            if(statisticsProgrammingLanguage != null
                    && statisticsProgrammingLanguage.existsProgrammingLanguage(language -> language.toLowerCase().contains(finalDaCercare.toLowerCase().trim()))
                    && predicatePercentage) {
                return true;
            }
        } else if(parametro.equals(Constants.PARAM_PROGR_LANGUAGE_SMALLER)) {
            Map<String, StatisticsProgrammingLanguage> languageProgrammingMap =
                    (Map<String, StatisticsProgrammingLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_PROGRAMMING_LANGUAGE);
            StatisticsProgrammingLanguage statisticsProgrammingLanguage = languageProgrammingMap.get(repo.getId());
            String finalDaCercare = daCercare;

            double percentDouble;
            boolean predicatePercentage = true;
            try {
                percentDouble = Double.parseDouble(percent);
                predicatePercentage = statisticsProgrammingLanguage.getPercentage() == percentDouble;
            } catch (NumberFormatException e) {}

            if(statisticsProgrammingLanguage != null
                    && statisticsProgrammingLanguage.existsProgrammingLanguage(language -> language.toLowerCase().contains(finalDaCercare.toLowerCase().trim()))
                    && predicatePercentage) {
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

                if (sorter.compare(dimensione, daCercare) == 0) {
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

                if (sorter.compare(stars, daCercare) == 0) {
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
        if(parametro.equals(Constants.PARAM_LANGUAGE_GREATER)) {
            Map<String, RepositoryLanguage> repositoryLanguageMap =
                    (Map<String, RepositoryLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_LANGUAGE);
            RepositoryLanguage  repositoryLanguage = repositoryLanguageMap.get(repo.getId());
            double percentage = 0;
            if(!percent.isEmpty()) {
                percentage = Double.valueOf(percent);
            }
            double lanuguagePercentage = 100;
            if(repositoryLanguage.getDetection1() != null) {
                lanuguagePercentage = repositoryLanguage.getDetection1().getPercentage();
            }
            if(repositoryLanguage != null && repositoryLanguage.getLanguage().toLowerCase().contains(daCercare.toLowerCase()) && percent.isEmpty()) {
                System.out.println(repositoryLanguage.toString());
                return true;
            } else if(repositoryLanguage != null && repositoryLanguage.getLanguage().toLowerCase().contains(daCercare.toLowerCase()) && lanuguagePercentage >= percentage) {
                return true;
            }
        } else if(parametro.equals(Constants.PARAM_LANGUAGE_SMALLER)) {
            Map<String, RepositoryLanguage> repositoryLanguageMap =
                    (Map<String, RepositoryLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_LANGUAGE);
            RepositoryLanguage  repositoryLanguage = repositoryLanguageMap.get(repo.getId());
            double percentage = 0;
            if(!percent.isEmpty()) {
                percentage = Double.valueOf(percent);
            }
            double lanuguagePercentage = 100;
            if(repositoryLanguage.getDetection1() != null) {
                lanuguagePercentage = repositoryLanguage.getDetection1().getPercentage();
            }
            if(repositoryLanguage != null && repositoryLanguage.getLanguage().toLowerCase().contains(daCercare.toLowerCase()) && percent.isEmpty()) {
                System.out.println(repositoryLanguage.toString());
                return true;
            } else if(repositoryLanguage != null && repositoryLanguage.getLanguage().toLowerCase().contains(daCercare.toLowerCase()) && lanuguagePercentage < percentage) {
                return true;
            }
        } else if(parametro.equals(Constants.PARAM_PROGR_LANGUAGE_GREATER)) {
            Map<String, StatisticsProgrammingLanguage> languageProgrammingMap =
                    (Map<String, StatisticsProgrammingLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_PROGRAMMING_LANGUAGE);
            StatisticsProgrammingLanguage statisticsProgrammingLanguage = languageProgrammingMap.get(repo.getId());
            String finalDaCercare = daCercare;

            double percentDouble;
            boolean predicatePercentage = true;
            try {
                percentDouble = Double.parseDouble(percent);
                predicatePercentage = statisticsProgrammingLanguage.getPercentage() >= percentDouble;
            } catch (NumberFormatException e) {}

            if(statisticsProgrammingLanguage != null
                    && statisticsProgrammingLanguage.existsProgrammingLanguage(language -> language.toLowerCase().contains(finalDaCercare.toLowerCase().trim()))
                    && predicatePercentage) {
                return true;
            }
        } else if(parametro.equals(Constants.PARAM_PROGR_LANGUAGE_SMALLER)) {
            Map<String, StatisticsProgrammingLanguage> languageProgrammingMap =
                    (Map<String, StatisticsProgrammingLanguage>) Applicazione.getInstance().getModello().getObject(Constants.MAP_REPOSITORY_PROGRAMMING_LANGUAGE);
            StatisticsProgrammingLanguage statisticsProgrammingLanguage = languageProgrammingMap.get(repo.getId());
            String finalDaCercare = daCercare;

            double percentDouble;
            boolean predicatePercentage = true;
            try {
                percentDouble = Double.parseDouble(percent);
                predicatePercentage = statisticsProgrammingLanguage.getPercentage() < percentDouble;
            } catch (NumberFormatException e) {}

            if(statisticsProgrammingLanguage != null
                    && statisticsProgrammingLanguage.existsProgrammingLanguage(language -> language.toLowerCase().contains(finalDaCercare.toLowerCase().trim()))
                    && predicatePercentage) {
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

        System.out.println("Start Creating File Properties!");

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
                write.println("q" + j + "=" + q.getKey().trim()+":"+ q.getValue().trim());
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
            Platform.runLater(() -> Applicazione.getInstance().getCommonEvents().showExceptionDialog(io));
            io.printStackTrace();
        }


    }


    public static boolean startGHRepoSearcher(){
        // Checking the connection
        CommonEvents commonEvents = Applicazione.getInstance().getCommonEvents();
        System.out.println("Checking internet connection!");

        boolean connect = netIsAvailable();
        if(connect == false){
            Applicazione.getInstance().getModello().addObject(Constants.MESSAGE_END_SEARCH,"Connection error!");
            return false ;
        }

        System.out.println("Launching GHRepoSearcher...");
        Platform.runLater(() -> commonEvents.setProgressBar("Launching GHRepoSearcher...", 1));


        BufferedReader stdInput = null;
        BufferedReader stdError = null;
        try {
            Path path = FileUtils.createAbsolutePath(Constants.GHREPO_SEARCHER_JAR);
            System.out.println(path.toString());

            File dir = new File(path.toString());

            // Cleaning up JSON folder...
            Platform.runLater(() -> commonEvents.setProgressBar("Cleaning up JSON folder...", 1));

            Path pathJson = FileUtils.createAbsolutePath(Constants.RELATIVE_PATH_JSON);
            System.out.println(pathJson.toString());

            File dir2 = new File(pathJson.toString());
            File[] files = dir2.listFiles();
            int i = 0;
            for (File f : files) {
                f.delete();
                if(i == Math.floor(files.length / 2)) {
                    Platform.runLater(() -> commonEvents.setProgressBar("We're half way through the cleaning process and I'm not gonna stop! :)", 2));
                }
                i++;
            }

            Platform.runLater(() -> commonEvents.setProgressBar("Cleaning up terminated!", 3));

            // Sect in command
            String cmd = "java -jar GHRepoSearcher.jar config.properties";
            Process process = Runtime.getRuntime().exec(cmd, null, dir);
            Applicazione.getInstance().getModello().addObject(Constants.THREAD_REPO_SEARCHER, process);

            Platform.runLater(() -> commonEvents.setProgressBar("Working behind the scenes...", 3));

            stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // Read the output from the command
            String s;
            while ((s = stdInput.readLine()) != null) {
                if (s.contains("ERROR")){
                    System.out.println(s);
                    Applicazione.getInstance().getModello().addObject(Constants.MESSAGE_END_SEARCH,"Invalid Token");
                    return false;
                }
                System.out.println(s);
            }

            // Read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                Applicazione.getInstance().getModello().addObject(Constants.MESSAGE_END_SEARCH,s);
                System.out.println(s);
                return false;
            }

        } catch (IOException ex) {}
        finally {
            if (stdInput != null) {
                try {
                    stdInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (stdError != null) {
                try {
                    stdError.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    public static boolean actionDetectIdiom() {
        System.out.println("Start Process language detection");

        BufferedReader stdInput = null;
        BufferedReader stdError = null;
        try {
            String pythonCommand = Utils.isWindows() ? "python" : "python3";
            String cmd = pythonCommand + " " + "detector.py";

            Path toolPath = FileUtils.createAbsolutePath(Constants.TOOL_LANGUAGE_DETECTION);
            File dir = new File(toolPath.toString());
            System.out.println("Path tool: " + toolPath);

            Process process = Runtime.getRuntime().exec(cmd, null, dir);
            Applicazione.getInstance().getModello().addObject(Constants.PROCESS_LANGUAGE_DETECTION, process);

            stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            // Read the output from the command
            String s;
            while ((s = stdInput.readLine()) != null) {
                if (s.contains("ERROR")){
                    System.out.println(s);
                    Applicazione.getInstance().getModello().addObject(Constants.MESSAGE_LANGUAGE_DETECTION, s);
                    return false;
                }
                System.out.println(s);
            }

            // Read any errors from the attempted command
            while ((s = stdError.readLine()) != null) {
                Applicazione.getInstance().getModello().addObject(Constants.MESSAGE_LANGUAGE_DETECTION, s);
                System.out.println(s);
                return false;
            }

        } catch (Exception ex) {
            Platform.runLater(() -> Applicazione.getInstance().getCommonEvents().showExceptionDialog(ex));
            ex.printStackTrace();
        } finally {
            if (stdInput != null) {
                try {
                    stdInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (stdError != null) {
                try {
                    stdError.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }


}
