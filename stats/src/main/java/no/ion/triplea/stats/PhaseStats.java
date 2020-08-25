package no.ion.triplea.stats;

public class PhaseStats {
    private static final int NELEMENTS = 130;

    private final CsvLine line;

    static PhaseStats create(CsvLine line) {
        if (line.size() != NELEMENTS) {
            throw new CsvException("Expected full stats line to have " + NELEMENTS + " elements, but had " +
                    line.size() + ": " + line.asString());
        }

        return new PhaseStats(line);
    }

    public PhaseStats(CsvLine line) {
        this.line = line;
    }

    public int getRound() { return intElement(0); }

    public Player player() {
        String playerTurn = line.getElement(1);
        switch (playerTurn) {
            case "Russians: ": return Player.RUSSIANS;
            case "Germans: ": return Player.GERMANS;
            case "British: ": return Player.BRITISH;
            case "Japanese: ": return Player.JAPANESE;
            case "Americans: ": return Player.AMERICANS;
            default: throw new IllegalArgumentException(playerTurn);
        }
    }

    public Phase phase() {
        String phaseString = line.getElement(2);
        switch (phaseString) {
            case "Purchase": return Phase.PURCHASE;
            case "Move": return Phase.MOVE;
            case "Battle": return Phase.BATTLE;
            case "NonCombatMove": return Phase.NON_COMBAT_MOVE;
            case "Place": return Phase.PLACE;
            case "EndTurn": return Phase.END_OF_TURN;
        }

        throw new IllegalArgumentException("Unknown Phase: " + phaseString);
    }

    boolean isEndTurn() { return phase() == Phase.END_OF_TURN; }

    public int getTuvRussians() { return intElement(24); }
    public int getTuvGermans() { return intElement(25); }
    public int getTuvBritish() { return intElement(26); }
    public int getTuvJapanese() { return intElement(27); }
    public int getTuvAmericans() { return intElement(28); }

    private int intElement(int index) { return Integer.parseInt(line.getElement(index)); }
}
