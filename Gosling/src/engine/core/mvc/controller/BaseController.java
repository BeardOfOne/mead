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

package engine.core.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import engine.api.IController;
import engine.api.IView;
import engine.util.event.ISignalListener;

public abstract class BaseController implements IController  {

	protected final Map<String, ISignalListener> SignalListenerMap = new HashMap<>();
	
	private IView _view;
	
	public <T extends IView> BaseController(T view) {
		_view = view;
		registerHandlers();
	}
	
	protected final IView getView() {
		return _view;
	}
	
	@Override public void dispose() {
		_view.dispose();
		_view = null;		
	}
	
	protected final String unregisterListener(ISignalListener listener) {
		for(Map.Entry<String, ISignalListener> kvp : SignalListenerMap.entrySet()) {
			if(kvp.getValue() == listener) {
				SignalListenerMap.remove(kvp.getKey());
				return kvp.getKey();
			}
		}
		return null;
	}
	
	protected final void registerListener(String signalName, ISignalListener listener) {
		if(!SignalListenerMap.containsKey(signalName)) {
			SignalListenerMap.put(signalName, listener);
		}
	}
		
	protected void registerHandlers() {
	}
}