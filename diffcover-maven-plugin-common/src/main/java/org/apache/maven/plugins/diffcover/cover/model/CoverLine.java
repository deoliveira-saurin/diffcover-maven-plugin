package org.apache.maven.plugins.diffcover.cover.model;

public class CoverLine {

    private int number;

    private String content;

    private CoverState state;

    public CoverLine(int number, String content, CoverState lineState) {
        this.number = number;
        this.content = content;
        this.state = lineState;
    }

    public CoverState getState() {
        return state;
    }

    public String toString() {
        return number + " " + content + " : " + state;
    }
}
