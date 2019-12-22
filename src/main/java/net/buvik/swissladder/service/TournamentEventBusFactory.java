package net.buvik.swissladder.service;

import net.buvik.swissladder.TournamentEventBus;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TournamentEventBusFactory {

    private TournamentEventBus tournamentEventBus;

    public TournamentEventBus tournamentEventBus() {
        if(tournamentEventBus == null) {
            tournamentEventBus = new DefaultTournamentEventBus();
        }
        return tournamentEventBus;
    }
}
