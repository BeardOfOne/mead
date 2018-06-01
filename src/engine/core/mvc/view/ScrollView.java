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

package engine.core.mvc.view;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.Scrollable;
import javax.swing.SwingConstants;

/**
 * This panel gives the ability to be able to scroll content that is placed within this panel.
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 *
 */
public abstract class ScrollView extends PanelView implements Scrollable, MouseMotionListener {

    private int _unitIncrement = 20;

    /**
     * Constructs a new instance of this class type
     */
    public ScrollView() {
        setOpaque(true);
        setAutoscrolls(true);
        addMouseMotionListener(this);
    }

    public void setMaxUnitIncrement(int pixels) {
        _unitIncrement = pixels;
    }

    @Override public void mouseDragged(MouseEvent e) {
        Rectangle r = new Rectangle(e.getX(), e.getY(), 1, 1);
        scrollRectToVisible(r);
    }

    @Override public void mouseMoved(MouseEvent e) {
    }

    @Override public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        if (orientation == SwingConstants.HORIZONTAL) {
            return visibleRect.width - _unitIncrement;
        } else {
            return visibleRect.height - _unitIncrement;
        }
    }

    @Override public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }

    @Override public Dimension getPreferredSize() {
        return new Dimension(65536, 65536);
    }

    @Override public boolean getScrollableTracksViewportWidth() {
        return false;
    }

    @Override public boolean getScrollableTracksViewportHeight() {
        return false;
    }

    @Override public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        // Get the current position.
        int currentPosition = 0;

        if (orientation == SwingConstants.HORIZONTAL) {
            currentPosition = visibleRect.x;
        } else {
            currentPosition = visibleRect.y;
        }

        // Return the number of pixels between currentPosition
        // and the nearest tick mark in the indicated direction.
        if (direction < 0) {
            int newPosition = currentPosition - (currentPosition / _unitIncrement) * _unitIncrement;
            return (newPosition == 0) ? _unitIncrement : newPosition;
        } else {
            return ((currentPosition / _unitIncrement) + 1) * _unitIncrement - currentPosition;
        }
    }

    @Override public void onViewInitialized() {
    }

    @Override public void initializeComponentBindings() {
    }

    @Override public void clear() {
    }
}