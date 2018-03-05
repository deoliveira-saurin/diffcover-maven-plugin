package org.apache.maven.plugins.diffcover.diff;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugins.diffcover.Parameters;
import org.apache.maven.plugins.diffcover.diff.model.DiffBloc;
import org.apache.maven.plugins.diffcover.diff.model.DiffClass;
import org.apache.maven.plugins.diffcover.diff.model.DiffClasses;
import org.apache.maven.plugins.diffcover.diff.model.DiffState;
import org.apache.maven.plugins.diffcover.exception.DiffCoverFatalException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Slf4j
public class GitDiffEngine implements DiffEngine {

    @Override
    public DiffClasses analyse(Parameters parameters) {
        DiffClasses diffClasses = new DiffClasses();

        Repository repository = initGitRepository(parameters.getBasedir());
        DiffFormatter diffFormatter = initDiffFormatter(repository);
        List<DiffEntry> diffEntries = initGitCommand(repository);

        for (DiffEntry entry : diffEntries) {
            File file = new File(parameters.getBasedir(), entry.getNewPath());
            DiffClass diffClass = new DiffClass(file);
            EditList editList = getEditList(diffFormatter, entry);
            DiffState state = getDiffState(entry.getChangeType());
            for (Edit edit : editList) {
                DiffBloc diffBloc = new DiffBloc(edit.getBeginB(), edit.getEndB(), state);
                diffClass.addBloc(diffBloc);
            }
            diffClasses.addDiffClass(diffClass);
        }

        return diffClasses;
    }

    private DiffState getDiffState(DiffEntry.ChangeType changeType) {
        DiffState diffState = null;
        switch (changeType) {
            case ADD:
                diffState = DiffState.ADD;
                break;
            case COPY:
                diffState = DiffState.COPY;
                break;
            case DELETE:
                diffState = DiffState.DELETE;
                break;
            case MODIFY:
                diffState = DiffState.MODIFY;
                break;
            case RENAME:
                diffState = DiffState.RENAME;
                break;
        }
        return diffState;
    }

    private EditList getEditList(DiffFormatter diffFormatter, DiffEntry entry) {
        try {
            return diffFormatter.toFileHeader(entry).toEditList();
        } catch (IOException ioe) {
            throw new DiffCoverFatalException(ioe);
        }
    }

    private Repository initGitRepository(File basedir) {
        File gitFolder = new File(basedir, ".git");
        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();
        Repository repository;
        try {
            repository = repositoryBuilder.setGitDir(gitFolder)
                    //.readEnvironment()
                    //.findGitDir()
                    .setMustExist(true)
                    .build();
        } catch (IOException ioe) {
            String message = String.format("Impossible to initiate the repository with %s", gitFolder.getAbsolutePath());
            throw new DiffCoverFatalException(message, ioe);
        }
        return repository;
    }

    private List<DiffEntry> initGitCommand(Repository repository) {
        try {
            return new Git(repository).diff().setCached(true).call();
        } catch (GitAPIException gapie) {
            String message = "";
            throw new DiffCoverFatalException(message, gapie);
        }
    }

    private DiffFormatter initDiffFormatter(Repository repository) {
        DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
        df.setRepository(repository);
        //df.setDiffComparator(RawTextComparator.DEFAULT);
        //df.setDetectRenames(true);
        return df;
    }
}
