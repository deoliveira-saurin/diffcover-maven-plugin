/**
 *
 */
package org.apache.maven.plugins.diffcover.main;

import org.apache.maven.plugins.diffcover.Parameters;
import org.apache.maven.plugins.diffcover.cover.JacocoCoverEngine;

import java.io.File;

/**
 * @author U00I168
 */
public class CoverTestMain {

    public static void main(String[] args) throws Exception {
        JacocoCoverEngine jacocoCoverEngine = new JacocoCoverEngine();
        File jacocoFile = new File("target/jacoco.exec");
        File classesDir = new File("target/classes/");
        File sourcesDir = new File("src/main/java/");
        Parameters parameters = new Parameters();
        parameters.setClassesDir(classesDir);
        parameters.setJacocoFile(jacocoFile);
        parameters.setSourcesDir(sourcesDir);
        jacocoCoverEngine.analyse(parameters);
    }
}