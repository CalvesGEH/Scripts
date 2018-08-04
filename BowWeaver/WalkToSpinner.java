package Scripts.BowWeaver;

import org.powerbot.script.Condition;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class WalkToSpinner extends Task<ClientContext> {

    private static final int staircase = 16673;
    private static final int spinner = 14889;
    private static final int door = 1543;
    private static final int flax = 1779;
    private String message;

    public WalkToSpinner(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.players.local().tile().floor() == 2
                && ctx.inventory.select().id(flax).count() > 0
                && !ctx.objects.select().id(spinner).poll().inViewport();
    }

    @Override
    public void execute() {
        message = "Going down stairs...";
        GameObject stairs = ctx.objects.select().id(staircase).nearest().poll();
        if (stairs.inViewport()) {
            ctx.movement.step(stairs);
            stairs.interact("Climb-down");
        }
        else {
            ctx.movement.step(stairs);
            ctx.camera.turnTo(stairs);
            stairs.interact("Climb-down");
        }
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().tile().floor() == 1;
            }
        }, 100, 50);
        message = "Walking to spinner...";
        ctx.movement.step(ctx.objects.select().id(spinner).poll());
        final GameObject closedDoor = ctx.objects.select().id(door).at(new org.powerbot.script.Tile(3207, 3214, 1)).poll();
        closedDoor.bounds(108, 127, -235, 0, 0, 127);
        if (closedDoor.valid()) {
            closedDoor.interact("Open");
        }
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return !closedDoor.valid();
            }
        }, 100, 25);
        ctx.camera.turnTo(ctx.objects.select().id(spinner).poll());
    }

    @Override
    public String getMessage() {
        return message;
    }
}
