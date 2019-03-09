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

package framework.communication.internal.signal.arguments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import framework.api.IModel;

/**
 * Event that specifies a UUID
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 * 
 */
public final class UUIDEventArgs extends ModelEventArgs {
    /**
     * The list of unique identifiers
     */
    private final List<UUID> _identifiers = new ArrayList();
        
    /**
     * Constructs a new instance of this class type
     *
     * @param sender The sender source
     * @param operationName The name of the operation being performed
     * @param uuid The uuid to include in the event
     */
    public UUIDEventArgs(IModel sender, String operationName, UUID... uuid) { 
        super(sender, operationName);
        _identifiers.addAll(Arrays.asList(uuid));
    }
    
    /**
     * Gets the list of unique identifiers associated to this class
     *
     * @return The list of unique identifiers
     */
    public List<UUID> getIdentifiers() {
        return new ArrayList<UUID>(_identifiers);
    }
}