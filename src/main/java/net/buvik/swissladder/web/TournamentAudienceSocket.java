package net.buvik.swissladder.web;

import net.buvik.swissladder.service.InMemoryTournamentStore;
import net.buvik.swissladder.service.TournamentEventBusFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.lang.String.format;

@ApplicationScoped
@ServerEndpoint("/audience/{username}/tournament/{tournamentId}")
public class TournamentAudienceSocket {

    @Inject
    InMemoryTournamentStore tournamentStore;

    @Inject
    TournamentEventBusFactory tournamentEventBusFactory;

    Map<String, Session> sessions = new ConcurrentHashMap<>();

    @OnOpen
    public void onOpen(Session session,
                       @PathParam("username") String username,
                       @PathParam("tournamentId") int tournamentId) {
        sessions.put(username, session);
        tournamentEventBusFactory.tournamentEventBus().addListener((message, theTournamentId) ->
            onMessage(message, tournamentStore.tournamentName(theTournamentId)));
        broadcast(format("[User %s joined, watching tournament %s]",
            username, tournamentStore.tournamentName(tournamentId)));
        var otherUsers = sessions.keySet().stream()
            .filter(name -> !(name.equals(username)))
            .collect(Collectors.joining(", "));
        if (!otherUsers.isEmpty()) {
            session.getAsyncRemote().sendText(format("* Currently these users are also watching: %s", otherUsers));
        }
    }

    @OnClose
    public void onClose(Session session,
                        @PathParam("username") String username,
                        @PathParam("tournamentId") int tournamentId) {
        tournamentEventBusFactory.tournamentEventBus().removeListener((message, theTournamentId) ->
            onMessage(message, tournamentStore.tournamentName(theTournamentId)));
        sessions.remove(username);
        broadcast(format("[User %s left the tournament %s]", username, tournamentStore.tournamentName(tournamentId)));
    }

    @OnError
    public void onError(Session session,
                        @PathParam("username") String username,
                        @PathParam("tournamentId") int tournamentId,
                        Throwable throwable) {
        tournamentEventBusFactory.tournamentEventBus().removeListener((message, theTournamentId) ->
            onMessage(message, tournamentStore.tournamentName(theTournamentId)));
        sessions.remove(username);
        // TODO: Log
    }

    @OnMessage
    public void onMessage(String message,
                          @PathParam("username") String username) {
        broadcast(format(">> %s: %s", username, message));
    }

    private void broadcast(String message) {
        sessions.values().forEach(session -> {
            session.getAsyncRemote().sendObject(message, result -> {
                if (result.getException() != null) {
                    System.out.println("Unable to send message: " + result.getException());
                }
            });
        });
    }
}
