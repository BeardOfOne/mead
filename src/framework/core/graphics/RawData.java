package framework.core.graphics;

import java.awt.Image;

/**
 * This class deals with holding raw data used for rendering
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 */
public class RawData implements IRenderable {

    /**
     * The raw data
     */
    private final Image _rawData;

    /**
     * Constructs a new instance of this class type
     *
     * @param rawData The raw data
     */
    public RawData(Image rawData) {
        _rawData = rawData;
    }

    @Override public Image getRenderableContent() {
        return _rawData;
    }
}