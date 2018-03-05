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

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.plugins.diffcover.DiffCoverEngine;
import org.apache.maven.plugins.diffcover.Parameters;

import java.io.File;

/**
 * Goal which touches a timestamp file.
 */
@Slf4j
@Mojo(name = "touch", defaultPhase = LifecyclePhase.TEST, threadSafe = true,
        requiresDependencyResolution = ResolutionScope.TEST)
public class DiffCoverPlugin
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

    @Parameter(defaultValue = "false", property = "failOnDiffUncover")
    private Boolean failOnDiffUncovered;

    @Override
    public void execute()
            throws MojoExecutionException {

        Parameters parameters = new Parameters();
        parameters.setBasedir(baseDirectory);
        parameters.setSourcesDir(sourcesDirectory);
        parameters.setJacocoFile(jacocoFile);
        parameters.setClassesDir(classesDirectory);
        log.info("Parameters {}", parameters);

        DiffCoverEngine diffCoverEngine = new DiffCoverEngine();
        Boolean ok = diffCoverEngine.analyse(parameters);

        if (failOnDiffUncovered && !ok) {
            throw new MojoExecutionException("Some modified codes aren't covered by unit tests.");
        }
    }
}
