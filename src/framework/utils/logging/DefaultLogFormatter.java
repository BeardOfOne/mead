package framework.utils.logging;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * The default log formatter for the logging functionality in the engine.
 * 
 * @author Daniel Ricci {@literal <thedanny09@icloud.com>}
 *
 */
public class DefaultLogFormatter extends Formatter {

    @Override public String format(LogRecord record) {
        return record.getMessage() + System.lineSeparator();
    }
}