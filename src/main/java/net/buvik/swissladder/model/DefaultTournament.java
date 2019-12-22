package net.buvik.swissladder.model;

import net.buvik.swissladder.Tournament;

public class DefaultTournament implements Tournament {

    public Integer id;
    public boolean finished;
    public String name;

    public DefaultTournament(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public Integer id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public boolean unfinished() {
        return !finished;
    }

}
