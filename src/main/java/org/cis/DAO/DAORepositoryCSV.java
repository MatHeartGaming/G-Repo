package org.cis.DAO;

import org.cis.controllo.FileUtils;
import org.cis.modello.Repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class DAORepositoryCSV implements IDAORepository{

    @Override
    public List<Repository> loadRepositories(String nameFile) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void saveRepositories(String directoryFiles, List<Repository> repositories) {
        // Use saveRepositories(Path pathFile, List<Repository> repositories, String[] header, BiFunction<Repository, Integer, String> stringFunction).
        throw new UnsupportedOperationException("Not implemented");
    }

    public void saveRepositories(Path pathFile, List<Repository> repositories, String[] header, BiFunction<Repository, Integer, String> stringFunction) {
        if (pathFile == null) throw new IllegalArgumentException("The pathFile argument cannot be null");
        if (repositories == null) throw new IllegalArgumentException("The repositories argument cannot be null");

        if (stringFunction == null) throw new IllegalArgumentException("The stringFunction callback cannot be null");

        try (BufferedWriter br = Files.newBufferedWriter(pathFile)) {
            // Header.
            br.write(String.join(",", header));
            br.newLine();
            // Data.
            for (int i = 0; i < repositories.size(); i++) {
                String line = stringFunction.apply(repositories.get(i), i);
                br.write(line);
                br.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean updateRepositories(Path pathFile, Consumer<String> stringConsumer) {
        if (pathFile == null) throw new IllegalArgumentException("The pathFile argument cannot be null");
        if (!FileUtils.exists(pathFile)) throw new IllegalStateException("The path " + pathFile + " from does not exist");

        if (stringConsumer == null) throw new IllegalArgumentException("The stringConsumer callback cannot be null");

        try (BufferedReader br = Files.newBufferedReader(pathFile)) {
            // Header.
            String header = br.readLine();
            // Data.
            String line;
            while ((line = br.readLine()) != null) {
                stringConsumer.accept(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
