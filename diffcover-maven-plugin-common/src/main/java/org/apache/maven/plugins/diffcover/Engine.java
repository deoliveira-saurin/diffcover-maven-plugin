package org.apache.maven.plugins.diffcover;

/**
 * Interface of all engines.
 *
 * @param <T>
 */
public interface Engine<T> {

    /**
     * Generic method that takes plugin parameters and return the specific object depend on engine's type.
     *
     * @param parameters plugin parameters
     * @return specific objet
     */
    T analyse(Parameters parameters);

}
