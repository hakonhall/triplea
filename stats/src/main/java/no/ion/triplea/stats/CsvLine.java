package no.ion.triplea.stats;

public class CsvLine {
    private final int lineno;
    private final String line;
    private final String[] elements;

    public static CsvLine parse(String line, int lineno) {
        if (line.isBlank()) {
            return new CsvLine(lineno, line);
        }

        // Appears CSV is very basic
        int fields = 1;
        for (int offset = line.indexOf(','); offset != -1; offset = line.indexOf(',', offset + 1)) {
            ++fields;
        }

        String[] elements = new String[fields];

        int startIndex = 0;
        for (int elementsIndex = 0;; ++elementsIndex) {
            int endIndex = line.indexOf(',', startIndex);
            if (endIndex == -1) {
                elements[elementsIndex] = line.substring(startIndex);
                if (elementsIndex + 1 != elements.length) {
                    throw new IndexOutOfBoundsException(elementsIndex);
                }

                return new CsvLine(lineno, line, elements);
            } else {
                elements[elementsIndex] = line.substring(startIndex, endIndex);
                startIndex = endIndex + 1;
            }
        }
    }

    private CsvLine(int lineno, String line) { this(lineno, line, new String[0]); }
    private CsvLine(int lineno, String line, String[] elements) {
        this.lineno = lineno;
        this.line = line;
        this.elements = elements;
    }

    int size() { return elements.length; }
    public boolean empty() { return elements.length == 0; }
    String getElement(int index) { return elements[index]; }

    public String asString() { return "line " + lineno + ": " + line; }

    /** Use {@link #asString()}. */
    @Override public String toString() { throw new UnsupportedOperationException(); }
}
