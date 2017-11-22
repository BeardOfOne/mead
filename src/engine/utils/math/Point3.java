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

package engine.utils.math;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 * Point class that represents a three-dimensional location
 * 
 * @author Daniel Ricci {@literal <thedanny09@gmail.com>}
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Point3 extends Point2 {
	
	/**
	 * z-axis value
	 */
	@XmlAttribute(name = "z")
	private double _z;
	
	/**
	 * Default constructor as per serialization requirements
	 */
	public Point3() {
	}

	/**
	 * This overloaded constructor will initialize the points to their 
	 * specified values
	 * 
	 * @param x The x-axis value
	 * @param y The y-axis value
	 * @param z The z-axis value
	 */
	public Point3(double x, double y, double z) {
		super(x, y);
		_z = z;
	}
	
	/**
	 * @return Gets the z value
	 */
	public final double getZ() {
		return _z;
	}
	
	/**
	 * @return An empty point of this type
	 */
	public static Point3 Empty() {
		return new Point3(0,0,0);
	}

}