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

package framework.core.entity;

import java.awt.Image;
import java.util.UUID;

import framework.core.factories.AbstractFactory;
import framework.core.factories.DataFactory;
import framework.core.graphics.IRenderable;

public abstract class AbstractDataEntity implements IRenderable {

    /**
     * The active data cache associated to the active data
     */
    private Image _renderableData;
    
    private UUID _identifier;
    
    public void setActiveData(UUID identifier) {
        _renderableData = null;
        _identifier = identifier;
        if(_identifier != null) {
            _renderableData = AbstractFactory.getFactory(DataFactory.class).getDataEntity(_identifier);
        }
    }
    
    public abstract void refresh();
    
    @Override public Image getRenderableContent() {
        return _renderableData;
    }
    
    @Override public String toString() {
        if(_identifier == null) {
            return "No Identifier";
        }
        return _identifier.toString();
    }
}