package Scripts.NightmareZone;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Constants;
import org.powerbot.script.rt4.Skills;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Overload extends Task<ClientContext> {
    private final int[] overloadIDs = {11733, 11732, 11731, 11730};
    private long timeSinceOverload = -1;
    private long runtime;
    private String message;

    public Overload(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return (ctx.combat.health() > 50 &&
                !ctx.inventory.select().id(overloadIDs).isEmpty() &&
                (ctx.skills.level(Constants.SKILLS_ATTACK) == ctx.skills.realLevel(Constants.SKILLS_ATTACK)));
    }

    @Override
    public void execute() {
        message = "Drinking Overload Potion.";
        final int startingHealth = ctx.combat.health();
        ctx.inventory.select().id(overloadIDs).poll().interact("Drink");
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return (startingHealth - ctx.combat.health()) > 40;
            }
        }, 150, 200);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
