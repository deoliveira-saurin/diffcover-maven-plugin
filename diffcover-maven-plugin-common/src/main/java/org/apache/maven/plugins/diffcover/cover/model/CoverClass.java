package org.apache.maven.plugins.diffcover.cover.model;

import lombok.ToString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A cover class contents informations on the class, source and cover state.
 */
@ToString
public class CoverClass {

    List<CoverLine> lines = new ArrayList<>();
    private File javaFile;
    private CoverState state = CoverState.IGNORED;

    public CoverClass(File javaFile) {
        this.javaFile = javaFile;
    }

    public void addLine(int number, String content, CoverState lineState) {
        CoverLine coverLine = new CoverLine(number, content, state);
        addLine(coverLine);
    }

    public void addLine(CoverLine coverLine) {
        lines.add(coverLine);
        // the code below is false ...
        switch (coverLine.getState()) {
            case IGNORED:
                break;
            case NOT_COVERED:
                state = CoverState.NOT_COVERED;
                break;
            case PARTIAL_COVERED:
                state = CoverState.PARTIAL_COVERED;
                break;
            case COVERED:
                if (state != CoverState.COVERED) state = CoverState.PARTIAL_COVERED;
                break;
        }
    }

    public File getJavaFile() {
        return javaFile;
    }

    public List<CoverLine> getLines() {
        return lines;
    }
}
