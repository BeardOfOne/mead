package engine.communication.external.builder;

import engine.communication.external.builder.IBuilder;

public final class Director {

	private IBuilder _builder;
		
	public void setBuilder(IBuilder builder) {
		_builder = builder;
	}
	
	public boolean construct() {
		if(_builder != null && _builder.buildDialog()) {
			
			// Build the setup phase
			_builder.buildSetup();
			
			// Build the content phase
			_builder.buildContent();
			
			try {
				// Finalize the build operation
				_builder.buildFinish();	
				
				return true;
			}
			catch(Exception exception) {
				exception.printStackTrace();
			}
		}
		
		return false;
	}
}