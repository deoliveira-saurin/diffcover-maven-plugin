package org.apache.maven.plugins.diffcover;

import lombok.ToString;

import java.io.File;

/**
 * All parameters used by the plugin
 */
@ToString
public class Parameters {

    private File sourcesDir;

    private File classesDir;

    private File jacocoFile;

    private File basedir;

    public File getClassesDir() {
        return classesDir;
    }

    public void setClassesDir(File classesDir) {
        this.classesDir = classesDir;
    }

    public File getJacocoFile() {
        return jacocoFile;
    }

    public void setJacocoFile(File jacocoFile) {
        this.jacocoFile = jacocoFile;
    }

    public File getSourcesDir() {
        return sourcesDir;
    }

    public void setSourcesDir(File sourcesDir) {
        this.sourcesDir = sourcesDir;
    }

    public File getBasedir() {
        return basedir;
    }

    public void setBasedir(File basedir) {
        this.basedir = basedir;
    }
}
