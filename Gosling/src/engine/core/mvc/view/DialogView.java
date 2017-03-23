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

package engine.core.mvc.view;

import java.awt.Window;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;

import engine.api.IView;
import engine.util.event.ISignalListener;

public abstract class DialogView extends JDialog implements IView {
	
	private final Map<String, ISignalListener> SignalListeners = new HashMap<>();
	private final ViewProperties _properties = new ViewProperties();
	
	public DialogView(Window parent, String title, int width, int height) {
		super(parent, title);
		setSize(width, height);
		registerListeners();
	}
		
	@Override public final ViewProperties getViewProperties() {
		return _properties;
	}	
	
	@Override public void dispose() {
		_properties.dispose();	
		IView.super.dispose();
	}
	
	@Override public void render() {
		IView.super.render();
	}
	
	@Override final public Map<String, ISignalListener> getSignalListeners() {
		return SignalListeners;
	}
	
	@Override public void registerListeners() {
	}
}