package engine.api;

public interface IModel extends IDestructor {
	public void addReceiver(IReceiver receiver);
	public void removeReciever(IReceiver receiver);
}
