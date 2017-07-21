package engine.core.graphics;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class CheckBoxListCellRenderer<E> extends JCheckBox implements ListCellRenderer<E> {

	@Override public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
		setComponentOrientation(list.getComponentOrientation());

		setFont(list.getFont());
		setText(String.valueOf(value)) ;

		setBackground(list.getBackground());
		setForeground(list.getForeground());

		setSelected(isSelected);
		setEnabled(list.isEnabled());

		return this;
	}
}