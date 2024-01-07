package editor.views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import framework.core.mvc.view.DialogView;
import framework.core.mvc.view.layout.SpringLayoutHelper;
import framework.core.system.Application;
import framework.utils.globalisation.Localization;

import resources.ResourceKeys;

/**
 * This dialog allows the user to configure the project's initial parameters
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public final class ProjectDialogView extends DialogView {

    /**
     * This panel holds the layout of all configuration fields
     */
    private JPanel _dialogLayout = new JPanel(new SpringLayout());

    /**
     * This panel holds the OK and Cancel buttons
     */
    private JPanel _confirmationPanel = new JPanel();

    /**
     * The OK button to accept the contents entered in this dialog
     */
    private JButton _okButton = new JButton(Localization.instance().getLocalizedString(ResourceKeys.OK));

    /**
     * The cancel button to cancel this dialogs operation
     */
    private JButton _cancelButton = new JButton(Localization.instance().getLocalizedString(ResourceKeys.Cancel));

    /**
     * The title text field
     */
    private JTextField _nameField = new JTextField(20);

    /**
     * Constructs a new instance of this class type
     */
    public ProjectDialogView() {
        super(Application.instance, Localization.instance().getLocalizedString(ResourceKeys.NewProject), 400, 100);

        setAutomaticDialogCentering(true);
        
        // Set the layout manager of this view
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        // Set the modal state
        setModal(true);

        // Make sure it is always on top
        setAlwaysOnTop(true);

        // Do not allow it to be resized
        setResizable(false);

        // Ensures that the dialog is centered on the screen w.r.t its owner
        setLocationRelativeTo(null);
        
        // NAME
        _dialogLayout.add(new JLabel(Localization.instance().getLocalizedString(ResourceKeys.ProjectName)));
        _nameField.setMaximumSize(_nameField.getPreferredSize());
        _dialogLayout.add(_nameField);

        // Make the grid compact 
        SpringLayoutHelper.makeCompactGrid(_dialogLayout, 1, 2, 6, 6, 10, 0);

        // Add our confirmation buttons to our panel
        _confirmationPanel.add(_okButton);
        _confirmationPanel.add(_cancelButton);

        // Add our layouts to this view
        add(_dialogLayout);
        add(_confirmationPanel);

        _nameField.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_ENTER) {
                    _okButton.doClick();
                }
            }
        });
        _okButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if(validateDialog()) {
                    setDialogResult(JOptionPane.OK_OPTION);
                    setVisible(false);
                }
            }
        });
        _okButton.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_ENTER) {
                    _okButton.doClick();
                }
            }
        });
        _cancelButton.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                setDialogResult(JOptionPane.CANCEL_OPTION);
                setVisible(false);
            }
        });
        _cancelButton.addKeyListener(new KeyAdapter() {
            @Override public void keyReleased(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_SPACE || event.getKeyCode() == KeyEvent.VK_ENTER) {
                    _cancelButton.doClick();
                }
            }
        });

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
     * Sets the name field
     *
     * @param name The name
     */
    public void setNameField(String name) {
        _nameField.setText(name);
    }

    @Override protected boolean validateDialog() {

        // If the setup form is invalid
        if(getNameField().trim().isEmpty()) {
            // Show an error dialog
            JOptionPane.showMessageDialog(
                this,
                Localization.instance().getLocalizedString(ResourceKeys.SetupError),
                Localization.instance().getLocalizedString(ResourceKeys.Error),
                JOptionPane.ERROR_MESSAGE
            );
            
            return false;
        }

        // Return the validity flag
        return true;
    }
}