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

package framework.communication.internal.signal;

/**
 * Container class used for holding signal names to signal listener relationships
 * 
 * This container is usually used when keeping track of which signals are attached to a particular
 * entity
 * 
 */
public class SignalListenerContainer {

    /**
     * The signal name 
     */
    public final String signalName;

    /**
     * The signal receiver
     */
    public final ISignalReceiver signalReceiver;

    /**
     * If this signal can be invoked on
     */
    public boolean isEnabled = true;
    
    /**
     * Constructs a new instance of this class type
     *
     * @param signalName The signal name
     * @param signalReceiver The signal receiver
     */
    public SignalListenerContainer(String signalName, ISignalReceiver signalReceiver) {
        this.signalName = signalName;
        this.signalReceiver = signalReceiver;
    }
}