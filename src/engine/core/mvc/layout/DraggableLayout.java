package engine.core.mvc.layout;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;

/**
 * This layout manager is used for handling draggable components
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
public class DraggableLayout implements LayoutManager {
	
	/**
	 * Indicates if the preffered size is to be used
	 */
	private boolean _usePreferredSize = true;

	/**
	 * Gets the layout size of the specified parent
	 * 
	 * @param parent The parent
	 * 
	 * @return The layout of the parent
	 */
	private Dimension getLayoutSize(Container parent)
	{
		Insets parentInsets = parent.getInsets();
		int x = parentInsets.left;
		int y = parentInsets.top;
		int width = 0;
		int height = 0;

		//  Get extreme values of the components on the container.
		//  The x/y values represent the starting point relative to the
		//  top/left of the container. The width/height values represent
		//  the bottom/right value within the container.

		for (Component component: parent.getComponents())
		{
			if (component.isVisible())
			{
				Point p = component.getLocation();
				Dimension d = getActualSize(component);
				x = Math.min(x, p.x);
				y = Math.min(y, p.y);
				width = Math.max(width, p.x + d.width);
				height = Math.max(height, p.y + d.height);
			}
		}

		// Width/Height is adjusted if any component is outside left/top edge

		if (x < parentInsets.left)
			width += parentInsets.left - x;

		if (y < parentInsets.top)
			height += parentInsets.top - y;

		//  Adjust for insets

		width += parentInsets.right;
		height += parentInsets.bottom;
		Dimension d = new Dimension(width, height);

		return d;
	}

	/**
	 * Gets the actual size of the specified component
	 * 
	 * @param component The component to get the size of 
	 * 
	 * @return The dimensions of the specified component
	 */
	private Dimension getActualSize(Component component) {
		if (_usePreferredSize) {
			return component.getPreferredSize();
		}

		Dimension dim = component.getSize();

		if (dim.width == 0 || dim.height == 0) {
			return component.getPreferredSize();
		}
		
		return dim;
	}

	@Override public void addLayoutComponent(String name, Component comp) {
	}

	@Override public void removeLayoutComponent(Component component) {
	}

	@Override public Dimension minimumLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			return preferredLayoutSize(parent);
		}
	}

	@Override public Dimension preferredLayoutSize(Container parent) {
		synchronized (parent.getTreeLock()) {
			return getLayoutSize(parent);
		}
	}

	@Override public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets parentInsets = parent.getInsets();
	
			int x = parentInsets.left;
			int y = parentInsets.top;
	
			//  Get x/y location of any component outside the bounds of the panel.
			//  All components will be adjust by the x/y values, if necessary.
	
			for (Component component: parent.getComponents())
			{
				if (component.isVisible())
				{
					Point location = component.getLocation();
					x = Math.min(x, location.x);
					y = Math.min(y, location.y);
				}
			}
	
			x = (x < parentInsets.left) ? parentInsets.left - x : 0;
			y = (y < parentInsets.top) ? parentInsets.top - y : 0;
	
			//  Set bounds of each component
	
			for (Component component: parent.getComponents())
			{
				if (component.isVisible())
				{
					Point p = component.getLocation();
					Dimension d = getActualSize(component);
	
					component.setBounds(p.x + x, p.y + y, d.width, d.height);
				}
			}
		}
	}
}