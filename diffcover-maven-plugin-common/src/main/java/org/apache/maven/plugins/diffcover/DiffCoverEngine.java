package org.apache.maven.plugins.diffcover;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugins.diffcover.cover.CoverEngine;
import org.apache.maven.plugins.diffcover.cover.JacocoCoverEngine;
import org.apache.maven.plugins.diffcover.cover.model.CoverClass;
import org.apache.maven.plugins.diffcover.cover.model.CoverClasses;
import org.apache.maven.plugins.diffcover.cover.model.CoverState;
import org.apache.maven.plugins.diffcover.diff.DiffEngine;
import org.apache.maven.plugins.diffcover.diff.GitDiffEngine;
import org.apache.maven.plugins.diffcover.diff.model.DiffBloc;
import org.apache.maven.plugins.diffcover.diff.model.DiffClass;
import org.apache.maven.plugins.diffcover.diff.model.DiffClasses;

@Slf4j
public class DiffCoverEngine implements Engine<Boolean> {

    @Override
    public Boolean analyse(Parameters parameters) {
        DiffEngine diffEngine = new GitDiffEngine();
        DiffClasses diffClasses = diffEngine.analyse(parameters);

        CoverEngine coverEngine = new JacocoCoverEngine();
        CoverClasses coverClasses = coverEngine.analyse(parameters);

        for (DiffClass diffClass : diffClasses.values()) {
            log.info("Diff {}", diffClass);
            CoverClass coverClass = coverClasses.get(diffClass.getFile());
            if (coverClass != null) {
                log.info("Cover {}", coverClass);
                for (DiffBloc diffBloc : diffClass.getBlocs()) {
                    for (int index = diffBloc.getStart(); index < diffBloc.getEnd(); index++) {
                        CoverState state = coverClass.getLines().get(index).getState();
                        log.info(coverClass.getLines().get(index).toString());
                        switch (state) {
                            case COVERED:
                                break;
                            case IGNORED:
                                break;
                            case PARTIAL_COVERED:
                                return false;
                            case NOT_COVERED:
                                return false;
                        }
                    }
                }
            }
        }

        return true;
    }
}
