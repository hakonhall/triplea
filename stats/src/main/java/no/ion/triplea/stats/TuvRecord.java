package no.ion.triplea.stats;

public record TuvRecord(float round, // 1.0 for russia, 1.2 for germany, etc
                        int tuvRussia,
                        int tuvGermany,
                        int tuvBritain,
                        int tuvJapan,
                        int tuvAmerica) {
}
