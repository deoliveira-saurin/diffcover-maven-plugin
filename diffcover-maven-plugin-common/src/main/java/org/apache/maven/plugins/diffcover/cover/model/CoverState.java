package org.apache.maven.plugins.diffcover.cover.model;

public enum CoverState {
    IGNORED("IGNORED"), NOT_COVERED("NOCOVER"), PARTIAL_COVERED("PTCOVER"), COVERED("COVERED");

    private final String shortName;

    private CoverState(String shortName) {
        this.shortName = shortName;
    }

    public static CoverState valueOf(int counter) {
        switch (counter) {
            case 0:
                return CoverState.IGNORED;
            case 1:
                return CoverState.NOT_COVERED;
            case 2:
                return CoverState.COVERED;
            case 3:
                return CoverState.PARTIAL_COVERED;
            default:
                return null;
        }
    }

    public String getShortName() {
        return shortName;
    }
}
