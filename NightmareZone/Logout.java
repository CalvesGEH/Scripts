package Scripts.NightmareZone;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;

public class Logout extends Task<ClientContext> {
    private static final Tile logoutTile = new Tile(2608, 3115, 0);
    private String message;

    public Logout(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return logoutTile.distanceTo(ctx.players.local()) < 3;
    }

    @Override
    public void execute() {
        message = "Logging Out.";
        ctx.game.logout();
        System.exit(0);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
