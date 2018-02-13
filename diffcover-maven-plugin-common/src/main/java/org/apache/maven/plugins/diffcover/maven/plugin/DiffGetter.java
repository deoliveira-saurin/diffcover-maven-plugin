package org.apache.maven.plugins.diffcover.maven.plugin;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class DiffGetter {


    public static List<DiffEntry> getDiffs(File gitFolder) throws Exception {
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        try {
            Repository repository = repositoryBuilder.setGitDir(gitFolder)
                    .readEnvironment()
                    .findGitDir()
                    .setMustExist(true)
                    .build();
            Git git = new Git(repository);
            return git.diff().setOutputStream( System.out ).call();
        } catch (IOException | GitAPIException e) {
            throw new Exception("Impossible to get git directory : ", e);
        }
    }
}
