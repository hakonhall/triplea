package no.ion.triplea.stats;

import java.util.Collections;
import java.util.List;

public class FullStats {
    private final List<PhaseStats> stats;

    FullStats(List<PhaseStats> stats) {
        this.stats = List.copyOf(stats);
    }

    List<PhaseStats> phaseStats() { return stats; }
}
