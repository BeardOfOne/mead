package framework.core.navigation;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * Defines the abstract implementation for the menu container.  A menu container is similar 
 * in nature to a JMenu
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public class AbstractMenuContainer extends AbstractMenu {

    /**
     * Constructs a new instance of this type
     * 
     * @param text The text of the menu component that will be displayed
     * @param parent The parent component associated to this option menu
     */
    public AbstractMenuContainer(String text, JComponent parent) {
        super(new JMenu(text), parent);
    }

    /**
     * Sets the mnemonic of this menu component
     * 
     * @param mnemonic The mnemonic to set to this menu component
     */
    public void setMnemonic(int mnemonic) {
        super.getComponent(JMenu.class).setMnemonic(mnemonic);
    }

    /**
     * Resets this menu and its sub-items to their default states
     */
    public final void reset() {
        for(Component component : super.getComponent(JMenu.class).getMenuComponents()) {
            if(component instanceof JComponent) {
                JComponent jComponent = (JComponent) component;
                Object clientProperty = jComponent.getClientProperty(jComponent);
                if(clientProperty instanceof AbstractMenuItem) {
                    AbstractMenuItem menuItem = (AbstractMenuItem) jComponent.getClientProperty(jComponent);
                    menuItem.onReset();
                }
            }
        }
    }

    @Override protected void onInitialize() {
        super.getComponent(JMenu.class).addMenuListener(new MenuListener() {
            @Override public void menuSelected(MenuEvent e) {
                JMenu menu = (JMenu)e.getSource();
                for(Component component : menu.getMenuComponents()) {
                    if(component instanceof JComponent) {
                        JComponent jComponent = (JComponent) component;
                        Object clientProperty = jComponent.getClientProperty(jComponent);
                        if(clientProperty instanceof AbstractMenu) {
                            AbstractMenu itemComponent = (AbstractMenu) jComponent.getClientProperty(jComponent);
                            itemComponent.onLoad();
                            jComponent.setVisible(itemComponent.visibility());
                            jComponent.setEnabled(jComponent.isVisible() && itemComponent.isEnabled());
                        }
                    }
                }
            }
            @Override public void menuCanceled(MenuEvent e) {
            }
            @Override public void menuDeselected(MenuEvent e) {
            }
        });
    }

}