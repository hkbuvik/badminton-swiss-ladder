package net.buvik.swissladder.service;

import net.buvik.swissladder.Player;
import net.buvik.swissladder.Tournament;
import net.buvik.swissladder.TournamentStore;
import net.buvik.swissladder.model.DefaultTournament;

import javax.enterprise.context.ApplicationScoped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

@ApplicationScoped
public class InMemoryTournamentStore implements TournamentStore {

    private Map<Integer, Tournament> tournaments = new HashMap<>();
    private List<Player> players = new ArrayList<>();
    private List<BiConsumer<String, Integer>> listeners = new ArrayList<>();

    @Override
    public Tournament add(Integer id, String name) {
        var tournament = new DefaultTournament(id, name);
        tournaments.put(id, tournament);
        return tournament;
    }

    @Override
    public List<Tournament> unfinished() {
        return tournaments.values().stream()
            .filter(Tournament::unfinished)
            .collect(Collectors.toList());
    }

    @Override
    public void addPlayer(int tournamentId, String name) {
        var player = new Player(name);
        players.add(player);
    }


    @Override
    public String tournamentName(int tournamentId) {
        return Optional.ofNullable(tournaments.get(tournamentId))
            .map(Tournament::name)
            .orElse("[not found]");
    }

    @Override
    public Optional<Tournament> find(int tournamentId) {
        return Optional.ofNullable(tournaments.get(tournamentId));
    }
}
