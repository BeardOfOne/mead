package engine.communication.internal.signal;

public interface IDataPipeline<T extends Object> {
	public void pipeData(T data);
}
