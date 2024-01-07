package framework.communication.external.builder;

import java.util.logging.Level;

import framework.communication.external.filesystem.AbstractFileSystem;
import framework.utils.logging.Tracelog;

/**
 * Director class used for directing builder type object
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class Director {

    /**
     * The Builder type
     */
    private final AbstractBuilder<AbstractFileSystem> _builder;

    /**
     * Constructs a new instance of this class type
     * 
     * @param builder The builder to direct
     */
    public Director(AbstractBuilder builder) {
        _builder = builder;
    }

    /**
     * Constructs the content using the specified builder
     * 
     * @return The success of the operation
     */
    public boolean construct() {
        try {
            if(!_builder.buildStart()) {
                return false;
            }

            _builder.buildContent();
            _builder.buildEnd();
        } 
        catch(Exception exception) {
            Tracelog.log(Level.SEVERE, false, exception);
            return false;
        }

        return true;
    }
}