package no.ion.triplea.stats;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import static no.ion.triplea.stats.Exceptions.uncheckIO;

/**
 * Writes Plotly JavaScript code.
 */
public class PlotlyWriter {
    private final HtmlOptions options;
    private final TuvData data;

    public static class HtmlOptions {
        int widthPx = 1200;
        int heightPx = 500;
        int marginT = 30;
        boolean withAxisAndAllies = false;
        boolean share = false;

        public HtmlOptions() {}
    }

    public static PlotlyWriter create(HtmlOptions options, TuvData data) {
        if (data.records().isEmpty()) {
            throw new IllegalArgumentException("There must be at least one datapoint");
        }

        return new PlotlyWriter(options, data);
    }

    private PlotlyWriter(HtmlOptions options, TuvData data) {
        this.options = options;
        this.data = data;
    }

    private void appendRecordsTo(StringBuilder builder, Function<TuvRecord, String> mapper) {
        boolean firstIteration = true;
        for (var record : data.records()) {
            if (firstIteration) {
                firstIteration = false;
            } else {
                builder.append(", ");
            }

            builder.append(mapper.apply(record));
        }
    }

    public void writeHtml(Path path) {
        String html = exampleHtml();
        uncheckIO(() -> Files.writeString(path, html, StandardCharsets.UTF_8));
    }

    public String exampleHtml() {
        var buffer = new StringBuilder();
        buffer.append("<html>\n" +
                "  <head>\n" +
                "    <script src=\"https://cdn.plot.ly/plotly-latest.min.js\"></script>\n" +
                "  </head>\n" +
                "  <body>\n" +
                "    <div id=\"graph\" style=\"width:")
        .append(options.widthPx)
        .append("px;height:")
        .append(options.heightPx)
        .append("px;\"></div>\n" +
                "    <script>\n");
        appendPlotlyDataLinesTo(buffer, "      ", "graph");
        buffer.append("    </script>\n" +
                "  </body>\n" +
                "</html>\n");

        return buffer.toString();
    }

    public void appendPlotlyDataLinesTo(StringBuilder buffer, String indentation, String elementId) {
        buffer.append(indentation).append("Plotly.newPlot(document.getElementById('").append(elementId).append("'), [\n");

        if (options.share) {
            appendTuvGraphTo(buffer, indentation, TuvRecord::tuvGermany, "Germany");
            buffer.append(",\n");
            appendTuvGraphTo(buffer, indentation, TuvRecord::tuvJapan, "Japan");
            buffer.append(",\n");
            appendTuvGraphTo(buffer, indentation, TuvRecord::tuvRussia, "Russia");
            buffer.append(",\n");
            appendTuvGraphTo(buffer, indentation, TuvRecord::tuvBritain, "UK");
            buffer.append(",\n");
            appendTuvGraphTo(buffer, indentation, TuvRecord::tuvAmerica, "USA");
        } else {
            appendTuvGraphTo(buffer, indentation, TuvRecord::tuvRussia, "Russia");
            buffer.append(",\n");
            appendTuvGraphTo(buffer, indentation, TuvRecord::tuvGermany, "Germany");
            buffer.append(",\n");
            appendTuvGraphTo(buffer, indentation, TuvRecord::tuvBritain, "UK");
            buffer.append(",\n");
            appendTuvGraphTo(buffer, indentation, TuvRecord::tuvJapan, "Japan");
            buffer.append(",\n");
            appendTuvGraphTo(buffer, indentation, TuvRecord::tuvAmerica, "USA");
        }

        if (options.withAxisAndAllies) {
            buffer.append(",\n");
            appendTuvGraphTo(buffer, indentation, record -> record.tuvGermany() + record.tuvJapan(), "Axis");
            buffer.append(",\n");
            appendTuvGraphTo(buffer, indentation, record -> record.tuvRussia() + record.tuvBritain() + record.tuvAmerica(), "Allies");
        }

        buffer.append('\n');

        buffer.append(indentation).append("], {\n");
        buffer.append(indentation).append("  title: 'Total Unit Value',\n");
        buffer.append(indentation).append("  xaxis: { title: 'round' },\n");
        buffer.append(indentation).append("  yaxis: { title: 'tuv' },\n");
        buffer.append(indentation).append("  margin: { t: ").append(options.marginT).append(" }\n");
        buffer.append(indentation).append("});\n");
    }

    /** Does not append a newline after the closing } */
    private void appendTuvGraphTo(StringBuilder buffer, String indentation, Function<TuvRecord, Integer> tuv,
                                  String graphName) {
        buffer.append(indentation).append("  {\n");

        buffer.append(indentation).append("    x: [");
        appendRecordsTo(buffer, record -> String.format("%.1f", record.round()));
        buffer.append("],\n");

        buffer.append(indentation).append("    y: [");
        appendRecordsTo(buffer, record -> Integer.toString(tuv.apply(record)));
        buffer.append("],\n");

        if (options.share) {
            buffer.append(indentation).append("    stackgroup: 'one', groupnorm: 'percent',");
        }

        buffer.append(indentation).append("    name: '").append(graphName).append("'\n");

        buffer.append(indentation).append("  }");
    }
}
