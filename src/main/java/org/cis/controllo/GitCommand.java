package org.cis.controllo;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

public class GitCommand {

    public LocalDate lastDateCommit(String cloneDirectory) throws IOException, GitAPIException {
        if (cloneDirectory == null || cloneDirectory.isEmpty()) {
            throw new IllegalArgumentException("Clone directory cannot be null or empty");
        }

        Path pathCloneDirectory = Paths.get(cloneDirectory);
        if (!FileUtils.exists(pathCloneDirectory)) {
            throw new IllegalStateException("Error, clone directory " + pathCloneDirectory + " not exists");
        }

        Git git = Git.open(new File(cloneDirectory));
        final List<Ref> branches = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
        final RevWalk revWalk = new RevWalk(git.getRepository());

        LocalDate localDate = branches.stream()
                                      .map(branch -> {
                                          try {
                                              return revWalk.parseCommit(branch.getObjectId());
                                          } catch (IOException e) {
                                              throw new RuntimeException(e);
                                          }
                                      })
                                      .sorted(Comparator.comparing((RevCommit commit) -> commit.getAuthorIdent().getWhen()).reversed())
                                      .findFirst()
                                      .map(revCommit -> LocalDate.ofInstant(revCommit.getAuthorIdent().getWhen().toInstant(), ZoneId.systemDefault()))
                                      .orElse(null);

        git.getRepository().close();
        git.close();
        return localDate;
    }

    /**
     *
     * @param cloneUrl
     * @param cloneDirectory - absolute path
     * @param token
     * @throws GitAPIException
     */
    public void cloneRepository(String cloneUrl, String cloneDirectory, String token, ProgressMonitor monitor) throws IllegalStateException, GitAPIException, IllegalArgumentException {
        // JGits commands are lazily evaluated to allow for configuration via method chaining.
        // The call of call() starts the execution of the command.
        // By default, JGit only clones the default branch.
        if (cloneUrl == null || cloneUrl.isEmpty()) {
            throw new IllegalArgumentException("Clone url cannot be null or empty");
        }

        if (cloneDirectory == null || cloneDirectory.isEmpty()) {
            throw new IllegalArgumentException("Clone directory cannot be null or empty");
        }

        Path pathCloneDirectory = Paths.get(cloneDirectory);
        if (FileUtils.exists(pathCloneDirectory)) {
            throw new IllegalStateException("Error, path " + pathCloneDirectory + " already exists");
        }

        CloneCommand cloneCommand = Git.cloneRepository();
        if (token != null && !token.isEmpty()) {
            cloneCommand = cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(token, ""));
        }

        if (monitor != null)
            cloneCommand.setProgressMonitor(monitor);

        Git git = null;
        try {
            git = cloneCommand.setURI(cloneUrl)
                            //.setBare(false)
                              .setDirectory(pathCloneDirectory.toFile())
                            //.setCloneAllBranches(true)
                            //.setNoCheckout(false)
                              .call();
        } catch (Exception e) {
            // Rollback.
            FileUtils.deleteDirTree(pathCloneDirectory);
            throw e;
        } finally {
            if (git != null) {
                git.getRepository().close();
                git.close();
            }
        }
    }
}
