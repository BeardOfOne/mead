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

package game.gameplay;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import engine.api.IData;
import engine.core.factories.AbstractFactory;
import engine.core.graphics.IRenderable;
import game.core.factories.DataFactory;

public abstract class AbstractDataEntity implements IRenderable {

    /**
     * The data associated to the initially specified active data
     */
    private final List<IData> _data;

    /**
     * The active data that is associated to this entity
     */
    private IData _activeData;
    
    /**
     * The active data cache associated to the active data
     */
    private Image _activeDataCache;

    /**
     * Constructs a new instance of this class type
     */
    protected AbstractDataEntity(UUID identifier) {
        _data = new ArrayList(AbstractFactory.getFactory(DataFactory.class).getDataGroup(identifier));
    }
    
    protected void setActiveData(UUID identifier) {
        _activeData = null;
        _activeDataCache = null;
        
        if(identifier != null) {
            for(IData data : _data) {
                if(data.getIdentifier().equals(identifier)) {
                    _activeData = data;
                    _activeDataCache = _activeData.getImageData();
                    break;
                }
            }
        }
    }

    public final boolean hasActiveData() {
        return _activeData != null;
    }
    
    @Override public Image getRenderableContent() {
        return _activeDataCache;
    }
}