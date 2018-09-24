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

package editor.views;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import framework.communication.internal.signal.arguments.AbstractEventArgs;
import framework.communication.internal.signal.arguments.ModelEventArgs;
import framework.core.factories.AbstractFactory;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ControllerFactory;
import framework.core.factories.ModelFactory;
import framework.core.graphics.CheckBoxListCellRenderer;
import framework.core.mvc.view.DialogView;
import framework.core.system.Application;
import framework.core.system.GameValues;
import framework.utils.globalisation.Localization;
import framework.utils.io.Paths;

import editor.controllers.PropertiesController;
import editor.controllers.TileLayersController;
import editor.models.PropertiesModel;
import editor.models.TileLayerModel;
import editor.models.TileModel;
import resources.ResourceKeys;

/**
 * This dialog displays information about selected objects within the scene
 * 
 * @author {@literal Daniel Ricci <thedanny09@icloud.com>}
 *
 */
public class PropertiesDialogView extends DialogView {

    /**
     * The unique identifier of the selected object
     */
    private JTextField _identifierTextField = new JTextField();

    /**
     * The name of the selected object
     */
    private JTextField _nameTextField = new JTextField();

    /**
     * The friendly name of the selected object
     */
    private JTextField _friendlyNameTextField = new JTextField();

    /**
     * The width value of the selection object
     */
    private JTextField _widthTextField = new JTextField();

    /**
     * The height value of the selected object
     */
    private JTextField _heightTextField = new JTextField();

    /**
     * The browse button component that controls the data for the selected object
     */
    private JButton _btnBrowse = new JButton(Localization.instance().getLocalizedString(ResourceKeys.Browse));

    /**
     * The clear button component that controls clearing of the image data within the currently selected tile
     */
    private JButton _btnClear = new JButton(Localization.instance().getLocalizedString(ResourceKeys.ClearImage));

    /**
     * The clear all button component that controls clearing the entirety of a selected tile
     */
    private JButton _btnClearAll = new JButton(Localization.instance().getLocalizedString(ResourceKeys.ClearAll));
    
    /**
     * The list of layers available for selection
     */
    private JList<TileLayerModel> _layers = new JList();

