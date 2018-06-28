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

package generated;

import java.awt.Point;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import framework.api.IData;
import framework.communication.internal.persistance.IXMLCodec;


/**
 * A tile map entity represents a portion of a tile map in data form
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 * 
 */
@XmlRootElement(name = "root")
@XmlAccessorType(XmlAccessType.FIELD)
public class TileMapData implements IXMLCodec, IData {

    /**
     * The UUID of this tile map entity
     */
    @XmlAttribute(name = "identifier")
    private final UUID _uuid;
    
    /**
     * The name of the tile map entity
     */
    @XmlAttribute(name = "name")
    private final String _tileName;

    /**
     * The layer name of the tile map entity
     */
    @XmlAttribute(name = "layers")
    private final List<UUID> _layers;

    /**
     * The friendly name of the tile
     */
    @XmlAttribute(name = "friendly")
    private final String _tileNameFriendly;

    /**
     * x1 Position
     */
    @XmlAttribute(name = "x1")
    private int _x1;

    /**
     * y1 Position
     */
    @XmlAttribute(name = "y1")
    private int _y1;
    
    /**
     * x2 Position
     */
    @XmlAttribute(name = "x2")
    private int _x2;
    
    /**
     * y2 Position
     */
    @XmlAttribute(name = "y2")
    private int _y2;
    
    /**
     * Creates a new instance of this class type
     */
    public TileMapData() {
        _uuid = null;
        _tileName = null;
        _tileNameFriendly = null;
        _layers = null;
    }

    /**
     * Creates a new instance of this class type
     * 
     * @param UUID The uuid of this tile
     * @param tileName The name of the tile
     * @param friendlyName The friendly name of the tile
     * @param layers the name of the layer
     */
    public TileMapData(UUID uuid, String tileName, String friendlyName, List<UUID> layers) {
        _uuid = uuid;
        _tileName = tileName;
        _tileNameFriendly = friendlyName;
        _layers = layers;
    }

    /**
     * Sets the top left position of the tile map data
     * 
     * @param x1 x-position
     * @param y1 y-position
     */
    public void setPositionTopLeft(int x1, int y1) {
        _x1 = x1;
        _y1 = y1;
    }
    
    public void setPositionBottomRight(int x2, int y2) {
        _x2 = x2;
        _y2 = y2;
    }

    public int getTopLeftX() {
        return _x1;
    }
    
    public int getTopLeftY() {
        return _y1;
    }
    
    public int getBottomRightX() {
        return _x2;
    }
    
    public int getBottomRightY() {
        return _y2;
    }
    
    @Override public UUID getIdentifier() {
        return _uuid;
    }

    @Override public List<UUID> getLayers() {
        return _layers;
    }

    @Override public String toString() {
        return _tileNameFriendly;
    }

    @Override public Point getTopLeft() {
        return new Point(getTopLeftX(), getTopLeftY());
    }

    @Override public Point getBottomRight() {
        return new Point(getBottomRightX(), getBottomRightY());
    }
}