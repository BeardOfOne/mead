package engine.util.event;

public final class SignalEventFactory {
	private SignalEventFactory() {
	}
	
	public static <T extends Object> SignalEvent getSignalEvent(Class<T> type, T sender, String operationName) {
		if(sender == null) {
			return new NullEvent();
		}
		
		switch(type.getName()) {
		case ModelEvent.class.getName(): 
				return new ModelEvent<type>(sender, operationName);
		}
		//return new SignalEvent<Object>(sender, operationName)
	}
}
