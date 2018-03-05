package org.apache.maven.plugins.diffcover.diff.model;

import lombok.ToString;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A cover class contents informations on the class, source and cover state.
 */
@ToString
public class DiffClass {

    List<DiffBloc> blocs = new ArrayList<>();

    //private DiffState state;
    private File file;

    public DiffClass(File file) {
        this.file = file;
    }

    public void addBloc(int start, int end, DiffState state) {
        DiffBloc diffBloc = new DiffBloc(start, end, state);
        addBloc(diffBloc);
    }

    public void addBloc(DiffBloc diffBloc) {
        blocs.add(diffBloc);
    }

    public File getFile() {
        return file;
    }

    public List<DiffBloc> getBlocs() {
        return blocs;
    }
}
