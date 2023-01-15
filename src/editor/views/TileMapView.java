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

package editor.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import framework.communication.internal.signal.arguments.EventArgs;
import framework.core.factories.AbstractFactory;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ControllerFactory;
import framework.core.factories.ModelFactory;
import framework.core.factories.ViewFactory;
import framework.core.mvc.view.PanelView;
import framework.core.mvc.view.layout.DragListener;
import framework.utils.MouseListenerEvent.SupportedActions;

import editor.controllers.TileMapController;
import editor.models.TileMapModel;

/**
 * This view represents a tilemap entity
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public class TileMapView extends PanelView {

    /**
     * Constructs a new instance of this class type
     *
     * @param name The name of the tile map
     * @param rows The number of rows
     * @param columns The number of columns
     * @param cellWidth The cell width
     * @param cellHeight The cell height
     * 
     */
    public TileMapView(String name, int rows, int columns, int cellWidth, int cellHeight) {
        // Do not render the background of the this class. Make it so that it is transparent. All tiles
        // that are created within this tile map should also have their opaque set to false
        setOpaque(false);

        // Create the setup model that will be associated to the tilemap
        TileMapModel tileMapModel = AbstractFactory.getFactory(ModelFactory.class).add(new TileMapModel(this), false);
        tileMapModel.setName(name);
        tileMapModel.setRows(rows);
        tileMapModel.setColumns(columns);
        tileMapModel.setWidth(cellWidth);
        tileMapModel.setHeight(cellHeight);

        // Create the tilemap controller that this view will use
        TileMapController tileMapController = AbstractSignalFactory.getFactory(ControllerFactory.class).add(new TileMapController(tileMapModel), false); 
        getViewProperties().setEntity(tileMapController);

        Rectangle visible = AbstractFactory.getFactory(ViewFactory.class).get(ProjectView.class).getVisibleRect();
        getViewProperties().getEntity(TileMapController.class).updateTileMapPosition(visible.x, visible.y);
        setLocation(visible.x, visible.y);

        setLayout(new GridBagLayout());
        // Listen to the drag events for both mouse and mouse motion
        DragListener drag = new DragListener(SupportedActions.LEFT) {
            @Override public void mousePressed(MouseEvent event) {
                super.mousePressed(event);
                getViewProperties().getEntity(TileMapController.class).toggleSelected();
            }
            @Override public void mouseReleased(MouseEvent args) {
                super.mouseReleased(args);
                Point location = TileMapView.this.getLocation();
                tileMapController.updateTileMapPosition(location.x, location.y);                
            }
        };

        this.addMouseListener(drag);
        this.addMouseMotionListener(drag);

        // Note: The border needs to be called at least after the tilemap model has been submitted to the controller
        setBorder(getTileMapBorder());
    }

    @Override public void render() {

        // Add the contents of the tilemap view to the main window
        AbstractSignalFactory.getFactory(ViewFactory.class).get(ProjectView.class).add(this);

        // Set the constraints of views
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;

        // Get the setup model associated to this view
        TileMapModel tileMapModel = getViewProperties().getEntity(TileMapController.class).getSetupDetails();

        // Get a reference to the controller of this view
        TileMapController tileMapController = getViewProperties().getEntity(TileMapController.class);

        for(int row = 0; row < tileMapModel.getRows(); ++row) {
            for(int col = 0; col < tileMapModel.getColumns(); ++col) {		

                // Create a tile and add it to our board
                TileView view = AbstractSignalFactory.getFactory(ViewFactory.class).add(new TileView(tileMapController), false);
                view.setPreferredSize(new Dimension(tileMapModel.getWidth(), tileMapModel.getHeight()));

                // Make sure that dimensions are properly mapped
                gbc.gridx = col;
                gbc.gridy = row;

                // Add the view to our tilemap
                add(view, gbc);

                // render the view
                view.render();
            }
        }

        setVisible(true);
    }

    private Border getTileMapBorder() {
        TileMapModel tileMapModel = getViewProperties().getEntity(TileMapController.class).getSetupDetails();
        return tileMapModel.getSelected() 
            ? BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(15, 1, 1, 1, Color.RED), tileMapModel.getName())
            : BorderFactory.createTitledBorder(BorderFactory.createMatteBorder(15, 1, 1, 1, Color.DARK_GRAY), tileMapModel.getName());
    }

    @Override public void update(EventArgs event) {
        super.update(event);

        if(event.getSource() instanceof TileMapModel) {
            TileMapModel tileMapModel = (TileMapModel) event.getSource();
            for(Component component: getComponents()) {
                if(component instanceof TileView) {
                    component.setPreferredSize(new Dimension(tileMapModel.getWidth(), tileMapModel.getHeight()));
                    component.revalidate();
                }
            }
            
            // Update the coordinate of this tile map
            setLocation(
                    tileMapModel.getXCoordinate(),
                    tileMapModel.getYCoordinate()
                    );

            // Set the border of the tile based on the selected state of the model
            setBorder(getTileMapBorder()); 
        }

        repaint();
    }
}