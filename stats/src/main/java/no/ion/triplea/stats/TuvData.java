package no.ion.triplea.stats;

import java.util.ArrayList;
import java.util.List;

/**
 * End of turn total unit value datapoints.
 */
public class TuvData {
    private final List<TuvRecord> records;

    static TuvData extractFrom(CsvFile file) { return extractFrom(file.getFullStats()); }

    static TuvData extractFrom(FullStats fullStats) {
        List<PhaseStats> phaseStatsList = fullStats.phaseStats();
        List<TuvRecord> records = new ArrayList<>();

        if (phaseStatsList.size() > 0) {
            {
                var firstStats = phaseStatsList.get(0);
                if (firstStats.phase() != Phase.PURCHASE) {
                    throw new IllegalStateException("Unexpected first phase: " + firstStats.phase());
                }

                records.add(makeRecord(firstStats, firstStats.player().startOfTurnInRound()));
            }

            for (var stats : phaseStatsList) {
                if (stats.phase() != Phase.END_OF_TURN) continue;

                records.add(makeRecord(stats, stats.player().endOfTurnInRound()));
            }
        }

        return new TuvData(records);
    }

    private static TuvRecord makeRecord(PhaseStats stats, float roundOffset) {
        return new TuvRecord(
                stats.getRound() + roundOffset,
                stats.getTuvRussians(),
                stats.getTuvGermans(),
                stats.getTuvBritish(),
                stats.getTuvJapanese(),
                stats.getTuvAmericans());
    }

    public TuvData(List<TuvRecord> records) {
        this.records = List.copyOf(records);
    }

    public List<TuvRecord> records() { return records; }
}
