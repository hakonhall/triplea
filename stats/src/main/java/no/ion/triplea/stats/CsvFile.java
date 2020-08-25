package no.ion.triplea.stats;

public class CsvFile {
    private final FullStats fullStats;

    public CsvFile(FullStats fullStats) {
        this.fullStats = fullStats;
    }

    public FullStats getFullStats() { return fullStats; }
}
