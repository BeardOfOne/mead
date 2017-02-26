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

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import engine.api.IController;
import engine.api.IView;

abstract class BaseView<T extends Container> implements IView {

	private IController _controller;
	private T _entity;
	
	public BaseView(T entity){
		_entity = entity;
		register();
	}
	
	@Override public Map<String, ActionListener> getRegisteredOperations() {
		return null;
	}
	
	@Override public final void executeRegisteredOperation(Object sender, String operation) {
		Map<String, ActionListener> operations = getRegisteredOperations();
		ActionListener event;
		if(operations != null && (event = operations.get(operation)) != null) {
			event.actionPerformed(new ActionEvent(sender, 0, null));	
		}
	}
	
	@Override public void dispose() {		
		_controller.dispose();
		_controller = null;
		
		_entity.removeAll();
		_entity = null;
	}
	
	public final <U extends IController> U getController(Class<T> controllerClass) {
		return _controller != null ? (U)_controller : null;
	}
	
	protected final void setController(IController controller) {
		_controller = controller;
	}
	
	public <U extends Container> U getEntity(Class<U> entityClass) { 
		return (U)_entity; 
	}

	@Override public void register(){
	}

	@Override public void render() {
	}
}