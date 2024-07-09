package framework.core.entity;

import java.awt.Image;
import java.util.UUID;

import framework.core.factories.AbstractFactory;
import framework.core.factories.DataFactory;
import framework.core.graphics.IRenderable;

public class DataEntity implements IRenderable {

	/**
	 * The identifier lookup value
	 */
    private UUID _identifier;

	/**
     * The active data cache associated to the active data
     */
    private Image _renderableData;
        
    /*
     * Constructs a new instance of this class type
     */
    protected DataEntity() {
    }
    
    /**
     * 
     * Constructs a new instance of this class type
     *
     * @param identifier The identifier lookup value
     */
    public DataEntity(UUID identifier) {
    	setActiveData(identifier);
    }
    
    /**
     * Sets the active data from the specified identifier
     *
     * @param identifier The identifier lookup value
     */
    public void setActiveData(UUID identifier) {
        _renderableData = null;
        _identifier = identifier;
        if(_identifier != null) {
            _renderableData = AbstractFactory.getFactory(DataFactory.class).getDataEntity(_identifier);
        }
    }

    public void refresh() {
    }
    
    public UUID getActiveDataIdentifier() {
    	return this._identifier;
    }
    
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