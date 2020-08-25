package no.ion.triplea.stats;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static no.ion.triplea.stats.Exceptions.uncheckIO;

public class CsvReader {
    private final List<String> lines;

    private int index = 0;

    public static CsvFile read(Path path) {
        List<String> lines = uncheckIO(() -> Files.readAllLines(path));
        return new CsvReader(lines).parse();
    }

    public CsvReader(List<String> lines) {
        this.lines = lines;
    }

    private int lineno() { return index + 1; }

    private CsvFile parse() {
        CsvLine filenameLine = next();
        CsvLine versionLine = next();
        CsvLine gameName = next();
        CsvLine gameVersion = next();

        nextEmpty();

        CsvLine round = next();
        CsvLine players = next();
        CsvLine alliances = next();

        nextEmpty();

        CsvLine turnOrder = next();
        CsvLine russians = next();
        CsvLine germans = next();
        CsvLine british = next();
        CsvLine japanese = next();
        CsvLine americans = next();

        nextEmpty();

        CsvLine winners = next();

        nextEmpty();

        do {
            CsvLine fullStats = next();
            if (fullStats.size() != 2 || !fullStats.getElement(0).startsWith("Full Stats")) continue;
            break;
        } while (true);
        
        CsvLine turnStats = next();
        CsvLine statsHeader = next();

        List<PhaseStats> phaseStats = new ArrayList<>();
        while (!eof()) {
            CsvLine statsLine = next();
            if (statsLine.empty()) break;
            PhaseStats stats = PhaseStats.create(statsLine);
            phaseStats.add(stats);
        }

        FullStats fullStats = new FullStats(phaseStats);

        return new CsvFile(fullStats);
    }

    private boolean eof() { return index == lines.size(); }

    private CsvLine next() {
        if (index >= lines.size()) {
            throw new CsvException("Unexpected end of file at line " + lineno());
        }

        String line = lines.get(index++);
        return CsvLine.parse(line, index);
    }

    private void nextEmpty() {
        CsvLine line = next();
        if (!line.empty()) {
            throw new CsvException("Expected line " + lineno() + " to be empty");
        }
    }
}
