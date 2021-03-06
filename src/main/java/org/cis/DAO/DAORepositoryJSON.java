package org.cis.DAO;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.cis.controllo.FileUtils;
import org.cis.modello.Repository;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DAORepositoryJSON implements IDAORepository {

    /**
     *  Upload all repositories from JSON files to the specified directory.
     *
     * @param directorySourceFiles directory that contains the JSON files.
     * @return list of repositories.
     */
    @Override
    public List<Repository> loadRepositories(String directorySourceFiles) {
        // .../folderX/folderDestination
        if (directorySourceFiles == null || directorySourceFiles.isEmpty()) {
            throw new IllegalArgumentException("directorySourceFiles cannot be null or empty");
        }
        try {
            return Files.list(Paths.get(directorySourceFiles))
                      //.collect(Collectors.toList())
                      //.parallelStream()
                      //.collect(Collectors.toList())
                      //.parallelStream()
                        .map(nameFile -> this.readRepositories(nameFile.toString()))
                        .flatMap(repository -> repository.stream())
                        .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Repository> readRepositories(String nameFile) {
        List<Repository> repositories = new ArrayList<>();
        JsonReader reader = null;
        try {
            reader = new JsonReader(new FileReader(nameFile));
            reader.beginObject();
            while (reader.hasNext()) {
                String attribute = reader.nextName();
                if (!attribute.equals("items")) {
                    reader.skipValue();
                    continue;
                }

                reader.beginArray();
                while (reader.hasNext()) {
                    repositories.add(readRepository(reader).setFile(nameFile));
                }
                reader.endArray();
            }
            reader.endObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return repositories;
    }

    private Repository readRepository(JsonReader reader) throws IOException {
        String id = null;
        String name = null;
        String htmlUrl = null;
        String description = null;
        String cloneUrl = null;
        int stars = 0;
        long size = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String attribute = reader.nextName();
            if (attribute.equals("id")) {
                id = reader.nextString();
            } else if (attribute.equals("name")) {
                name = reader.nextString();
            } else if (attribute.equals("html_url")) {
                htmlUrl = reader.nextString();
            } else if (attribute.equals("description")) {
                description = reader.nextString();
            } else if (attribute.equals("clone_url")) {
                cloneUrl = reader.nextString();
            } else if (attribute.equals("size")) {
                size = reader.nextLong();
            } else if(attribute.equals("stargazers_count")) {
                stars = reader.nextInt();
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Repository(id, name, description, htmlUrl,  cloneUrl, size, stars);
    }

    /**
     *  Save the repositories in JSON files in the specified directory.
     *  Each JSON file will contain at most 100 repositories.
     *
     * @param directoryDestinationFiles directory to save the repository JSON files.
     * @param repositories git repository to be saved in JSON file.
     */
    @Override
    public void saveRepositories(String directoryDestinationFiles, List<Repository> repositories) {
        // .../folderX/folderDestination
        if (directoryDestinationFiles == null || directoryDestinationFiles.isEmpty()) {
            throw new IllegalArgumentException("directorySourceFiles cannot be null or empty");
        }
        if (repositories == null) {
            throw new IllegalArgumentException("repositories cannot be null");
        }
        /*
            Example:
            External name: 2010-11-21T00-00-01_2013-12-14T00-00-01
            Internal name: 2010-11-21T00-00-01_2013-12-14T00-00-01_1.json -- 100 Repository.
            ...
            Internal name: 2010-11-21T00-00-01_2013-12-14T00-00-01_5.json -- 100 Repository.
            Internal name: 2010-11-21T00-00-01_2013-12-14T00-00-01_6.json -- 77 Repository.

            Each xxx_y.json will have a maximum of 100 repositories.
         */

        Function<Repository, String> classifierByExternalName = repository -> {
            return repository.getFile().substring(repository.getFile().lastIndexOf(FileUtils.PATH_SEPARATOR) + 1, repository.getFile().lastIndexOf("_"));
        };

        Function<Repository, String> classifierByInternalName = repository -> {
            return repository.getFile().substring(repository.getFile().lastIndexOf(FileUtils.PATH_SEPARATOR) + 1);
        };

        Map<String, Map<String, List<Repository>>> mapClassExternalName =
                repositories.stream().collect(Collectors.groupingBy(classifierByExternalName, Collectors.groupingBy(classifierByInternalName)));

        mapClassExternalName.forEach((externalName, mapClassInternalName) -> {
            // Total number of repositories for the date range (external name).
            int totalCount = getCountRepositoryForDateInterval(mapClassInternalName);

            int finalTotalCount = totalCount;
            mapClassInternalName.forEach((internalName, listRepository) -> {
                JsonWriter writer = null;
                try {
                    writer = new JsonWriter(new FileWriter(directoryDestinationFiles + FileUtils.PATH_SEPARATOR + internalName));
                    writer.setIndent("  ");

                    writer.beginObject();
                    writer.name("total_count").value(finalTotalCount);

                    writer.name("items");
                    writer.beginArray();

                    for (Repository repository: listRepository) {
                        saveRepository(writer, repository);
                    }

                    writer.endArray();

                    writer.endObject();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        });
    }

    private void saveRepository(JsonWriter writer, Repository repository) throws IOException {
        writer.beginObject();
        writer.name("id").value(repository.getId());
        writer.name("name").value(repository.getName());
        writer.name("html_url").value(repository.getUrlProject());
        writer.name("description").value(repository.getDescription());
        writer.name("last_committed_date").value(repository.getDataProperty().get());
        writer.name("clone_url").value(repository.getCloneUrl());
        writer.name("language").value(repository.getLanguageProperty());
        writer.name("programming_language").value(repository.getProgrammingLanguageProperty());
        writer.name("size").value(repository.getSize());
        writer.name("stargazers_count").value(repository.getStars());
        writer.endObject();
    }

    private int getCountRepositoryForDateInterval(Map<String, List<Repository>> mapClassInternalName) {
        int totalCount = 0;
        for (List<Repository> entryList : mapClassInternalName.values()) {
            totalCount = totalCount + entryList.size();
        }
        return totalCount;
    }

}
