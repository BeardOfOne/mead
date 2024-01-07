package editor.menu;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import framework.core.factories.AbstractFactory;
import framework.core.factories.AbstractSignalFactory;
import framework.core.factories.ModelFactory;
import framework.core.factories.ViewFactory;
import framework.core.navigation.AbstractMenuContainer;
import framework.core.navigation.AbstractMenuItem;
import framework.core.navigation.MenuBuilder;
import framework.utils.globalisation.Localization;

import editor.models.TileMapModel;
import editor.views.ProjectView;
import resources.ResourceKeys;

/**
 * The tile layers menu item
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 */
public class ShowTileMapMenu extends AbstractMenuContainer {
    
    public class TileMapMenuEntry extends AbstractMenuItem {
        
        private final TileMapModel _tileMapModel;
        
        public TileMapMenuEntry(JComponent parent, TileMapModel tileMapModel) {
            super(new JMenuItem(tileMapModel.getName()), parent);
            _tileMapModel = tileMapModel;
        }

        @Override protected void onExecute(ActionEvent actionEvent) {
            ViewFactory factory = AbstractSignalFactory.getFactory(ViewFactory.class);
            if(factory != null) {

                ProjectView view = factory.get(ProjectView.class);

                // Get the visible and bounds rectangle
                Rectangle visible = view.getVisibleRect();

                // Put the visibility to the top-left
                visible.x = _tileMapModel.getXCoordinate();
                visible.y = _tileMapModel.getYCoordinate();

                // Scroll to the new viewport
                view.scrollRectToVisible(visible);
            }
        }
    }
    
    /**
     * Constructs a new instance of this class type
     * 
     * @param parent The parent component to this menu entity
     */
    public ShowTileMapMenu(JComponent parent) {
        super(Localization.instance().getLocalizedString(ResourceKeys.ShowTileMap), parent);
    }
    
    @Override protected void onLoad() {
        getComponent().removeAll();
        if(visibility()) {
            MenuBuilder builder = MenuBuilder.start(getComponent());
            for(TileMapModel tileMapModel : AbstractFactory.getFactory(ModelFactory.class).getAll(TileMapModel.class)) {
                builder.addMenuItem(new TileMapMenuEntry(getComponent(), tileMapModel));
            }
        }
    }

    @Override protected boolean isEnabled() {
        return AbstractSignalFactory.isRunning();
    }
    
    @Override protected boolean visibility() {
        return isEnabled() && !AbstractFactory.getFactory(ModelFactory.class).getAll(TileMapModel.class).isEmpty();
    }
}