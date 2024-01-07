package framework.core.factories;

import java.util.List;
import java.util.Queue;
import java.util.logging.Level;

import framework.api.IModel;
import framework.communication.internal.signal.arguments.UUIDEventArgs;
import framework.utils.logging.Tracelog;

/**
 * The model factory used to create model type resources
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public final class ModelFactory extends AbstractSignalFactory<IModel> {

    @Override public <U extends IModel> U add(U resource, boolean isShared) {

        // Add the actual resource
        super.add(resource, isShared);

        Queue<U> cachedResources = (Queue<U>) _cache.get(resource.getClass());
        if(cachedResources != null && !cachedResources.isEmpty()) {
            U cachedResource = cachedResources.remove();
            if(cachedResource != null) {

                // Copy the data that has been cached over to the model
                resource.copyData(cachedResource);

                // Perform a refresh of the model to show the updated contents
                resource.refresh();

                // Remove the cache entry if there are no more elements left
                if(cachedResources.isEmpty()) {
                    Tracelog.log(Level.INFO, false, "Removing the key " + resource.getClass() + " from the cache");
                    _cache.remove(resource.getClass());
                }
            }
            else {
                Tracelog.log(Level.SEVERE, false, "Could not inject the specified model");
            }
        }

        return resource;
    }

    /**
     * Performs a selective multicast on the specified class type
     * 
     * Note: This performs a selective multicast using the specified UUID
     *  
     * @param classType The class type to multicast on
     * @param event The event to pass to each resource
     * @param <U> A type extending The class template type
     * @param <T> A type extending any class type
     */
    public <U extends IModel, T extends Object> void selectiveMulticastSignal(Class<U> classType, UUIDEventArgs event) {

        // Get the list of models that have currently been created
        List<IModel> resources = _privateSignals.get(classType);

        // Verify that the list if valid
        if(resources != null) {

            // Unwrap the event and cast it to a model event.
            // This is a strong assumption, but right now this is what
            // the assumption is when sending out a signal using this factory.
            UUIDEventArgs uuidEvent = event;

            // For every resource that has been created
            for(IModel resource : resources) {
                if(uuidEvent.getIdentifiers().contains(resource.getUUID())) {
                    resource.invokeSignal(event);
                }
            }
        }
    }
    
    @Override protected boolean isPersistent() {
        return false;
    }
}