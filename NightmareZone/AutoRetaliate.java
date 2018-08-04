package Scripts.NightmareZone;

import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Game;

public class AutoRetaliate extends Task<ClientContext> {
    private String message;

    public AutoRetaliate(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return !ctx.combat.autoRetaliate();
    }

    @Override
    public void execute() {
        message = "Activating AutoRetaliate.";
        ctx.combat.autoRetaliate(true);
        ctx.game.tab(Game.Tab.INVENTORY);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
