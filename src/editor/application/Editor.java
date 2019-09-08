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

package editor.application;

import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;

import javax.swing.WindowConstants;

import framework.core.navigation.MenuBuilder;
import framework.core.system.Application;
import framework.core.system.EngineProperties;
import framework.core.system.EngineProperties.Property;
import framework.utils.globalisation.Localization;

import editor.menu.AboutMenuItem;
import editor.menu.AllViewsMenuItem;
import editor.menu.ExitMenuItem;
import editor.menu.ExportDataMenuItem;
import editor.menu.ExportImagesMenuItem;
import editor.menu.ImportImageMenuItem;
import editor.menu.LoadMenuItem;
import editor.menu.ProjectMenuItem;
import editor.menu.ProjectSettingsMenuItem;
import editor.menu.PropertiesMenuItem;
import editor.menu.ResetPerspective;
import editor.menu.SaveMenuItem;
import editor.menu.ShowTileMapMenu;
import editor.menu.TileLayersMenuItem;
import editor.menu.TileMapMenuItem;
import editor.menu.TileMapSettingsMenuItem;
import resources.ResourceKeys;

/**
 * This is the main application, the main method resides within this class
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class Editor extends Application {

    /**
     * The default application name of this application
     */
    private String _applicationName;

    /**
     * Constructs a new instance of this class type
     * 
     * @param isDebug TRUE if the application is in debug mode, FALSE otherwise
     */
    public Editor(boolean isDebug) {
        super(isDebug); 
        
        // Pressing on the close button won't do it's default action
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // Set the default size of the application to be the size of the screen
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();  
        setSize(env.getMaximumWindowBounds().width, env.getMaximumWindowBounds().height);
    }

    /**
     * The main method entry-point for the application
     * 
     * @param args The outside argument / command line argument
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override public void run() {
                boolean debugMode = false;
                for(String arg : args) {
                    if(arg.trim().equalsIgnoreCase("debug")) {
                        debugMode = true;
                        break;
                    }
                }
                new Editor(debugMode);
            }
        });
    }

    /**
     * Populates the file menu
     */
    private void populateFileMenu() {

        MenuBuilder rootBuilder = MenuBuilder.start(getJMenuBar())
                .addMenu(Localization.instance().getLocalizedString(ResourceKeys.File))
                .addMenuItem(ProjectMenuItem.class)
                .addMenuItem(TileMapMenuItem.class)
                .addSeparator()
                .addMenuItem(LoadMenuItem.class)
                .addMenuItem(SaveMenuItem.class)
                .addSeparator();

        rootBuilder.addBuilder(MenuBuilder.start(rootBuilder)
                .addMenu(Localization.instance().getLocalizedString(ResourceKeys.Import))
                .addMenuItem(ImportImageMenuItem.class));

        rootBuilder.addBuilder(MenuBuilder.start(rootBuilder)
                .addMenu(Localization.instance().getLocalizedString(ResourceKeys.Export))
                .addMenuItem(ExportDataMenuItem.class)
                .addSeparator()
                .addMenuItem(ExportImagesMenuItem.class))
        .addSeparator();

        rootBuilder.addMenuItem(ExitMenuItem.class);
    }

    /**
     * Populates the edit menu
     */
    private void populateEditMenu() {
        MenuBuilder.start(getJMenuBar())
        .addMenu(Localization.instance().getLocalizedString(ResourceKeys.Edit))
            .addMenuItem(ProjectSettingsMenuItem.class)
            .addMenuItem(TileMapSettingsMenuItem.class);
    }
    
    /**
     * Populates the view menu
     */
    private void populateViewMenu() {
        MenuBuilder.start(getJMenuBar())
        .addMenu(Localization.instance().getLocalizedString(ResourceKeys.View))
        .addMenuItem(AllViewsMenuItem.class)
        .addSeparator()
        .addMenuItem(TileLayersMenuItem.class)
        .addMenuItem(PropertiesMenuItem.class)
        .addSeparator()
        .addMenu(ShowTileMapMenu.class)
        .addMenuItem(ResetPerspective.class);
    }

    /**
     * Populates the help menu
     */
    private void populateHelpMenu() {
        MenuBuilder.start(getJMenuBar())
        .addMenu(Localization.instance().getLocalizedString(ResourceKeys.Help))
        .addMenuItem(AboutMenuItem.class);
    }

    @Override protected void onBeforeEngineDataInitialized() {
        EngineProperties.instance().setProperty(Property.LOCALIZATION_PATH_CVS, "resources/resources.csv");
        //EngineProperties.instance().setProperty(Property.LOG_DIRECTORY, System.getProperty("user.home") + File.separator + "desktop" + File.separator);
        EngineProperties.instance().setProperty(Property.ENGINE_OUTPUT, Boolean.toString(false));
        EngineProperties.instance().setProperty(Property.SUPPRESS_SIGNAL_REGISTRATION_OUTPUT, Boolean.toString(true));
        EngineProperties.instance().setProperty(Property.DISABLE_TRANSLATIONS_PLACEHOLDER, Boolean.toString(true));
        
        // Set the title of the application
        _applicationName = Localization.instance().getLocalizedString(ResourceKeys.ApplicationTitle);
        setTitle(_applicationName);
    }

    @Override protected void onApplicationShown() {
    }

    @Override protected void onWindowInitialized() {
        super.onWindowInitialized();
        populateFileMenu();
        populateEditMenu();
        populateViewMenu();
        populateHelpMenu();
    }

    @Override public String getTitle() { 
        return _applicationName; 
    }
}