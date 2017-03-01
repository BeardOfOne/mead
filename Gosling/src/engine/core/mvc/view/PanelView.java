package engine.core.mvc.view;

import java.awt.Container;

import javax.swing.JPanel;

import engine.api.IView;
import engine.api.IView.ViewProperties;

public abstract class PanelView extends JPanel implements IView {
	
	private final ViewProperties properties = new ViewProperties();

	@Override public ViewProperties getViewProperties() {
		return properties;
	}
	
	@Override public Container getContainerClass() {
		return this;
	}
}