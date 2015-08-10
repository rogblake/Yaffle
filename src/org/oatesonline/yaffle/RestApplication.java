
package org.oatesonline.yaffle;

import org.oatesonline.yaffle.services.impl.LeaderboardService;
import org.oatesonline.yaffle.services.impl.LeaderboardUpdateService;
import org.oatesonline.yaffle.services.impl.LeagueService;
import org.oatesonline.yaffle.services.impl.LeagueUpdateService;
import org.oatesonline.yaffle.services.impl.LoginService;
import org.oatesonline.yaffle.services.impl.PlayerService;
import org.oatesonline.yaffle.services.impl.PlayerUpdateService;
import org.oatesonline.yaffle.services.impl.TeamService;
import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.routing.Router;

public class RestApplication extends Application {

    /**
     * Creates a root Restlet that will receive all incoming calls.
     */
	// router.attach("/items/{itemName}", ItemResource.class);
    @Override
    public Restlet createInboundRoot() {
        // Create a router Restlet that routes each call to a
        // new instance of HelloWorldResource.
        Router router = new Router(getContext());

        // Defines only one route
//        router.attachDefault(HelloWorld.class);
        router.attach("/league/{leaguecode}", LeagueService.class);
        router.attach("/leaderboard", LeaderboardService.class);
        router.attach("/admin/update/leaderboard", LeaderboardUpdateService.class);
        router.attach("/admin/update/{leaguecode}", LeagueUpdateService.class);
        router.attach("/admin/createplayer", PlayerService.class);
        router.attach("/admin/update/{playerId}/{leagueCode}/{teamId}", PlayerUpdateService.class);
       router.attach("/player/{playerid}", PlayerService.class);
         router.attach("/playerupdate/create", PlayerUpdateService.class);
        router.attach("/teams/{playerid}", TeamService.class);
        router.attach("/login", LoginService.class);
        return router;
    }
    
}
