/**
* Daniel Ricci <thedanny09@gmail.com>
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

package engine.api;

import java.awt.Container;

import engine.util.event.ISignalReceiver;

public interface IView extends IDestructor, ISignalReceiver {
	
	public class ViewProperties implements IDestructor {
		private IController _controller;		
		private boolean _hasRendered = false;
		
		@Override public void dispose() {
			_controller.dispose();
		}

		public final void setController(IController controller) {
			this._controller = controller;
		}
		
		public IController getController() {
			return _controller;
		}
		
		protected final void flagAsRendered() {
			_hasRendered = true;
		}
		
		public final boolean hasRendered() {
			return _hasRendered;
		}
	}
	
	default public <T extends Container> T getContainerClass() {
		return (T)this;
	}
	
	default public boolean hasRendered() {
		return getViewProperties().hasRendered();
	}
	
	default public void render() {
		getViewProperties().flagAsRendered();
	}
	
	public ViewProperties getViewProperties();	
}