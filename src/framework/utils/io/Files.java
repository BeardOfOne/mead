package framework.utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Helper class for managing files
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public class Files {

    /**
     * Copies the contents of the source location file to the specified destination
     * 
     * @param source The source absolute path including the file name and extension
     * @param destination The destination absolute path (no file name)
     *
     * @return The success of the operation
     * 
     */
    @SuppressWarnings("resource")
    public static boolean copyFile(String source, String destination) {

        // Hold a reference to the source file
        File sourceFile = new File(source);

        // If the source file doesn't exist then there is an error
        if(!sourceFile.exists()) {
            // print error message and return false
            System.out.println(String.format("Error: Source file %s doesn't exist!", source));
            return false;
        }

        // Hold a reference to the destination file
        File destinationFile = new File(destination);

        // If the destination file does not exist then create it
        if(!destinationFile.exists()) {
            try {
                destinationFile.getParentFile().mkdirs();
                // Create the file
                destinationFile.createNewFile();
            } catch (IOException ioException) {
                // Print the error and return false
                ioException.printStackTrace();
                return false;
            }
        }

        // Open a source and destination channel
        try(FileChannel sourceChannel  = new FileInputStream(sourceFile).getChannel();  FileChannel destinationChanel = new FileOutputStream(destinationFile).getChannel()) {
            // Use the destination channel and transfer the contents from the source channel
            destinationChanel.transferFrom(sourceChannel, 0, sourceChannel.size());  	
        }
        catch(Exception exception) {
            // Print error and return false
            exception.printStackTrace();
            return false;
        }

        return true;
    }
}
