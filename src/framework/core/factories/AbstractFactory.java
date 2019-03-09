/**
 * Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject
 * to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package framework.core.factories;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import framework.utils.logging.Tracelog;

/**
 * Top-most factory class that defines primitive functionality for other factories
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 */
public abstract class AbstractFactory {

    /**
     * The list of factories that have been constructed and that are still active 
     */
    static final List<AbstractFactory> FACTORIES = new ArrayList<>();

    /**
     * Resets the factory
     */
    public static final void clearFactories() {
        for(int i = FACTORIES.size() - 1; i >= 0; --i) {
            AbstractFactory factory = FACTORIES.get(i);
            if(!factory.isPersistent()) {
                FACTORIES.remove(i);
                factory.clear();
            }
        }
    }

    /**
     * Checks if there is a factory that is running
     * 
     * Note: For a factory to run there must have been something created
     *       in the factory, and it cannot be a factory that is non-persistent
     * 
     * @return Flag indicating if the factory is running
     */
    public static final boolean isRunning() {
        // Go through the list of factories
        for(AbstractFactory factory : FACTORIES) {
            if(!factory.isPersistent() && factory.hasEntities()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Retrieves the specified factory in question. 
     * 
     * If the factory does not exist then there will be an attempt 
     * to create the factory and store a reference to it within 
     * this construct. 
     * 
     * Future calls to this method will return a reference to the 
     * created factory.
     * 
     * @param factoryClass The class to lookup in the list of already created factories
     * 
     * @param <T> A type extending AbstractFactory
     * 
     * @return The factory of the specified concrete type
     */
    public static final <T extends AbstractFactory> T getFactory(Class<T> factoryClass) {
        // Verify if the factory has already been created, if so then
        // return that instance
        for(AbstractFactory factory : FACTORIES) {
            if(factory.getClass() == factoryClass) {
                return (T) factory;
            }
        }

        // If execution gets to here, then it is assumed that 
        // the factory being asked for has not been created yet 
        T factory = null;

        try {
            // Call the default constructor for the factory creation
            factory = factoryClass.getConstructor().newInstance();

            // add the factory to the factories list so that 
            // the reference is returned next time
            FACTORIES.add(factory);
        } 
        catch (Exception exception) {
            Tracelog.log(Level.SEVERE, false, exception);
        }

        // Return the newly created factory reference
        return factory;
    }

    /**
     * Indicates if the factory has entities that it currently being used.  An entity is in use
     * if it is stored in a buffer/cache of some sort and can be called from outside the factory
     * 
     * @return TRUE if the factory has entities that are currently being used, FALSE otherwise
     */
    protected abstract boolean hasEntities();
 
    protected abstract void clear();
    
    /**
     * Indicates if this factory persists throughout the lifetime of the application
     */

    /**
     * Gets if this factory is a persistent one or not. A persistent factory is one
     * that always exists regardless of if there are elements within it.
     *
     * @return TRUE if this factory is persistent, FALSE otherwise
     */
    protected abstract boolean isPersistent();
}