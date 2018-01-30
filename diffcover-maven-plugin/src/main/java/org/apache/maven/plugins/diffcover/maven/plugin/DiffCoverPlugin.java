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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.jacoco.core.analysis.CoverageBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Goal which touches a timestamp file.
 */
@Mojo(name = "verify", defaultPhase = LifecyclePhase.TEST, threadSafe = true,
        requiresDependencyResolution = ResolutionScope.TEST)
public class DiffCoverPlugin
        extends AbstractMojo {

    /**
     * Read-only parameter with value of Maven property <i>project.build.directory</i>.
     */
    @Parameter(defaultValue = "${project.build.directory}", property = "outputDir", readonly = true)
    private File outputDirectory;

    /**
     * The location of the jacoco file (result of goal prepare-agent of jacoco plugin).
     */
    @Parameter(defaultValue = "${project.build.directory}/jacoco.exec", property = "jacocoFile")
    private File jacocoFile;

    /**
     * .....
     */
    @Parameter(defaultValue = "${project.build.sourceDirectory}", property = "sourcesDir")
    private File sourcesDirectory;

    /**
     * The directory containing generated classes of the project being tested.</br>
     * This will be included after the test classes in the test classpath.
     */
    @Parameter(defaultValue = "${project.build.outputDirectory}", property = "classesDir", readonly = true)
    private File classesDirectory;

    /**
     * A list of &lt;exclude&gt; elements specifying the source (by pattern) that should be excluded in analysing.
     */
    @Parameter(defaultValue = "")
    private String[] excludes;

    /**
     * A list of &lt;include&gt; elements specifying the source (by pattern) that should be included in analysing.
     */
    @Parameter(defaultValue = "")
    private String[] includes;

    /**
     * Set this to "true" to cause a failure if uncovered sources by tests has been modified. Defaults to "false".</br>
     * By defaults, each diff uncovered generate a log warn in console.
     */
    @Parameter(defaultValue = "false")
    private Boolean failIfUncoveredDiff;

    @Override
    public void execute()
            throws MojoExecutionException {

        try {
            CoverEngine coverEngine = new CoverEngine();
            CoverageBuilder coverageBuilder =
                    coverEngine.createCoverageBuilder(jacocoFile, classesDirectory);
            coverEngine.analyse(coverageBuilder, sourcesDirectory);
        } catch (IOException e) {
            throw new MojoExecutionException("Error during analyse cover", e);
        }

        File f = outputDirectory;

        if (!f.exists()) {
            boolean mkdirs = f.mkdirs();
            if (!mkdirs) {
                throw new MojoExecutionException("Error creating dir " + f.getAbsolutePath());
            }
        }

        File touch = new File(f, "touch.txt");

        try (FileWriter w = new FileWriter(touch)){
            w.write("touch.txt");
        } catch (IOException e) {
            throw new MojoExecutionException("Error creating file " + touch, e);
        }
    }
}
