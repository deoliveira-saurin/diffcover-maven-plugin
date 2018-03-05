package org.apache.maven.plugins.diffcover.cover;

import org.apache.maven.plugins.diffcover.Engine;
import org.apache.maven.plugins.diffcover.cover.model.CoverClasses;

/**
 * Inteface of all cover engines like JacocoEngine, CloverEngine, ...<br/>
 * All cover engines have to return a coverClasses.
 */
public interface CoverEngine extends Engine<CoverClasses> {
}
