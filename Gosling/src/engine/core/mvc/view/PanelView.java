package engine.core.mvc.view;

import javax.swing.JPanel;

import engine.api.IView;

public abstract class PanelView extends JPanel implements IView {
	
	private final ViewProperties properties = new ViewProperties();

	public PanelView() {
		registerHandlers();
	}
	
	@Override public ViewProperties getViewProperties() {
		return properties;
	}
	
	@Override public void dispose() {
		properties.dispose();
	}
	
	protected void registerHandlers() {
	}
}