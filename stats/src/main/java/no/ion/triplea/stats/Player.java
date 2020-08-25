package no.ion.triplea.stats;

public enum Player {
    RUSSIANS(0.0f), GERMANS(0.2f), BRITISH(0.4f), JAPANESE(0.6f), AMERICANS(0.8f);

    private final float startOfTurnInRound;

    Player(float startOfTurnInRound) {
        this.startOfTurnInRound = startOfTurnInRound;
    }

    public float startOfTurnInRound() { return startOfTurnInRound; }
    public float endOfTurnInRound() { return startOfTurnInRound + 0.2f; }
}
