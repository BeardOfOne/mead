package engine.core.mvc.view;

import java.awt.Container;

import javax.swing.JPanel;

import engine.api.IView;

public abstract class PanelView extends JPanel implements IView {
	
	private final ViewProperties properties = new ViewProperties();

	@Override public ViewProperties getViewProperties() {
		return properties;
	}
	
	@Override public Container getContainerClass() {
		return this;
	}
	
	@Override public void dispose() {
		properties.dispose();
	}
}