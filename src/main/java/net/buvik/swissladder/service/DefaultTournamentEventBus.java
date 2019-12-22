package net.buvik.swissladder.service;

import net.buvik.swissladder.TournamentEventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class DefaultTournamentEventBus implements TournamentEventBus {

    private List<BiConsumer<String, Integer>> listeners = new ArrayList<>();

    @Override
    public void addListener(BiConsumer<String, Integer> listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(BiConsumer<String, Integer> listener) {
        listeners.remove(listener);
    }

    @Override
    public void notify(int tournamentId, String message) {
        listeners.parallelStream()
            .forEach(listener -> listener.accept(message, tournamentId));
    }

}
