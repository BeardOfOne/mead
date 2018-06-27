/**
 * Daniel Ricci <thedanny09@gmail.com>
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

package framework.communication.external.builder;

import java.util.logging.Level;

import framework.communication.external.filesystem.AbstractFileSystem;
import framework.utils.logging.Tracelog;

/**
 * Director class used for directing builder type object
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
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