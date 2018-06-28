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

package editor.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.border.Border;

import editor.application.Application;
import editor.controllers.TileMapController;
import editor.models.TileMapModel;
import framework.communication.internal.signal.arguments.AbstractEventArgs;
import framework.core.factories.AbstractFactory;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ControllerFactory;
import framework.core.factories.ModelFactory;
import framework.core.factories.ViewFactory;
import framework.core.mvc.view.PanelView;
import framework.core.mvc.view.layout.DraggableListener;
import framework.utils.globalisation.Localization;
import resources.ResourceKeys;

/**
 * This view represents a tilemap entity
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 *
 */
public class TileMapView extends PanelView {

    /**
     * Selected border style of this view
     */
    private final Border SELECTED_BORDER = BorderFactory.createMatteBorder(15, 2, 2, 2, Color.RED);

    /**
     * Normal border style of this view
     */
    private final Border NORMAL_BORDER = BorderFactory.createMatteBorder(15, 2, 2, 2, Color.DARK_GRAY);

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

        // Create the setup model that will be associated to the tilemap
        TileMapModel setupModel = AbstractFactory.getFactory(ModelFactory.class).add(new TileMapModel(this), false);
        setupModel.setName(name);
        setupModel.setRows(rows);
        setupModel.setColumns(columns);
        setupModel.setWidth(cellWidth);
        setupModel.setHeight(cellHeight);

        // Create the tilemap controller that this view will use
        TileMapController tileMapController = AbstractSignalFactory.getFactory(ControllerFactory.class).add(new TileMapController(setupModel), false); 
        getViewProperties().setEntity(tileMapController);

        Rectangle visible = AbstractFactory.getFactory(ViewFactory.class).get(ProjectView.class).getVisibleRect();
        getViewProperties().getEntity(TileMapController.class).updateTileMapPosition(visible.x, visible.y);
        setLocation(visible.x, visible.y);
    }

    @Override public void initializeComponentBindings() {
        // Listen to the drag events for both mouse and mouse motion
        DraggableListener drag = new DraggableListener(this);

        this.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent args) {
                if(!drag.getLastDragged()) {

                    // Toggle the selection state of the tile map
                    boolean isOn = getViewProperties().getEntity(TileMapController.class).toggleSelected();

                    // Set the focus on this tilemap based on if it is toggled
                    setFocusable(isOn);
                    if(isOn) {
                        requestFocusInWindow();
                    }
                }
                else {
                    // Get the current coordinate of this tilemap and update it within our model
                    getViewProperties().getEntity(TileMapController.class).updateTileMapPosition(getLocation().x, getLocation().y);
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent args) {

                // If the user pressed on the delete button, prompt the user if they wish to 
                // really delete this tile map
                if(args.getKeyCode() == KeyEvent.VK_DELETE) {
                    // If the user wishes to delete the tile map, perform the deletion
                    if(JOptionPane.showConfirmDialog(Application.instance(), Localization.instance().getLocalizedString(ResourceKeys.DeleteTileMapQuestion), Localization.instance().getLocalizedString(ResourceKeys.DeleteTileMapTitle), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    }
                }
            }
        });
    }

    @Override public void onViewInitialized() {
        setLayout(new GridBagLayout());
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
        TileMapModel setupModel = getViewProperties().getEntity(TileMapController.class).getSetupDetails();

        // Get a reference to the controller of this view
        TileMapController tileMapController = getViewProperties().getEntity(TileMapController.class);

        for(int row = 0; row < setupModel.getRows(); ++row) {
            for(int col = 0; col < setupModel.getColumns(); ++col) {		

                // Create a tile and add it to our board
                TileView view = AbstractSignalFactory.getFactory(ViewFactory.class).add(new TileView(tileMapController), false);
                view.setPreferredSize(new Dimension(setupModel.getWidth(), setupModel.getHeight()));

                // Make sure that dimensions are properly mapped
                gbc.gridx = col;
                gbc.gridy = row;

                // Set the border
                view.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));

                // Add the view to our tilemap
                add(view, gbc);

                // render the view
                view.render();
            }
        }

        setVisible(true);
    }

    @Override public void update(AbstractEventArgs event) {
        super.update(event);

        if(event.getSource() instanceof TileMapModel) {
            TileMapModel tileMapModel = (TileMapModel) event.getSource();

            // Update the coordinate of this tile map
            setLocation(
                    tileMapModel.getXCoordinate(),
                    tileMapModel.getYCoordinate()
                    );

            // Set the border of the tile based on the selected state of the model
            setBorder(tileMapModel.getSelected() ? SELECTED_BORDER : NORMAL_BORDER); 
        }

        repaint();
    }
}