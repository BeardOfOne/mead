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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.logging.Level;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.text.PlainDocument;

import framework.core.mvc.view.DialogView;
import framework.core.mvc.view.layout.SpringLayoutHelper;
import framework.core.system.Application;
import framework.utils.filters.DocumentIntegerFilter;
import framework.utils.globalisation.Localization;
import framework.utils.logging.Tracelog;

import resources.ResourceKeys;

/**
 * This dialog allows the user to configure the parameters for the tilemap
 * 
 * @author {@literal Daniel Ricci <thedanny09@icloud.com>}
 *
 */
public class TileMapDialogView extends DialogView {

    /**
     * The dimensions of the setup if anything
     */
    private Dimension _dimension;

    /**
     * The cancel button to cancel this dialogs operation
     */
    private final JButton _cancelButton = new JButton(Localization.instance().getLocalizedString(ResourceKeys.Cancel));

    /**
     * The cell height text field
     */
    private final JTextField _cellHeightField = new JTextField();

    /**
     * The cell width text field
     */
    private final JTextField _cellWidthField = new JTextField();

    /**
     * The column text field
     */
    private final JTextField _columnField = new JTextField();

    /**
     * This panel holds the OK and Cancel buttons
     */
    private final JPanel _confirmationPanel = new JPanel();

    /**
     * This panel holds the layout of all configuration fields
     */
    private final JPanel _dialogPanel = new JPanel(new SpringLayout());

    /**
     * The title text field, this will correspond to the title of the tile map
     */
    private final JTextField _nameField = new JTextField(20);

    /**
     * The OK button to accept the contents entered in this dialog
     */
    private final JButton _okButton = new JButton(Localization.instance().getLocalizedString(ResourceKeys.OK));

    /**
     * The row text field
     */
    private final JTextField _rowField = new JTextField();

    /**
     * The panel associated to the controls for scaling
     */
    private JPanel _scalePanel = new JPanel();

    /**
     * The check box for controlling auto-scaling
     */
    private final JCheckBox _scaleCheckBox = new JCheckBox();

    /**
     * Constructs a new instance of this class type
     */
    private TileMapDialogView() {
        
        super(Application.instance, Localization.instance().getLocalizedString(ResourceKeys.NewTilemap));

        // Set the layout manager of this view
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        setAutomaticDialogCentering(true);
        
        // Set the modal state
        setModal(true);

        // Make sure it is always on top
        setAlwaysOnTop(true);

        // Do not allow it to be resized
        setResizable(false);
    }

