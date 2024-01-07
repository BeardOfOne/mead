package editor.menu;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JMenuItem;

import framework.communication.external.builder.Director;
import framework.core.factories.AbstractFactory;
import framework.core.factories.ModelFactory;
import framework.core.navigation.AbstractMenuItem;
import framework.utils.globalisation.Localization;

import editor.models.TileMapModel;
import editor.persistance.builder.ExportDataBuilder;
import resources.ResourceKeys;

/**
 * The export data menu item 
 * 
 * @author {@literal Daniel Ricci {@literal <thedanny09@icloud.com>}}
 *
 */
public class ExportDataMenuItem extends AbstractMenuItem {

    /**
     * Constructs a new instance of this class type
     * 
     * @param parent The parent component to this menu entity
     */
    public ExportDataMenuItem(JComponent parent) {
        super(new JMenuItem(Localization.instance().getLocalizedString(ResourceKeys.ExportData)), parent);
    }

    @Override public void onExecute(ActionEvent actionEvent) {
        Director exportDirector = new Director(new ExportDataBuilder());
        exportDirector.construct();
    }

    @Override public boolean isEnabled() {
        return !AbstractFactory.getFactory(ModelFactory.class).getAll(TileMapModel.class).isEmpty();
    }
}