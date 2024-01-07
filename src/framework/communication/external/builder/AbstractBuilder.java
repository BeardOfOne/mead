package framework.communication.external.builder;

import framework.communication.external.filesystem.AbstractFileSystem;

/**
 * The abstract representation of the methods implemented by all builder types
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public abstract class AbstractBuilder<T extends AbstractFileSystem>  {

    /**
     * File system used by the builder
     */
    protected T _fileSystem;

    /**
     * Performs all user-input related tasks before going through the 
     * list of automated build processes
     * 
     * @return The success of the user-input to determine if the build process should continue
     */
    public abstract boolean buildStart();

    /**
     * Builds custom content
     */
    public abstract void buildContent();

    /**
     * Finalizes the building of the builder's tasks
     */
    public abstract void buildEnd();
}