    /**
     * Constructs a new instance of this class type
     *
     * @param dimension The dimensions of the image (for scaling purposes)
     */
    public TileMapDialogView(Dimension dimension) {
        this();

        _dimension = dimension;
        
        // NAME
        _dialogPanel.add(new JLabel(Localization.instance().getLocalizedString(ResourceKeys.Name)));
        _nameField.setMaximumSize(_nameField.getPreferredSize());
        _dialogPanel.add(_nameField);

        // ROWS
        _dialogPanel.add(new JLabel(Localization.instance().getLocalizedString(ResourceKeys.Rows)));
        _rowField.setMaximumSize(_rowField.getPreferredSize());
        ((PlainDocument)_rowField.getDocument()).setDocumentFilter(new DocumentIntegerFilter());
        _dialogPanel.add(_rowField);

        // COLUMNS
        _dialogPanel.add(new JLabel(Localization.instance().getLocalizedString(ResourceKeys.Columns)));
        _columnField.setMaximumSize(_columnField.getPreferredSize());
        ((PlainDocument)_columnField.getDocument()).setDocumentFilter(new DocumentIntegerFilter());
        _dialogPanel.add(_columnField);

        // CELL WIDTH
        _dialogPanel.add(new JLabel(Localization.instance().getLocalizedString(ResourceKeys.CellWidth)));
        _cellWidthField.setMaximumSize(_cellWidthField.getPreferredSize());
        ((PlainDocument)_cellWidthField.getDocument()).setDocumentFilter(new DocumentIntegerFilter());
        _dialogPanel.add(_cellWidthField);

        // CELL HEIGHT
        _dialogPanel.add(new JLabel(Localization.instance().getLocalizedString(ResourceKeys.CellHeight)));
        _cellHeightField.setMaximumSize(_cellHeightField.getPreferredSize());
        ((PlainDocument)_cellHeightField.getDocument()).setDocumentFilter(new DocumentIntegerFilter());
        _dialogPanel.add(_cellHeightField);

        // Make the grid compact 
        SpringLayoutHelper.makeCompactGrid(_dialogPanel, 5, 2, 6, 6, 10, 0);

        // Add the dialog panel
        add(_dialogPanel);
        
        // SCALE DIMENSIONS
        if(_dimension != null) {
            _scalePanel.add(new JLabel(Localization.instance().getLocalizedString(ResourceKeys.ScaleDimensions)));
            _scaleCheckBox.setMaximumSize(_scaleCheckBox.getPreferredSize());
            _scalePanel.add(_scaleCheckBox);

            // Add the scale panel
            add(_scalePanel);
        }
        
        // Add our confirmation buttons to our panel
        _confirmationPanel.add(_okButton);
        _confirmationPanel.add(_cancelButton);
        add(_confirmationPanel);
        
        // If dimension was provided then add some more bindings
        if(_dimension != null) {

            // The binding method for column field and row field changes
            KeyListener columnRowListener = new KeyAdapter() {
                @Override public void keyReleased(KeyEvent e) {

                    // If the scale check box is unchecked then do not proceed any further
                    if(!_scaleCheckBox.isSelected()) {
                        return;
                    }

                    if(_columnField.getText().isEmpty() || _rowField.getText().isEmpty()) {
                        return;
                    }

                    try {
                        // Calculate the cell width and height fields BEFORE
                        // applying them, this is in case of division by 0, I do not
                        // want partial results
                        int cellWidth = _dimension.width / Integer.parseInt(_columnField.getText());
                        int cellHeight = _dimension.height / Integer.parseInt(_rowField.getText());

                        _cellWidthField.setText(Integer.toString(cellWidth));
                        _cellHeightField.setText(Integer.toString(cellHeight));
                    }
                    catch(Exception exception) {
                        // Nothing logged here, this is mainly because of potential division by zero issues
                        // that are not worth the code to guard against
                    }
                }
            };

            // Add the listener to the column and row field
            _columnField.addKeyListener(columnRowListener);
            _rowField.addKeyListener(columnRowListener);

            // The binding method for cell width and cell height changes
            KeyListener cellWidthHeightListener = new KeyAdapter() {
                @Override public void keyReleased(KeyEvent e) {

                    // If the scale check box is unchecked then do not proceed any further
                    if(!_scaleCheckBox.isSelected()) {
                        return;
                    }

                    if(_cellWidthField.getText().isEmpty() || _cellHeightField.getText().isEmpty()) {
                        return;
                    }

                    try {
                        // Calculate the cell width and height fields BEFORE
                        // applying them, this is in case of division by 0, I do not
                        // want partial results
                        int columns = _dimension.width / Integer.parseInt(_cellWidthField.getText());
                        int rows = _dimension.height / Integer.parseInt(_cellHeightField.getText());

                        _columnField.setText(Integer.toString(columns));
                        _rowField.setText(Integer.toString(rows));      
                    }
                    catch(Exception exception) {
                        // Nothing logged here, this is mainly because of potential division by zero issues
                        // that are not worth the code to guard against
                    }
                }
            };

            // Add the listener to the cell width and cell height field
            _cellWidthField.addKeyListener(cellWidthHeightListener);
            _cellHeightField.addKeyListener(cellWidthHeightListener);
        }

        // OK button event
        _okButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if(validateDialog()) {
                    setDialogResult(JOptionPane.OK_OPTION);
                    setVisible(false);
                }
            }
        });

        // Cancel button event
        _cancelButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                setDialogResult(JOptionPane.CANCEL_OPTION);
                setVisible(false);
            }
        });

    }
    
    public TileMapDialogView(String name, String rows, String columns, String width, String height) {
        this();
        
        _nameField.setText(name);
        _rowField.setText(rows);
        _columnField.setText(columns);
        _cellWidthField.setText(width);
        _cellHeightField.setText(height);
    }

    /**
     * Gets the cell height data of the dialog
     * 
     * @return The cell height size
     */
    public int getCellHeightField() {
        return Integer.parseInt(_cellHeightField.getText().trim());
    }

    /**
     * Gets the cell width data of the dialog
     * 
     * @return The cell width size
     */
    public int getCellWidthField() {
        return Integer.parseInt(_cellWidthField.getText().trim());
    }

    /**
     * Gets the column data of the dialog
     * 
     * @return The number of columns entered
     */
    public int getColumnsField() {
        return Integer.parseInt(_columnField.getText().trim());
    }

    /**
     * Gets the name text field (trimmed)
     * 
     * @return The name text field
     */
    public String getNameField() {
        return _nameField.getText().trim();
    }

    /**
     * Gets the rows data of the dialog
     * 
     * @return The number of rows entered
     */
    public int getRowsField() {
        return Integer.parseInt(_rowField.getText().trim());
    }

    @Override protected boolean validateDialog() {

        // Get the name of the title
        String title = getNameField();

        // Set a basic flag to determine validity
        boolean isValid = false;

        try {

            // Get the contents of the setup view
            int rows = getRowsField();
            int columns = getColumnsField();
            int cellWidth = getCellWidthField();
            int cellHeight = getCellHeightField();

            // Set the validity flag
            isValid = title.length() > 0 && rows > 0 && columns > 0 && cellWidth > 0 && cellHeight > 0;	
        }
        catch(Exception exception) {
            Tracelog.log(Level.SEVERE, true, exception);
        }

        // If the setup form is invalid
        if(!isValid) {
            // Show an error dialog
            JOptionPane.showMessageDialog(
                this,
                Localization.instance().getLocalizedString(ResourceKeys.SetupError),
                Localization.instance().getLocalizedString(ResourceKeys.Error),
                JOptionPane.ERROR_MESSAGE
            );
        }

        // Return the validity flag
        return isValid;
    }
}