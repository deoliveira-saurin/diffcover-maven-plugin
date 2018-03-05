package org.apache.maven.plugins.diffcover.cover.model;

import lombok.ToString;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@ToString
public class CoverClasses {
    Map<File, CoverClass> coverClasses = new HashMap<>();

    public void addCoverClass(CoverClass coverClass) {
        coverClasses.put(coverClass.getJavaFile(), coverClass);
    }

    public CoverClass get(File javaFile) {
        return coverClasses.get(javaFile);
    }
}
