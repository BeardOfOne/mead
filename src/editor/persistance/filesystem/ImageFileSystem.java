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

package editor.persistance.filesystem;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.logging.Level;

import javax.imageio.ImageIO;

import editor.models.TileMapModel;
import editor.models.TileModel;
import framework.communication.external.filesystem.AbstractFileSystem;
import framework.utils.io.Paths;
import framework.utils.logging.Tracelog;

/**
 * Image file system for handling images
 * 
 * @author {@literal Daniel Ricci <thedanny09@gmail.com>}
 */
public final class ImageFileSystem extends AbstractFileSystem {

    /**
     * Constructs a new instance of this class type
     * 
     * @param file The path associated to this file system
     */
    public ImageFileSystem(File file) {
        super(file);
    }

    @Override public String serialize() {

        // Ensure that the file provided is a directory
        if(!_file.isDirectory()) {
            System.out.print("Error: A directory must be provided to this filesystem, received " + _file.getAbsolutePath().toString());
            return null;
        }

        List<TileMapModel> tileMapModels = getData(TileMapModel.class);
        for(int i = 0, iSize = tileMapModels.size(); i < iSize; ++i) {

            File file = new File(_file.getAbsolutePath() + File.separator + tileMapModels.get(i).getName());
            file.mkdir();

            List<TileModel> tileModels = tileMapModels.get(i).getTiles();
            for(int j = 0, jSize = tileModels.size(); j < jSize; ++j) {
                Image image = tileModels.get(j).getImage();
                if(image != null) {
                    // If the image is not a buffered image then do not process the image, it needs to be of that type
                    if(!(image instanceof BufferedImage)) {
                        Tracelog.log(Level.SEVERE, true, "Error: Cannot export image for tilemap " + tileModels.get(j).toString() + ", wrong image format");
                        continue;
                    }

                    try {
                        // Write the contents of the image to disk
                        ImageIO.write(
                                (BufferedImage)image,
                                Paths.cleanExtensionMark(EXTENSION_PNG, true),
                                new File(file.getPath() + File.separator + Integer.toString(j) + EXTENSION_PNG)
                                );						
                    }
                    catch(Exception exception) {
                        Tracelog.log(Level.SEVERE, true, exception);
                    }
                }
            }
        }

        return null;
    }
}	