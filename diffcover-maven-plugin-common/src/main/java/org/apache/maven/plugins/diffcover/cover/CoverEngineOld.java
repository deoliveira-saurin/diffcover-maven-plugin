package org.apache.maven.plugins.diffcover.cover;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import org.apache.maven.plugins.diffcover.cover.model.CoverState;
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

public class CoverEngineOld {
    public CoverageBuilder createCoverageBuilder(File jacocoFile, File classesDir) throws IOException {
        ExecFileLoader execFileLoader = new ExecFileLoader();
        execFileLoader.load(jacocoFile);
        CoverageBuilder coverageBuilder = new CoverageBuilder();
        Analyzer analyzer = new Analyzer(execFileLoader.getExecutionDataStore(), coverageBuilder);
        analyzer.analyzeAll(classesDir);
        return coverageBuilder;
    }

    public void analyse(CoverageBuilder coverageBuilder, File sourcesDir) throws IOException {
        for (ISourceFileCoverage sourceFileCoverage : coverageBuilder.getSourceFiles()) {
            String name = sourceFileCoverage.getName();
            String pack = sourceFileCoverage.getPackageName();
            File file = new File(sourcesDir.getAbsolutePath() + File.separator + pack + File.separator + name);

            List<String> lines = Files.readAllLines(Paths.get(file.getAbsolutePath()), StandardCharsets.UTF_8);
            for (int index = 0; index < lines.size(); index++) {
                ILine coverLine = sourceFileCoverage.getLine(index + 1);
                String line = lines.get(index);
                System.out.println(index + 1 + " " + CoverState.valueOf(coverLine.getStatus()).getShortName() + " : " + line);
            }

            CompilationUnit unit = JavaParser.parse(file);
            for (MethodDeclaration methodDeclaration : unit.findAll(MethodDeclaration.class)) {
                System.out.println(methodDeclaration.getName() + " : " + methodDeclaration.getBegin() + "-" + methodDeclaration.getEnd());
                if (methodDeclaration.getBegin().isPresent() && methodDeclaration.getBegin().isPresent()) {
                    int begin = methodDeclaration.getBegin().get().line;
                    int end = methodDeclaration.getEnd().get().line;
                    for (int index = begin - 1; index < end; index++) {
                        ILine coverLine = sourceFileCoverage.getLine(index + 1);
                        String line = lines.get(index);
                        if (line.trim().length() != 0) {
                            System.out.println(index + 1 + " " + CoverState.valueOf(coverLine.getStatus()).getShortName() + " : " + line);
                        }
                    }
                }
            }
        }
    }


}