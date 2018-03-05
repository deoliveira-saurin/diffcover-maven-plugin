package org.apache.maven.plugins.diffcover.diff.model;

import lombok.ToString;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@ToString
public class DiffClasses {
    Map<File, DiffClass> diffClasses = new HashMap<>();

    public void addDiffClass(DiffClass diffClass) {
        diffClasses.put(diffClass.getFile(), diffClass);
    }

    public Collection<DiffClass> values() {
        return diffClasses.values();
    }
}
