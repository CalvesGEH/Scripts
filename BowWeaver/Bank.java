package Scripts.BowWeaver;


import org.powerbot.script.Condition;
import org.powerbot.script.Tile;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GameObject;

import java.util.concurrent.Callable;

public class Bank extends Task<ClientContext> {

    private static final int strings = 1777;
    private static final int flax = 1779;
    private static final int door = 1543;
    private static final int staircase = 16672;
    private static final int spinner = 14889;
    private int stringsMade = 0;
    private static final Tile bankTile = new Tile(3209, 3219, 2);
    private String message;

    public Bank(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return ctx.inventory.select().id(flax).count() == 0;
    }

    @Override
    public void execute() {
        final GameObject stairs = ctx.objects.select().id(staircase).nearest().poll();
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
        message = "Going up stairs...";
        if (stairs.inViewport()) {
            stairs.interact("Climb-up");
        }
        else {
            ctx.movement.step(stairs);
            ctx.camera.turnTo(stairs);
            stairs.interact("Climb-up");
        }
        Condition.wait(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return ctx.players.local().tile().floor() == 2;
            }
        }, 300, 15);
        message = "Banking...";
        if (ctx.players.local().tile().floor() == 2) {
            Tile bankTileTemp = bankTile.derive(org.powerbot.script.Random.nextInt(-1, 1), org.powerbot.script.Random.nextInt(-1, 1));
            if (ctx.bank.inViewport()) {
                ctx.movement.step(bankTileTemp);
                ctx.input.hop(ctx.bank.nearest().tile().x(), ctx.bank.nearest().tile().y());
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return bankTile.distanceTo(ctx.players.local()) < 3;
                    }
                }, 150, 25);
                ctx.bank.open();
            } else {
                ctx.camera.turnTo(ctx.bank.nearest());
                ctx.movement.step(bankTileTemp);
                ctx.input.hop(ctx.bank.nearest().tile().x(), ctx.bank.nearest().tile().y());
                Condition.wait(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        return bankTile.distanceTo(ctx.players.local()) < 3;
                    }
                }, 150, 25);
                ctx.bank.open();
            }
            Condition.wait(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return ctx.bank.opened();
                }
            }, 100, 15);
            stringsMade += ctx.inventory.select().id(strings).count();
            ctx.bank.depositInventory();
            ctx.bank.withdraw(flax, org.powerbot.script.rt4.Bank.Amount.ALL);
            ctx.bank.close();
        }
    }

    public int getStringsMade() {
        return stringsMade;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
