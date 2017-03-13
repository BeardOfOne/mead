package engine.util.event;

import engine.api.IModel;

public final class ModelEvent extends SignalEvent {

	private final IModel _model;
	
	public ModelEvent(Object sender, String operationName, IModel model) { super(sender, operationName);
		this._model = model;
	}
	
	public <T extends IModel> T getModel(Class<T> modelType) {
		return (T)_model;
	}
}
