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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;
import java.util.Vector;

import javax.swing.JPanel;

import engine.api.IInvokableView;
import engine.api.IView;
import engine.communication.internal.dispatcher.DispatcherOperation;
import engine.core.mvc.controller.BaseController;
import engine.core.mvc.model.GameModel;
import engine.factories.ControllerFactory;

public abstract class BaseView extends JPanel implements IView {

	private final Vector<BaseController> _controllers = new Vector<>();
	private final Vector<IInvokableView> _entities = new Vector<>();
	
	private BaseView(){
		register();
	}
	
	public BaseView(final BaseController controller) {			
		this();
		_controllers.add(controller);
	}
	
	public <T extends BaseController> BaseView(Class<T> controller, boolean shared) {
		this();
		setController(ControllerFactory.instance().get(controller, shared, this));
	}
	
	public final <T extends BaseController> T getController(Class<T> controllerClass) {	
		BaseController myController = null;
		for(BaseController controller : _controllers) {
			if(controller.getClass() == controllerClass) {
				myController = controller;
				break;
			}
		}
		return (T) myController;
	}
	
	protected final <T extends BaseController> void setController(T controller) {
		assert controller != null : "Cannot add null controller into baseview";
		if(!controllerExists(controller)) {
			_controllers.add(controller);
		}
	}
	
	@Override public Map<DispatcherOperation, ActionListener> getRegisteredOperations() {
		return null;
	}
		
	@Override public final void executeRegisteredOperation(Object sender, DispatcherOperation operation) {		
		Map<DispatcherOperation, ActionListener> operations = getRegisteredOperations();
		ActionListener event;
		if(operations != null && (event = operations.get(operation)) != null) {
			event.actionPerformed(new ActionEvent(sender, 0, null));	
		}
	}
	
	private boolean controllerExists(BaseController controller) {
		assert controller != null : "Cannot pass a null controller";
		boolean found = false;
		
		for(BaseController _controller : _controllers) {
			if(_controller.getClass() == controller.getClass()) {
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	protected final void addInvokableEntity(IInvokableView entity) {
		_entities.addElement(entity);
	}
	
	protected final <T extends IInvokableView> T getInvokableEntityResult(Class<T> entityClass) {
		T result = null;
		for(IInvokableView entity : _entities) {
			if(entity.getClass() == entityClass) {
				result = (T) entity;
				break;
			}
		}
		return result;
	}
	
	@Override public void register(){
	}
		
	@Override public void render(){
		for(IInvokableView entity : _entities) {
			entity.invoke();
		}
	}
	
	@Override public void refresh(GameModel model){
	}
				
	@Override public void dispose() {
		removeAll();
		_controllers.clear();
	}
}