# triplea
Utils for the TripleA game

## stats

The stats module implements a way to visualize the total unit value (TUV) of the various nations throughout the turns.
It reads the Full Game Stats CSV file and makes an HTML file to be viewed in a browser.
Example: https://github.com/hakonhall/triplea/blob/master/stats/src/test/resources/example.html

Compile the `stats` module with a version 14 Java compiler or later, and `mvn install`.  Run sommething like

```
java --enable-preview -cp target/triplea-java-1.0-SNAPSHOT.jar no.ion.triplea.stats.Main \
  -s -H 1000 -W 1500 -o ../g1.html ~/games/TripleA/stats_2020_08_23_World\ War\ II\ v5\ 1942\ SE\ TR_round_18_full.csv
```

Then open `g1.html` in a browser.  Add `-s` to get a graph of relative TUV strength instead.
