package editor.menu;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ViewFactory;
import framework.core.navigation.AbstractMenuItem;
import framework.utils.globalisation.Localization;

import editor.views.ProjectView;
import resources.ResourceKeys;

/**
 * The tile layers menu item
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public class ResetPerspective extends AbstractMenuItem {

    /**
     * Constructs a new instance of this class type
     * 
     * @param parent The parent component to this menu entity
     */
    public ResetPerspective(JComponent parent) {
        super(new JMenuItem(Localization.instance().getLocalizedString(ResourceKeys.ResetPerspective)), parent);
    }

    @Override public void onExecute(ActionEvent actionEvent) {

        ViewFactory factory = AbstractSignalFactory.getFactory(ViewFactory.class);
        if(factory != null) {

            ProjectView view = factory.get(ProjectView.class);

            // Get the visible and bounds rectangle
            Rectangle visible = view.getVisibleRect();

            // Put the visibility to the top-left
            visible.x = 0;
            visible.y = 0;

            // Scroll to the new viewport
            view.scrollRectToVisible(visible);
        }
    }

    @Override public boolean isEnabled() {
        return AbstractSignalFactory.isRunning();
    }
}