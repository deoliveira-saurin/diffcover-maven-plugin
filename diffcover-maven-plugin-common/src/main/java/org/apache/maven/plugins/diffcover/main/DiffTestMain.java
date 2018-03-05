package org.apache.maven.plugins.diffcover.main;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.Edit;
import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.patch.FileHeader;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.util.List;

public class DiffTestMain {

    public static void main(String[] args) throws Exception {
        File gitFolder = new File("../maven-diffcover-test/.git");
        //File gitFolder = new File(".git");

        FileRepositoryBuilder repositoryBuilder = new FileRepositoryBuilder();

        Repository repository = repositoryBuilder.setGitDir(gitFolder)
                //.readEnvironment()
                //.findGitDir()
                //.setMustExist(true)
                .build();

        DiffFormatter df = new DiffFormatter(System.out); //DisabledOutputStream.INSTANCE);
        df.setRepository(repository);
//        df.setDiffComparator(RawTextComparator.DEFAULT);
//        df.setDiffAlgorithm(new HistogramDiff());
//        df.setDetectRenames(true);

        Git git = new Git(repository);
        List<DiffEntry> diffEntries = git.diff().setCached(true).call();

        for (DiffEntry entry : diffEntries) {
            System.out.println(entry.getChangeType());
            System.out.println(entry.getNewPath());
            System.out.println(entry.getNewMode());
            String path = entry.getNewPath();
            String baseDir = "src/main/java/";
            if (path.startsWith(baseDir))
                System.out.println(path.substring(baseDir.length()));
            //df.format(entry);

            FileHeader fh = df.toFileHeader(entry);
            EditList editList = fh.toEditList();
            for (Edit edit : editList) {
                System.out.println(edit);
            }
        }
    }
}
