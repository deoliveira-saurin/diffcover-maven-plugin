package org.apache.maven.plugins.diffcover.diff.model;

public class DiffBloc {

    private int start;

    private int end;

    private DiffState state;

    public DiffBloc(int start, int end, DiffState state) {
        this.start = start;
        this.end = end;
        this.state = state;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public DiffState getState() {
        return state;
    }
}
