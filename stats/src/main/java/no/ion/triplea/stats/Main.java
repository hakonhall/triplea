package no.ion.triplea.stats;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    private final String[] args;
    private int argi = 0;

    public Main(String[] args) {
        this.args = args;
    }

    public static void main(String[] args) {
        new Main(args).run();
    }

    private void run() {
        Path toPath = null;
        PlotlyWriter.HtmlOptions options = new PlotlyWriter.HtmlOptions();

        for_loop:
        for (argi = 0; argi < args.length; ++argi) {
            switch (args[argi]) {
                case "-a":
                    options.withAxisAndAllies = true;
                    break;
                case "-H":
                    options.heightPx = Integer.parseInt(getOptionArgument("-H"));
                    break;
                case "-o":
                    toPath = Path.of(getOptionArgument(args[argi]));
                    if (!Files.isDirectory(toPath.getParent())) {
                        usageError(toPath.getParent() + " is not a directory");
                    }
                    break;
                case "-s":
                    options.share = true;
                    break;
                case "-W":
                    options.widthPx = Integer.parseInt(getOptionArgument("-W"));
                    break;
                default:
                    if (args[argi].startsWith("-")) {
                        usageError("Unknown option: " + args[argi]);
                    }

                    break for_loop;
            }
        }

        if (toPath == null) {
            usageError("Missing -o");
        }

        if (options.share && options.withAxisAndAllies) {
            usageError("-a and -s are exclusive options");
        }

        if (argi != args.length - 1) {
            usageError("Expected exactly one CSV file argument");
        }

        Path csvPath = Path.of(args[argi]);
        if (!Files.exists(csvPath)) {
            usageError("File does not exist: " + csvPath);
        }

        if (!csvPath.toString().endsWith(".csv")) {
            System.out.println("Warning: File extension not .csv");
        }

        CsvFile file = CsvReader.read(csvPath);
        TuvData data = TuvData.extractFrom(file);
        PlotlyWriter writer = PlotlyWriter.create(options, data);
        writer.writeHtml(toPath);
    }

    private void usageError(String message) {
        System.err.println(message);
        System.exit(1);
    }

    private String getOptionArgument(String option) {
        if (argi + 1 >= args.length) {
            usageError("Missing argument to " + option);
        }

        return args[++argi];
    }
}
