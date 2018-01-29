/**
 *
 */
package org.apache.maven.plugins.diffcover.maven.plugin;

import org.jacoco.core.analysis.CoverageBuilder;

import java.io.File;

/**
 * @author U00I168
 */
public class MainLoadExec {
    public static void main(String[] args) throws Exception {
        CoverEngine coverEngine = new CoverEngine();
        File jacocoFile = new File("target/jacoco.exec");
        File classesDir = new File("target/classes/");
        File sourcesDir = new File("src/main/java/");
        CoverageBuilder coverageBuilder = coverEngine.createCoverageBuilder(jacocoFile , classesDir);
        coverEngine.analyse(coverageBuilder, sourcesDir);
    }
}