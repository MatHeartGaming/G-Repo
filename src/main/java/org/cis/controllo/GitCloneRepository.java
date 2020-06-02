package org.cis.controllo;

import org.cis.modello.Repository;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.util.List;

public class GitCloneRepository {


    /**
     * Clone all the git repositories.
     *
     * @param repositories list of repositories to clone.
     * @param directoryDestination directory where all git repositories will be saved.
     * @param token user access GitHub token.
     */
    public void cloneRepositories (List<Repository> repositories, String directoryDestination, String token) {
        if (repositories == null || repositories.isEmpty()) {
            throw new IllegalArgumentException("The list cannot be null or empty");
        }

        try {
            for (Repository repository: repositories) {
                String cloneDirectory = directoryDestination + "/" + repository.getName();
                repository.setCloneDirectory(cloneDirectory);
                cloneRepository(repository.getCloneUrl(), cloneDirectory, token);

            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
    }

    private void cloneRepository(String cloneUrl, String cloneDirectory, String token) throws GitAPIException {
        //todo: verificare se il repository corrente è già stato clonato. (verificare se già esiste la cloneDirectory del repo)
        CloneCommand cloneCommand = Git.cloneRepository();
        if (!token.isEmpty()) {
            cloneCommand = cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""));
        }

        cloneCommand.setURI(cloneUrl)
                    .setBare(false)
                    .setDirectory(new File(cloneDirectory))
                    .setCloneAllBranches(true)
                    .setNoCheckout(false)
                    .call();
    }
}
