package org.apache.maven.plugins.diffcover.maven.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.commons.lang3.builder.Diff;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.sisu.Parameters;
import org.jacoco.core.analysis.CoverageBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "touch", defaultPhase = LifecyclePhase.TEST, threadSafe = true,
        requiresDependencyResolution = ResolutionScope.TEST)
public class MyMojo
        extends AbstractMojo {

    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir")
    private File outputDirectory;

    @Parameter(defaultValue = "${project.build.directory}/jacoco.exec", property = "jacocoFile")
    private File jacocoFile;

    @Parameter(defaultValue = "${project.build.sourceDirectory}", property = "sourcesDir")
    private File sourcesDirectory;

    /**
     * The directory containing generated classes of the project being tested.</br>
     * This will be included after the test classes in the test classpath.
     */
    @Parameter(defaultValue = "${project.build.outputDirectory}", property = "classesDir")
    private File classesDirectory;

    @Parameter(defaultValue = "${project.basedir}", property = "baseDir")
    private File baseDirectory;

    @Override
    public void execute()
            throws MojoExecutionException {

        try {
            File gitFolder = new File(baseDirectory, ".git");
            List<DiffEntry> diffs = DiffGetter.getDiffs(gitFolder);
            CoverEngine coverEngine = new CoverEngine();
            CoverageBuilder coverageBuilder =
                    coverEngine.createCoverageBuilder(jacocoFile, classesDirectory);
            coverEngine.analyse(coverageBuilder, sourcesDirectory);
        } catch (IOException e) {
            throw new MojoExecutionException("Error during analyse cover", e);
        } catch (Exception e) {
            throw new MojoExecutionException("Error during Diff retrieving", e);
        }

        File f = outputDirectory;

        if (!f.exists()) {
            boolean mkdirs = f.mkdirs();
            if (!mkdirs) {
                throw new MojoExecutionException("Error creating dir " + f.getAbsolutePath());
            }
        }

        File touch = new File(f, "touch.txt");

        try (FileWriter w = new FileWriter(touch)) {
            w.write("touch.txt");
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + touch, e);
        }
    }
}
