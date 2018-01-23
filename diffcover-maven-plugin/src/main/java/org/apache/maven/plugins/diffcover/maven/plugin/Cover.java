package org.apache.maven.plugins.diffcover.maven.plugin;

public enum Cover {
    IGNORED("IGNORED"), NOT_COVERED("N-COVER"), PARTIAL_COVERED("P-COVER"), COVERED("COVERED");
    private final String shortName;

    private Cover(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }
}
