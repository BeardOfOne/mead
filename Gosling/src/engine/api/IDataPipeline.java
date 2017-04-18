package engine.api;

public interface IDataPipeline<T> {
	public <U extends Object> void pipeData(Class<U> sender, T data);
}
