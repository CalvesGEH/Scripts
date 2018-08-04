package Scripts.NightmareZone;

import org.powerbot.script.rt4.ClientContext;

public class Absorption extends Task<ClientContext> {
    private final int absorptionIDs[] = {11737, 11736, 11735, 11734};
    private String message;
    private int absorption;

    public Absorption(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        absorption = ctx.varpbits.varpbit(1067) >> 5 & 0x3FF;
        return (!ctx.inventory.select().id(absorptionIDs).isEmpty() &&
                absorption < 700);
    }

    @Override
    public void execute() {
        message = "Increasing Absorption.";
        ctx.inventory.select().id(absorptionIDs).poll().interact("Drink");
    }

    @Override
    public String getMessage() {
        return message;
    }
}
