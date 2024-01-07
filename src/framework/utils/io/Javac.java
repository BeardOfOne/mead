package framework.utils.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import framework.utils.logging.Tracelog;

/**
 * This class provides runtime source compilation functionality.
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public class Javac extends SimpleJavaFileObject {

    /**
     * Constructs a new instance of this class type
     * 
     * @param uri The uri of the location of the source file
     */
    private Javac(URI uri) {
        super(uri, Kind.SOURCE);
    }

    /**
     * Compiles a source file specified through a uri, this means that the file should exist
     * on disk somewhere
     * 
     * @param uri The uri of the source file
     * 
     * @return A list of files associated to the compilation output of the uri.
     * 
     * @throws IOException If there is an internal error with referencing the uri through a File object
     */
    public static List<File> compile(URI uri) throws IOException {

        // Get a reference to the system compiler
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if(compiler == null) {
            Tracelog.log(Level.SEVERE, false, "Cannot find any Java Compiler...");
        }
        
        // Create a new diagnostic collector
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();

        // Get a reference to the file manager from the compiler
        StandardJavaFileManager fileManager = compiler.getStandardFileManager(diagnostics, null, null);

        // Set the location of where the class output files should be
        fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Collections.singleton(new File(uri).getParentFile()));

        // Create a task using the compiler resource
        CompilationTask task = compiler.getTask(
                null, 
                fileManager, 
                diagnostics, 
                null, 
                null, 
                Collections.singleton(new Javac(uri))
                );

        // Perform the compilation operation
        boolean success = task.call();

        // Go through the diagnostics if something bad happened
        for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
            System.out.println("Error: Could not properly compile the specified resource");
            System.out.println("Error: Code = " + diagnostic.getCode());
            System.out.println("Error: Kind = " + diagnostic.getKind());
            System.out.println("Error: Position = " + diagnostic.getPosition());
            System.out.println("Error: Start Position = " + diagnostic.getStartPosition());
            System.out.println("Error: End Position = " + diagnostic.getEndPosition());
            System.out.println("Error: Source = " + diagnostic.getSource());
            System.out.println("Error: Message = " + diagnostic.getMessage(null));
        }

        // Output information about the compilation outcome
        System.out.println("Info: " + uri.toString() + (success ? " successfully compiled" : "failed compilation!!!"));

        List<File> classFiles = new ArrayList();
        for(JavaFileObject fileObject : fileManager.list(StandardLocation.CLASS_OUTPUT, "", Collections.singleton(Kind.CLASS), true)) {

            // Create a logical file reference to the compiles .class output file
            File file = new File(fileObject.getName());

            // Specify that the file should be removed when the application running is terminated
            file.deleteOnExit();

            // Add the .class file to our list
            classFiles.add(file);

            // Specify some output information
            System.out.println("Info: " + fileObject.getName() + " has been generated as a result of the compilation process.");
        }

        // Return the list of output class files generated as a result of
        // compiling the specified URI source file
        return classFiles;	
    }

    @Override public CharSequence getCharContent(boolean ignoreEncodingErrors) {

        String source = null;

        // Get a reference to the source file specified by the uri
        try(BufferedReader reader = new BufferedReader(new FileReader(new File(this.uri)))) {

            // Read the uri file and get all the string contents
            source = reader.lines().reduce("", String::concat);
        } 
        catch (Exception exception) {
            exception.printStackTrace();
        }

        // return the entirety of the uri file specified
        return source;
    }
}
