package org.apache.maven.plugins.diffcover.cover;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugins.diffcover.Parameters;
import org.apache.maven.plugins.diffcover.cover.model.CoverClass;
import org.apache.maven.plugins.diffcover.cover.model.CoverClasses;
import org.apache.maven.plugins.diffcover.cover.model.CoverLine;
import org.apache.maven.plugins.diffcover.cover.model.CoverState;
import org.apache.maven.plugins.diffcover.exception.DiffCoverFatalException;
import org.jacoco.core.analysis.Analyzer;
import org.jacoco.core.analysis.CoverageBuilder;
import org.jacoco.core.analysis.ILine;
import org.jacoco.core.analysis.ISourceFileCoverage;
import org.jacoco.core.tools.ExecFileLoader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class JacocoCoverEngine implements CoverEngine {

    public CoverClasses analyse(Parameters parameters) {
        CoverClasses coverClasses = new CoverClasses();

        CoverageBuilder coverageBuilder = initCoverageBuilder(parameters);

        for (ISourceFileCoverage sourceFileCoverage : coverageBuilder.getSourceFiles()) {
            String name = sourceFileCoverage.getName();
            String pack = sourceFileCoverage.getPackageName();

            File packageFile = new File(parameters.getSourcesDir(), pack);
            File javaFile = new File(packageFile, name);
            CoverClass coverClass = new CoverClass(javaFile);
            log.info("Add CoverClass {}", coverClass);

            try {
                List<String> lines = Files.readAllLines(Paths.get(javaFile.getAbsolutePath()), StandardCharsets.UTF_8);
                for (int index = 0; index < lines.size(); index++) {
                    ILine iline = sourceFileCoverage.getLine(index + 1);
                    String line = lines.get(index);
                    CoverState coverState = CoverState.valueOf(iline.getStatus());
                    CoverLine coverLine = new CoverLine(index + 1, line, coverState);
                    coverClass.addLine(coverLine);
                }
            } catch (IOException ioe) {
                String message = String.format("Impossible to read sourcefile '%s'", javaFile.getAbsolutePath());
                throw new DiffCoverFatalException(message, ioe);
            }
            coverClasses.addCoverClass(coverClass);
        }

        return coverClasses;
    }

    private CoverageBuilder initCoverageBuilder(Parameters parameters) {
        CoverageBuilder coverageBuilder = new CoverageBuilder();
        ExecFileLoader execFileLoader = new ExecFileLoader();
        try {
            execFileLoader.load(parameters.getJacocoFile());
        } catch (IOException ioe) {
            String message = String.format("Impossible to read file '%s'", parameters.getJacocoFile().getPath());
            throw new DiffCoverFatalException(message, ioe);
        }
        Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);
        try {
            analyzer.analyzeAll(parameters.getClassesDir());
        } catch (IOException ioe) {
            String message = String.format("Impossible to read files in directory '%s'", parameters.getClassesDir());
            throw new DiffCoverFatalException(message, ioe);
        }
        return coverageBuilder;
    }
}