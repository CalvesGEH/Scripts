package Scripts.NightmareZone;

import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;

import java.io.Console;

public class ReduceHealth extends Task<ClientContext> {
    private final static int rockCake = 7510;
    private final int[] overloadIDs = {11733, 11732, 11731, 11730};
    private final int absorptionIDs[] = {11737, 11736, 11735, 11734};
    private String message;
    private static final Tile logoutTile = new Tile(2608, 3115, 0);
    private int absorption;

    public ReduceHealth(ClientContext ctx) {
        super(ctx);
        absorption = ctx.varpbits.varpbit(1067);
    }

    @Override
    public boolean activate() {
        int absorption = ctx.varpbits.varpbit(1067) >> 5 & 0x3FF;
        return (ctx.inventory.select().id(rockCake).poll().valid() &&
                ctx.combat.health() > 1 &&
                (ctx.inventory.select().id(overloadIDs).isEmpty() || (ctx.skills.level(Constants.SKILLS_ATTACK) > ctx.skills.realLevel(Constants.SKILLS_ATTACK))) &&
                !(logoutTile.distanceTo(ctx.players.local()) < 3));
    }

    @Override
    public void execute() {
        message = "Reducing Health to 1.";
        ctx.inventory.select().id(rockCake).poll().interact("Guzzle");
    }

    @Override
    public String getMessage() {
        return message;
    }
}
