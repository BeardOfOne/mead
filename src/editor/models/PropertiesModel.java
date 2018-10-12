/**
 * Daniel Ricci <thedanny09@icloud.com>
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

package editor.models;

import framework.communication.internal.signal.ISignalListener;
import framework.communication.internal.signal.arguments.EventArgs;
import framework.core.mvc.model.BaseModel;

/**
 * The model associated to the properties of a particular tile 
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class PropertiesModel extends BaseModel {

    /**
     * The tile model currently being used in the properties pipeline
     */
    private TileModel _tileModel;

    /**
     * Constructs a new instance of this class type
     * 
     * @param receivers The list of receivers to receive event notifications
     */
    public PropertiesModel(ISignalListener... receivers) {
        super(receivers);
    }

    /**
     * Sets the specified tile model
     * 
     * @param model The tile model
     */
    public void setTileModel(TileModel model) {
        _tileModel = model;
        doneUpdating();
    }

    /**
     * Gets the tile model associated to this model
     * 
     * @return The tile model
     */
    public TileModel getTileModel() {
        return _tileModel;
    }

    @Override public void update(EventArgs signalEvent) {
    }
}