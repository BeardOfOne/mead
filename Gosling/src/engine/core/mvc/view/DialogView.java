package engine.core.mvc.view;

import javax.swing.JDialog;

public abstract class DialogView extends BaseView<JDialog> {
	public DialogView() {
		super(new JDialog());
	}
}