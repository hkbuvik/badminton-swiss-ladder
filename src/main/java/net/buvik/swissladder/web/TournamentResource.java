package net.buvik.swissladder.web;

import net.buvik.swissladder.Tournament;
import net.buvik.swissladder.TournamentEventBus;
import net.buvik.swissladder.service.InMemoryTournamentStore;
import net.buvik.swissladder.service.TournamentEventBusFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.Optional;

@Path("/tournament/{id}")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TournamentResource {

    @Inject
    InMemoryTournamentStore tournamentStore;

    @Inject
    TournamentEventBusFactory tournamentEventBusFactory;

    @POST
    public Tournament newTournament(@PathParam("id") int id,
                                    @QueryParam("name") String name) {
        var tournament = tournamentStore.add(id, name);
        tournamentEventBusFactory.tournamentEventBus().notify(id, "Tournament added");
        return tournament;
    }

    @GET
    public Optional<Tournament> tournament(@PathParam("id") int id) {
        return tournamentStore.find(id);
    }
}
