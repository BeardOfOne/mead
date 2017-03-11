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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Collectors;

import engine.api.IController;
import engine.api.IModel;
import engine.api.IView;

public abstract class BaseController implements IController  {

	private IView _view;
	private final Collection<IModel> _models = new ArrayList<>();
	
	public <T extends IView> BaseController(T view) {
		_view = view;
		register();
	}
	
	protected final IView getView() {
		return _view;
	}
	
	@Override public void dispose() {
		_view.dispose();
		_view = null;
		
		for(IModel model : _models) {
			model.dispose();
		}
		_models.clear();
	}
	
	protected final void addModel(IModel model) {
		model.addReceiver(_view);
		_models.add(model);
	}
	
	protected final void removeModel(IModel model) {
		_models.remove(model);
		model.removeReciever(_view);
	}
	
	protected <T extends IModel> Collection<IModel> getModels(Class<T> modelType) {
		return _models
			.stream()
			.filter(z -> z.getClass() == modelType)
			.collect(Collectors.toList());
	}
	
	protected Iterator<IModel> getModels() {
		return _models.iterator();
	}

	protected abstract void register();
}