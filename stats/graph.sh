#!/bin/bash

~/share/jdk-14/bin/java --enable-preview -cp ~/tmp/triplea-java/target/triplea-java-1.0-SNAPSHOT.jar no.ion.triplea.stats.Main "$@"

# Example invocation:
# ./graph.sh -s -H 1000 -W 1500 -o ../g1.html ~/games/TripleA/stats_2020_08_23_World\ War\ II\ v5\ 1942\ SE\ TR_round_18_full.csv
