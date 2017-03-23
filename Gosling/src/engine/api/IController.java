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

import engine.util.event.ISignalReceiver;

public interface IController extends IDestructor, ISignalReceiver {
	
	public final class ControllerProperties implements IDestructor {
		
		private IView _view;
		
		@Override public void dispose() {
			_view.dispose();
		}
		
		public final void setView(IView view) {
			this._view = view;
		}
		
		public IView getView() {
			return _view;
		}
		
		public boolean isViewVisible() {
			return getView().getContainerClass().isVisible();
		}
		
		public <T extends IView> T getView(Class<T> viewType) {
			return (T)getView();
		}

		@Override public void flush() {
			
		}
	}
	
	public ControllerProperties getControllerProperties();
}