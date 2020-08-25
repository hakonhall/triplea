package no.ion.triplea.stats;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static no.ion.triplea.stats.Exceptions.uncheckIO;
import static org.junit.jupiter.api.Assertions.*;

class CsvReaderTest {

    private static final Path EXAMPLE_CSV = Path.of("src/test/resources/stats_2020_08_23_World War II v5 1942 SE TR_round_26_full.csv");

    @Test
    void verifyReader() {
        CsvFile csvFile = CsvReader.read(EXAMPLE_CSV);
        TuvData data = TuvData.extractFrom(csvFile);
        String tuvString = RawData.toTuvDataString(data);
        String expectedRawData = uncheckIO(() -> Files.readString(Path.of("src/test/resources/raw.data")));
        assertEquals(expectedRawData, tuvString);

        var options = new PlotlyWriter.HtmlOptions();
        options.withAxisAndAllies = true;
        var writer = PlotlyWriter.create(options, data);
        String html = writer.exampleHtml();
        String expectedHtml = uncheckIO(() -> Files.readString(Path.of("src/test/resources/example.html")));
        assertEquals(expectedHtml, html);
    }
}