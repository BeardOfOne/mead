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

package engine.communication.internal.persistance;

import java.io.Serializable;

public interface IPersistant extends Serializable {
	
	/**
	 * Event string for registering to serialized events
	 */
	public static final String EVENT_SERIALIZE = "EVENT_SERIALIZE";
	
	default public String serialize() {
		return "";
	}
	
	//default public void writeObject(ObjectOutputStream out) throws IOException
	//{
	/*
	 * private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	    out.writeInt(1); // how many images are serialized?
	    ImageIO.write(_buffer, "png", out); // png is lossless
	}
	 */
	//}
}