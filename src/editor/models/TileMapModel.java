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

package editor.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import framework.api.IModel;
import framework.communication.internal.signal.ISignalListener;
import framework.core.mvc.model.BaseModel;


/**
 * This model represents a singular tile map entity
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="tilemap")
public final class TileMapModel extends BaseModel {

    /**
     * Signal name indicating that this model's selection state has changed
     */
    public static final String EVENT_TILEMAP_SELECTION_CHANGED = "EVENT_TILEMAP_SELECTION_CHANGED";

    /**
     * The name of the tile map
     */
    @XmlAttribute(name="name")
    private String _name = "";

    /**
     * The number of rows within the tile map
     */
    @XmlAttribute(name="rows")
    private int _rows;

    /**
     * The number of columns within the tile map
     */
    @XmlAttribute(name="columns")
    private int _columns;

    /**
     * The tile width of a single tile
     */
    @XmlAttribute(name="width")
    private int _width;

    /**
     * The tile height of a single tile
     */
    @XmlAttribute(name="height")
    private int _height;

    /**
     * The top-left x-value
     */
    @XmlAttribute(name="x")
    private int _x;
    
    /**
     * The top-left y-value 
     */
    @XmlAttribute(name="y")
    private int _y;
    
    /**
     * A list of all the tiles associated to this tile map
     */
    @XmlElement(name="tile")
    private List<TileModel> _tiles = new ArrayList();

    /**
     * This flag indicates if the model is in a selected state
     */
    private transient boolean _selected;

    /**
     * Constructs a new instance of this class type
     *
     * Note: This constructor is necessary for serialization
     *
     */
    public TileMapModel() {
    }

    /**
     * Constructs a new instance of this class type
     *
     * @param receiver The list of receivers that will receive events when this model changes
     * @param <T> A type that extends from ISignalListener
     */
    public <T extends ISignalListener> TileMapModel(T... receiver) {
        super(receiver);
    }

    /**
     * Sets the selection state of this model
     * 
     * @param selected The selection state of this model
     */
    public void setSelected(boolean selected) {
        _selected = selected;
        setOperation(EVENT_TILEMAP_SELECTION_CHANGED);
        doneUpdating();
    }

    /**
     * Gets the selection state of this tile
     * 
     * @return If this tilemap is selected
     */
    public boolean getSelected() {
        return _selected;
    }

    /**
     * @return the height
     */
    public int getHeight() {
        return _height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(int height) {
        this._height = height;
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return _name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this._name = name;
    }

    /**
     * @return the columns
     */
    public int getColumns() {
        return _columns;
    }

    /**
     * @param columns the columns to set
     */
    public void setColumns(int columns) {
        this._columns = columns;
    }

    /**
     * @return the rows
     */
    public int getRows() {
        return _rows;
    }

    /**
     * @param rows the rows to set
     */
    public void setRows(int rows) {
        this._rows = rows;
    }

    /**
     * @return the tiles
     */
    public List<TileModel> getTiles() {
        return _tiles;
    }

    /**
     * @param tiles the tiles to set
     */
    public void setTiles(List<TileModel> tiles) {
        this._tiles = tiles;
    }

    /**
     * @return the width
     */
    public int getWidth() {
        return _width;
    }

    /**
     * Sets the width
     * 
     * @param width the width to set
     */
    public void setWidth(int width) {
        this._width = width;
    }

    /**
     * Sets the coordinate of this model
     * 
     * @param x The x-coordinate
     * 
     */
    public void setCoordinate(int x, int y) {
        _x = x;
        _y = y;
    }

    /**
     * @return The x-coordinate
     */
    public int getXCoordinate() {
        return _x;
    }
    
    /**
     * @return The y-coordinate
     */
    public int getYCoordinate() {
        return _y;
    }

    @Override public void copyData(IModel model) {

        super.copyData(model);
        if(model instanceof TileMapModel) {

            TileMapModel tileMapModel = (TileMapModel) model;

            setName(tileMapModel.getName());
            setRows(tileMapModel.getRows());
            setColumns(tileMapModel.getColumns());
            setWidth(tileMapModel.getWidth());
            setHeight(tileMapModel.getHeight());
            setCoordinate(tileMapModel.getXCoordinate(), tileMapModel.getYCoordinate());

            getTiles().addAll(tileMapModel.getTiles());
        }
    }

    @Override public String toString() {
        return String.format("Name: %s, Rows: %d, Columns: %d, CellWidth: %d, CellHeight: %d",
                getName(), 
                getRows(), 
                getColumns(), 
                getWidth(), 
                getHeight()
                );
    }
}
 