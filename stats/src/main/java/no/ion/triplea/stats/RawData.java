package no.ion.triplea.stats;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static no.ion.triplea.stats.Exceptions.uncheckIO;

public class RawData {
    static void write(TuvData data, Path toPath) {
        String string = toTuvDataString(data);
        uncheckIO(() -> Files.writeString(toPath, string, StandardCharsets.UTF_8));
    }

    static String toTuvDataString(TuvData data) {
        StringBuilder builder = new StringBuilder();

        for (var record : data.records()) {
            builder.append(record.round())
                    .append("\t")
                    .append(record.tuvRussia())
                    .append("\t")
                    .append(record.tuvGermany())
                    .append("\t")
                    .append(record.tuvBritain())
                    .append("\t")
                    .append(record.tuvJapan())
                    .append("\t")
                    .append(record.tuvAmerica())
                    .append('\n');
        }

        return builder.toString();
    }
}
