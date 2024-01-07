package editor.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import framework.communication.internal.signal.arguments.EventArgs;
import framework.core.factories.AbstractFactory;
import framework.core.factories.ModelFactory;
import framework.core.factories.ViewFactory;
import framework.core.graphics.RawData;
import framework.core.mvc.view.PanelView;

import editor.controllers.TileMapController;
import editor.models.TileModel;

/**
 * This view represents the visuals of a single tile
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 */
public class TileView extends PanelView {

    /**
     * The selected border style
     */
    private final Border SELECTED_BORDER = BorderFactory.createLineBorder(Color.RED, 2);

    /**
     * The default border style
     */
    private final Border DEFAULT_BORDER = BorderFactory.createLineBorder(Color.BLACK, 1);

    /**
     * Constructs a new instance of this class type
     *
     * @param tileMapController The controller of the tile map that owns this tile
     */
    public TileView(TileMapController tileMapController) {
        // Set the border
        setBorder(DEFAULT_BORDER);

        // Do not show any background, let the image do all the work
        setOpaque(false);
        
        // Use the tile map controller as the controller for this view
        getViewProperties().setEntity(tileMapController);

        // Create the tile model that this view will be populated from
        TileModel tileModel = AbstractFactory.getFactory(ModelFactory.class).add(
                new TileModel(this, tileMapController),
                false
                );

        // Add a new tile model that will be associated to this entity
        tileMapController.addTileEntity(tileModel);

        // Set the layout of this view
        setLayout(new BorderLayout());

        this.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent args) {
                PropertiesDialogView propertiesView = AbstractFactory.getFactory(ViewFactory.class).get(PropertiesDialogView.class);
                if(propertiesView != null && propertiesView.isVisible()) {
                    getViewProperties().getEntity(TileMapController.class).updateProperties(TileView.this);
                }
            }
            @Override public void mouseExited(MouseEvent args) {
                PropertiesDialogView propertiesView = AbstractFactory.getFactory(ViewFactory.class).get(PropertiesDialogView.class);
                if(propertiesView != null && propertiesView.isVisible()) {
                    getViewProperties().getEntity(TileMapController.class).updateProperties(null);
                }
            }
            @Override public void mouseReleased(MouseEvent args) {
                getViewProperties().getEntity(TileMapController.class).toggleSelectedTile(TileView.this);
            }
        });
    }

    @Override public void update(EventArgs event) {
        super.update(event);

        if(event.getSource() instanceof TileModel) {

            // Get the tile map model of the signal event received
            TileModel tileMapModel = (TileModel) event.getSource();

            // Set the border of the tile based on the selected state of the model
            setBorder(tileMapModel.getSelected() ? SELECTED_BORDER : DEFAULT_BORDER); 

            // If there is an image to be rendered, submit it to the graphics
            // rendering pipeline
            if(tileMapModel.getImage() != null) {
                addRenderableContent(new RawData(tileMapModel.getImage()));
            }
        }

        // Repaint the view
        repaint();
    }

    @Override public void render() {
        setVisible(true);
    }
}