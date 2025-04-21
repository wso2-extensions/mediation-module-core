package org.wso2.carbon.module.core.writers;

import au.com.bytecode.opencsv.CSVWriter;
import org.wso2.carbon.module.core.exceptions.SimpleMessageContextException;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class CSVWriterWithQuote extends CSVWriter {
    private static final int INITIAL_STRING_SIZE = 1024;
    private final char separator;
    private final char quotechar;
    private final char escapechar;
    private final Writer writer;
    private final String lineEnd;

    public CSVWriterWithQuote(Writer writer, char separator, char quotechar, char escapechar, String lineEnd) {
        super(writer, separator, quotechar, escapechar, lineEnd);
        this.separator = separator;
        this.quotechar = quotechar;
        this.escapechar = escapechar;
        this.writer = writer;
        this.lineEnd = lineEnd;
    }

    @Override
    public void writeAll(List allLines) {
        writeAll((Iterable) allLines);
    }

    public void writeAll(Iterable<String[]> allLines) {
        StringBuilder sb = new StringBuilder(INITIAL_STRING_SIZE);
        try {
            for (String[] line : allLines) {
                writeNext(line, sb);
                sb.setLength(0);
            }
        } catch (IOException exception) {
            throw new SimpleMessageContextException("Error while writing CSV", exception);
        }
    }


    public void writeNext(String[] nextLine, Appendable appendable) throws IOException {
        if (nextLine == null) {
            return;
        }

        for (int i = 0; i < nextLine.length; i++) {

            if (i != 0) {
                appendable.append(separator);
            }

            String nextElement = nextLine[i];

            if (nextElement == null) {
                continue;
            }

            Boolean stringContainsSpecialCharacters = stringContainsSpecialCharacters(nextElement);

            if (stringContainsSpecialCharacters && quotechar != NO_QUOTE_CHARACTER) {
                appendable.append(quotechar);
            }

            if (stringContainsSpecialCharacters) {
                processLine(nextElement, appendable);
            } else {
                appendable.append(nextElement);
            }

            if (stringContainsSpecialCharacters && quotechar != NO_QUOTE_CHARACTER) {
                appendable.append(quotechar);
            }
        }

        appendable.append(lineEnd);
        writer.write(appendable.toString());
    }

    private boolean stringContainsSpecialCharacters(String line) {
        return line.indexOf(quotechar) != -1
                || line.indexOf(escapechar) != -1
                || line.indexOf(separator) != -1
                || line.contains(DEFAULT_LINE_END)
                || line.contains("\r");
    }

    private void processLine(String nextElement, Appendable appendable) throws IOException {
        for (int j = 0; j < nextElement.length(); j++) {
            char nextChar = nextElement.charAt(j);
            processCharacter(appendable, nextChar);
        }

    }

    private void processCharacter(Appendable appendable, char nextChar) throws IOException {
        if (escapechar != NO_ESCAPE_CHARACTER && checkCharactersToEscape(nextChar)) {
            appendable.append(escapechar);
        }
        appendable.append(nextChar);
    }

    private boolean checkCharactersToEscape(char nextChar) {
        if (quotechar == NO_QUOTE_CHARACTER) {
            return nextChar == quotechar || nextChar == escapechar || nextChar == separator || nextChar == '\n';
        }
        return nextChar == quotechar || nextChar == escapechar;
    }

    public void close() throws IOException {
        if (writer != null) {
            flush();
            writer.close();
        }
    }

    public void flush() throws IOException {
        if (writer != null) {
            writer.flush();
        }
    }
}
