package net.buvik.swissladder;

import java.util.UUID;

public class Player {

    private final String name;
    private UUID id;

    public Player(String name) {
        this.name = name;
        id = UUID.randomUUID();
    }

    public UUID id() {
        return id;
    }
}
