package net.buvik.swissladder;

import java.util.List;
import java.util.Optional;

public interface TournamentStore {

    Tournament add(Integer id, String name);

    List<Tournament> unfinished();

    void addPlayer(int tournamentId, String name);

    String tournamentName(int tournamentId);

    Optional<Tournament> find(int tournamentId);
}
