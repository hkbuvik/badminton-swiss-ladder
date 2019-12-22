package net.buvik.swissladder;

import java.util.function.BiConsumer;

public interface TournamentEventBus {
    void addListener(BiConsumer<String, Integer> listener);

    void removeListener(BiConsumer<String, Integer> listener);

    void notify(int tournamentId, String message);
}
