package engine.core.mvc.view;

import java.awt.Window;

import javax.swing.JDialog;

import engine.api.IView;

public abstract class DialogView extends JDialog implements IView {
	
	private final ViewProperties properties = new ViewProperties();
	
	public DialogView(Window parent, String title, int width, int height) {
		super(parent, title);
		setSize(width, height);
	}
		
	@Override public final ViewProperties getViewProperties() {
		return properties;
	}	
	
	@Override public void render() {
		IView.super.render();
	}
}