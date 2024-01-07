package framework.core.physics;

import java.awt.Component;

/**
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 */
public interface ICollidable {
    public boolean isValidCollision(Component source);
    public void onCollisionStart(Component source);
    public void onCollisionStop(Component source);
}