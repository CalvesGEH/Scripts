package Scripts.BowWeaver;

import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.Component;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class Weave extends Task<ClientContext> {

    private static final int strings = 1777;
    private static final int door = 1543;
    private static final int flax = 1779;
    private static final int spinner = 14889;
    private String message;
    private static final Tile spinnerTile = new Tile(3209, 3213, 1);

    public Weave(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().id(strings).count() < 28
                && ctx.objects.select().id(spinner).poll().inViewport()
                && ctx.players.local().tile().floor() == 1
                && ctx.inventory.select().id(flax).count() > 0;
    }

    @Override
    public void execute() {
        final Component spin = ctx.widgets.widget(270).component(16).component(29);
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
        message = "Spinning...";
        if (ctx.objects.select().id(spinner).poll().inViewport()) {
            ctx.objects.select().id(spinner).poll().interact("Spin");
        }
        else {
            ctx.camera.turnTo(ctx.objects.select().id(spinner).poll());
            ctx.objects.select().id(spinner).poll().interact("Spin");
        }
        Condition.sleep(1000);
        if (ctx.players.local().tile().equals(spinnerTile)) {
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return spin.visible();
                }
            }, 250, 10);
            spin.click();
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.inventory.select().id(flax).count() == 0;
                }
            }, 300, 180);
        }
    }

    @Override
    public String getMessage() {
        return message;
    }
}
