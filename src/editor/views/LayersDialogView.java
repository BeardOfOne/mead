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

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import editor.application.Application;
import editor.controllers.TileLayersController;
import editor.models.TileLayerModel;
import framework.communication.internal.signal.arguments.AbstractEventArgs;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ControllerFactory;
import framework.core.mvc.view.DialogView;
import framework.utils.globalisation.Localization;
import resources.ResourceKeys;

/**
 * Displays the list of available tile layers
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 * 
 */
public class LayersDialogView extends DialogView {

    /**
     * The last selected index. 
     * 
     * Note: We denote -1 because the selection fetching method for the list returns -1 when nothing has been selected
     */
    private int _lastSelectedIndex = -1;
    
    /**
     * Add button for adding a new layer
     */
    private final JButton _btnAdd = new JButton(Localization.instance().getLocalizedString(ResourceKeys.Add));

    /**
     * Update button for updating an existing layer
     */
    private final JButton _btnUpdate = new JButton(Localization.instance().getLocalizedString(ResourceKeys.Update));
    
    /**
     * Remove button for removing a selected layer
     */
    private final JButton _btnRemove = new JButton(Localization.instance().getLocalizedString(ResourceKeys.Remove));

    /**
     * List of layers used by the view
     */
    private final JList<TileLayerModel> _layers = new JList();

    /**
     * Text field component
     */
    private final JTextField _layersTextBox = new JTextField(22);

    /**
     * Constructs a new instance of this class type
     */
    public LayersDialogView() {
        super(
                Application.instance(), 
                Localization.instance().getLocalizedString(ResourceKeys.TileLayers)
                );

        getViewProperties().setEntity(
                AbstractSignalFactory.getFactory(ControllerFactory.class).add(new TileLayersController(), true)	
                );

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setResizable(false);
        setAlwaysOnTop(true);
        
        refeshControls();
    }
    
    private void refeshControls() {

        // Empty the contents of the search text
        _layersTextBox.setText("");
        
        // Set focus to the search text
        _layersTextBox.requestFocusInWindow();
        
        // Clear the selection from the layers list
        _layers.clearSelection();

        // Flag that indicates if the text search is empty
        boolean isTextBoxEmpty = _layersTextBox.getText().trim().isEmpty();
        
        // The add button is enabled when there is something valid within the text box
        _btnAdd.setEnabled(!isTextBoxEmpty);
        
        // The update button is enabled when there is a selection within the list
        _btnUpdate.setEnabled(!_layers.isSelectionEmpty());
        
        // The remove button is enabled when there is a selection within the list
        _btnRemove.setEnabled(!_layers.isSelectionEmpty());
    }

    @Override public void onViewInitialized() {
        JPanel actionsPanel = new JPanel();
        actionsPanel.add(_btnAdd);
        actionsPanel.add(_btnUpdate);
        actionsPanel.add(_btnRemove);
        add(actionsPanel);

        //Search containers
        JPanel _searchPanel = new JPanel();
        _searchPanel.add(_layersTextBox);
        add(_searchPanel);

        _layers.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        _layers.setLayoutOrientation(JList.VERTICAL);

        // Adds a scrolling to the list of layers
        JScrollPane listScroller = new JScrollPane(_layers);
        add(listScroller);

        /**
         * Bindings for the window itself
         */
        this.addWindowListener(new WindowAdapter() {
            @Override public void windowOpened(WindowEvent event) {
                super.windowOpened(event);
                _layersTextBox.requestFocusInWindow();
            }
        });
        
       _layers.addMouseListener(new MouseAdapter() {
            @Override public void mouseReleased(MouseEvent event) {
                if(_layers.getSelectedIndex() == _lastSelectedIndex) {
                    _layers.clearSelection();
                    _btnRemove.setEnabled(false);
                    _btnUpdate.setEnabled(false);
                }
                _lastSelectedIndex = _layers.getSelectedIndex(); 
            }
        });

        /**
         * Bindings for the text field
         */
        _layersTextBox.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent event) {
                _btnAdd.doClick();
            }
        });
        
        _layersTextBox.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent event) {
                _btnAdd.setEnabled(!_layersTextBox.getText().trim().isEmpty());
                _btnUpdate.setEnabled(!_layersTextBox.getText().trim().isEmpty() && !_layers.isSelectionEmpty());
                _lastSelectedIndex = _layers.getSelectedIndex();
            }
        });

        /**
         * Bindings for adding a new layer
         */
        _btnAdd.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent event) {
                TileLayersController controller = getViewProperties().getEntity(TileLayersController.class);
                if(controller.addLayer(_layersTextBox.getText())) {
                    refeshControls();
                    _lastSelectedIndex = _layers.getSelectedIndex();
                }
            }
        });

        /**
         * Bindings for the update button
         */
        _btnUpdate.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent event) {
                TileLayersController controller = getViewProperties().getEntity(TileLayersController.class);
                if(controller.updateLayer(_layers.getSelectedValue(), _layersTextBox.getText().trim())) {
                    refeshControls();
                }
            }
        });

        
        /**
         * Bindings for the remove button
         */
        _btnRemove.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent event) {
                TileLayersController controller = getViewProperties().getEntity(TileLayersController.class);
                if(controller.removeLayer(_layers.getSelectedValue())) {
                    refeshControls();
                }
            }
        });

        // Bind to the list of layers the models list from the controller.
        _layers.setModel(
                AbstractSignalFactory.getFactory(ControllerFactory.class).get(TileLayersController.class).getLayersModel()
                );

        /**
         * Bindings for the list of tiles when a selection occurs
         */
        _layers.addListSelectionListener(new ListSelectionListener() {
            @Override public void valueChanged(ListSelectionEvent event) {
                if(!event.getValueIsAdjusting()) {
                    _btnRemove.setEnabled(!_layers.isSelectionEmpty());
                }
            }
        });
    }

    @Override public void render() {

        pack();

        // Positions this dialog at the middle-right of the application
        // Note: This has to be done after the call to pack() because
        // the dimensions will change
        Dimension screenSize = getToolkit().getScreenSize();
        setLocation(0, ((int)(screenSize.getHeight() / 2) - (getHeight() / 2)));

        // Make sure to render the view only after the positioning of the view is done
        // or it will show up where the mouse is and potentially remove any on-mouse events
        // before making its way to the desired position
        setVisible(true);
    }

    @Override public void update(AbstractEventArgs signalEvent) {
    }
}