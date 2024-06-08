package framework.core.graphics;

import java.awt.Image;
import java.util.List;

public interface IRenderableContainer extends IRenderable {
	public List<IRenderable> getRenderableContents();

	@Override default Image getRenderableContent() {
		return null;
	}
}
