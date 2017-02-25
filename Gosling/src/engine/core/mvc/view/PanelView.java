package engine.core.mvc.view;

import javax.swing.JPanel;

public abstract class PanelView extends BaseView<JPanel> {
	public PanelView() {
		super(new JPanel());
	}
}