    /**
     * Constructs a new dialog
     */
    public PropertiesDialogView() {
        super(
                Application.instance,
                Localization.instance().getLocalizedString(ResourceKeys.Properties),
                400,
                650
                );

        // Sets this view to always be on top
        setAlwaysOnTop(true);

        // Create a properties model and assign it to our controller
        PropertiesModel propertiesModel = AbstractFactory.getFactory(ModelFactory.class).add(new PropertiesModel(this), true);
        getViewProperties().setEntity(
                AbstractSignalFactory.getFactory(ControllerFactory.class).add(new PropertiesController(propertiesModel), true)
                );

        // Set the layout manager of this dialog to be a grid-like layout
        getContentPane().setLayout(new GridLayout(7, 2, 0, 0));
        
        // ID Entry
        JPanel idPanel = new JPanel();
        getContentPane().add(idPanel);
        JLabel lblID = new JLabel(Localization.instance().getLocalizedString(ResourceKeys.ID));
        idPanel.add(lblID);
        _identifierTextField.setEditable(false);
        idPanel.add(_identifierTextField);
        _identifierTextField.setColumns(10);

        // Name Entry
        JPanel namePanel = new JPanel();
        getContentPane().add(namePanel);
        namePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JLabel lblName = new JLabel(Localization.instance().getLocalizedString(ResourceKeys.Name));
        namePanel.add(lblName);
        namePanel.add(_nameTextField);
        _nameTextField.setColumns(10);

        // Friendly Name Entry
        JPanel friendlyNamePanel = new JPanel();
        getContentPane().add(friendlyNamePanel);
        JLabel lblFriendlyName = new JLabel(Localization.instance().getLocalizedString(ResourceKeys.FriendlyName));
        friendlyNamePanel.add(lblFriendlyName);
        friendlyNamePanel.add(_friendlyNameTextField);
        _friendlyNameTextField.setColumns(10);

        // Width Entry
        JPanel widthPanel = new JPanel();
        getContentPane().add(widthPanel);
        JLabel lblWidth = new JLabel(Localization.instance().getLocalizedString(ResourceKeys.Width));
        widthPanel.add(lblWidth);
        _widthTextField.setEditable(false);
        widthPanel.add(_widthTextField);
        _widthTextField.setColumns(10);

        // Height Entry
        JPanel heightPanel = new JPanel();
        getContentPane().add(heightPanel);
        JLabel lblHeight = new JLabel(Localization.instance().getLocalizedString(ResourceKeys.Height));
        heightPanel.add(lblHeight);
        _heightTextField.setEditable(false);
        heightPanel.add(_heightTextField);
        _heightTextField.setColumns(10);

        // Layer Entry
        JPanel layerPanel = new JPanel();

        // Set how the rendering of the list box should be done.  I am using a custom
        // renderer to make it so that a check box appears to the left of each entry
        _layers.setCellRenderer(new CheckBoxListCellRenderer<TileLayerModel>());

        // Indicate the preferred size of the component, there were some sizing issues
        // when loading up the properties window for the first time when the mouse was
        // hovering over a tile
        _layers.setPreferredSize(new Dimension(200, 400));

        // Create a scroll pane and the layers within it
        JScrollPane listScroller = new JScrollPane(_layers);
        layerPanel.add(new JLabel(Localization.instance().getLocalizedString(ResourceKeys.Layer)));
        layerPanel.add(listScroller);
        getContentPane().add(layerPanel);

        // Data Entry
        JPanel dataPanel = new JPanel();
        JLabel lblData = new JLabel(Localization.instance().getLocalizedString(ResourceKeys.Data));
        dataPanel.add(lblData);
        dataPanel.add(_btnBrowse);
        dataPanel.add(_btnClear);
        dataPanel.add(_btnClearAll);
        getContentPane().add(dataPanel);

        /**
         * Name text field bindings
         */
        _nameTextField.addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent event) {

                // Some cleanup has to be done on the name
                char[] textName = _nameTextField.getText().trim().toCharArray();

                // Go through each character and replace those that are not letter, numbers, or underscores
                // with a white space, they will then be replaced with an underscore
                for(int i = 0; i < textName.length; ++i) {
                    if(!Character.isAlphabetic(textName[i]) && !Character.isDigit(textName[i]) && textName[i] != '_') {
                        textName[i] = '_';
                    }
                }

                // Update the text name
                _nameTextField.setText(new String(textName));

                // Get the properties controller associated to this view
                getViewProperties().getEntity(PropertiesController.class).setName(_nameTextField.getText());
            }
        });

        /**
         * Friendly Name text field bindings
         */
        _friendlyNameTextField.addFocusListener(new FocusAdapter() {
            @Override public void focusLost(FocusEvent e) {
                // Trim the friendly name of white spaces
                _friendlyNameTextField.setText(_friendlyNameTextField.getText().trim());

                // Get the properties controller associated to this view
                PropertiesController controller = getViewProperties().getEntity(PropertiesController.class);

                // Set the friendly name
                controller.setFriendlyName(_friendlyNameTextField.getText());
            }
        });

        /**
         * Browse Button Bindings
         */
        _btnBrowse.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent args) {

                JFileChooser chooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileFilter(new FileNameExtensionFilter("Image File (*.jpg, *.jpeg, *.png, *.gif)", "jpg", "jpeg", "png", "gif"));

                if(GameValues.exists(chooser.getClass().getSimpleName())) {
                    chooser.setCurrentDirectory(new File(GameValues.getGameValue(chooser.getClass().getSimpleName())));
                }

                // If the user chose a file
                if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                    // Store within the game properties the selected directory
                    GameValues.addGameProperty(chooser.getClass().getSimpleName(), chooser.getCurrentDirectory().toString());

                    // Create a buffer image
                    BufferedImage buffer = null;
                    try {

                        // Assign to the buffer image the selected file as an image
                        buffer = ImageIO.read(chooser.getSelectedFile());
                    }
                    catch(Exception exception) {
                        exception.printStackTrace(System.err);
                        return;
                    }

                    // Call the controller and update the model with the new buffered data
                    getViewProperties().getEntity(PropertiesController.class).setData(buffer);

                    // If the name text field is empty, then use the name of the image (excluding the extension)
                    // as the name for the tile, this is a convenience method.
                    if(_nameTextField.getText().trim().isEmpty()) {

                        // Set the name of the text field to be the name without extensions of the file being
                        // inserted into the tile
                        _nameTextField.setText(Paths.filenameNoExtension(chooser.getSelectedFile()));

                        // Set the name of the text field
                        getViewProperties().getEntity(PropertiesController.class).setName(_nameTextField.getText());
                    }
                }
            }
        });

        /**
         * Clear Button Bindings
         */
        _btnClear.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent event) {
                getViewProperties().getEntity(PropertiesController.class).setData(null);
            }
        });
        
        /**
         * Clear All Button Bindings
         */
        _btnClearAll.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent event) {
                getViewProperties().getEntity(PropertiesController.class).resetData();
            }
        });


        /**
         * Layer selection handlers
         */
        _layers.addListSelectionListener(new ListSelectionListener() {
            @Override public void valueChanged(ListSelectionEvent e) {

                // Get the adjusting value, which indicates if the layers control is
                // in the processes of doing its updated, or if it is "done"
                boolean adjust = _layers.getValueIsAdjusting();

                // If there are no more adjustments on the layer list to be done
                // and the layers control is enabled on the UI then update the list
                // on the server
                if (!adjust && _layers.isEnabled()) {

                    // Get the list of items that were checked
                    JList list = (JList) e.getSource();
                    List selectionValues = list.getSelectedValuesList();

                    // Call our controller and the set the new layer values
                    getViewProperties().getEntity(PropertiesController.class).setLayers(selectionValues);
                }
            }
        });
        _layers.setSelectionModel(new DefaultListSelectionModel() 
        {       
            // Make it so that clicking behaves the same as ctrl + click, else it will
            // toggle and only ever allow one element, this prevents that.
            @Override public void setSelectionInterval(int index0, int index1) 
            {
                if(_layers.isSelectedIndex(index0))  {
                    _layers.removeSelectionInterval(index0, index1);
                }
                else  {
                    _layers.addSelectionInterval(index0, index1);
                }
            }
        });
    }

    /**
     * Helper method used to easily update the state of the components on the UI
     *  
     * @param isEditable If the components can be interacted with by the user
     */
    private void updateComponentStates(boolean isEditable) {

        // Set if the fields are editable
        _nameTextField.setEditable(isEditable);
        _friendlyNameTextField.setEditable(isEditable);

        // Focusable prevents events from being fired even when
        // the fields are not enabled
        _nameTextField.setFocusable(isEditable);
        _friendlyNameTextField.setFocusable(isEditable);

        // Set if the layers drop down can be interacted with
        _layers.setEnabled(isEditable);

        // Set if the image manipulation buttons can be interacted with
        _btnBrowse.setEnabled(isEditable);
        _btnClear.setEnabled(isEditable);
        _btnClear.setEnabled(isEditable);
    }

    @Override public void render() {
        // Positions this dialog at the middle-right of the application
        Dimension screenSize = getToolkit().getScreenSize();
        setLocation((int)(screenSize.getWidth() - getWidth()), (int)((screenSize.getHeight() - getHeight()) / 2));

        // Make sure to render the view only after the positioning of the view is done
        // or it will show up where the mouse is and potentially remove and on-mouse events
        // before making its way to the desired position
        setVisible(true);

        // Flag this properties window as being in a read only state
        updateComponentStates(false);

        // Request focus on the window
        requestFocus();
    }

    @Override public void update(AbstractEventArgs event) {

        // Cast to our proper event
        ModelEventArgs modelEvent = (ModelEventArgs) event;

        // Get the properties model of our event
        PropertiesModel model = (PropertiesModel) modelEvent.getSource();

        // From the properties model the the tile model within it
        TileModel tileModel = model.getTileModel();

        // Update the UI components based on if the tile model exists and if it
        // is selected or not.  Recall that a selected tile is one that was clicked
        // by the user.
        updateComponentStates(tileModel != null && tileModel.getSelected());

        // Set the name text field
        _nameTextField.setText(tileModel != null ? tileModel.getName() : "");

        _friendlyNameTextField.setText(tileModel != null ? tileModel.getFriendlyName() : "");

        // Set the identifier text field
        _identifierTextField.setText(tileModel != null ? tileModel.getUUID().toString() : "");

        // Set the width text field
        _widthTextField.setText(tileModel != null ? String.valueOf(tileModel.getWidth()) : "");

        // Set the height text field
        _heightTextField.setText(tileModel != null ? String.valueOf(tileModel.getHeight()) : "");

        // Ensure that the layers control is not enabled before populating
        // it or the event handlers attached will fire after the values are 
        // set resulting in a clearing of the layers
        boolean isLayersEnabled = _layers.isEnabled();
        _layers.setEnabled(false);

        // Make sure there is a valid tile model and a valid layer
        if(tileModel == null) {
            // Set the model of the combo box to an empty combo box model
            // effectively clearing the contents of the combo box on the view
            _layers.setModel(new DefaultComboBoxModel());
        } 
        else {
            TileLayersController tileLayersController = AbstractSignalFactory.getFactory(ControllerFactory.class).get(TileLayersController.class);
            if(tileLayersController != null) {

                // Set the model of the layer list
                _layers.setModel(tileLayersController.getLayersModel());

                // Go through the list of layers in the properties window, and check
                // the one that are currently set for the specified tile model
                List<Integer> indices = new ArrayList();
                for(int i = 0, size = _layers.getModel().getSize(); i < size; ++i) {
                    TileLayerModel lookupLayer = _layers.getModel().getElementAt(i);
                    if(tileModel.getLayers().stream().anyMatch(z -> z.equals(lookupLayer.getUUID()))) {
                        indices.add(i);
                    }
                }
                _layers.setSelectedIndices(indices.stream().mapToInt(z -> z).toArray());
            }
        }
        
        // Re-enable the layers because of above
        _layers.setEnabled(isLayersEnabled);
    } 
}