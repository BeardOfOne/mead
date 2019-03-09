/**
 * Daniel Ricci {@literal <thedanny09@icloud.com>}
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

package framework.core.graphics;

import java.awt.Graphics;

/**
 * This interface defines methods for compenents that provide the ability to render content
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 */
public interface IRenderer {

    /**
     * The default render limit value for this panel
     */
    public static final int RENDER_LIMIT_DEFAULT_VALUE = -1;
    
    public class RendererProperties {
        /**
         * The x-coordinate for rendering 
         */
        public int x = -1;
        
        /**
         * The y-coordinate for rendering
         */
        public int y = -1;
        
        /**
         * The width for rendering
         */
        public int width = -1;
        
        /**
         * The height for rendering
         */
        public int height = -1;

        /**
         * Indicates if the renderer can perform a draw call
         * 
         * Note: If the draw call cannot be performed, then it is
         *       up to the renderer implementation to detail what should
         *       happen.
         */
        public boolean canDraw = true;
        
        public void reset() {
            x = -1;
            y = 1;
            width = -1;
            height = -1;
        }
    }
    
    /**
     * Renders the specified renderable data using the specified graphics context
     * 
     * @param renderableData The renderable data
     * @param context The graphics context
     */
    public void render(IRenderable renderableData, Graphics context);
   
    /**
     * A hook into the graphics context and the renderable data before the draw call is initiated
     *
     * @param renderableData The renderable data entity
     * @param context The graphics context
     */
    default public void preProcessGraphics(IRenderable renderableData, Graphics context) {
    }
}