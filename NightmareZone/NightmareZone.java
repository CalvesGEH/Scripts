package Scripts.NightmareZone;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Script.Manifest(name="NightmareZone", description = "Automatically does the Nightmare Zone.")
public class NightmareZone extends PollingScript<ClientContext> implements PaintListener {
    private List<Task> taskList = new ArrayList<Task>();
    private Rest rest;
    private Overload overload;
    private AutoRetaliate autoRetaliate;
    private Absorption absorption;
    private ReduceHealth reduceHealth;
    private Logout logout;
    private Task currentTask;
    private int startingXP;
    private int startingLevel;

    @Override
    public void start() {
        startingXP = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        startingLevel = ctx.skills.realLevel(Constants.SKILLS_STRENGTH);

        rest = new Rest(ctx);
        overload = new Overload(ctx);
        autoRetaliate = new AutoRetaliate(ctx);
        absorption = new Absorption(ctx);
        reduceHealth = new ReduceHealth(ctx);
        logout = new Logout(ctx);
        taskList.addAll(Arrays.asList(rest, autoRetaliate, overload, absorption, reduceHealth, logout));
    }

    @Override
    public void poll() {
        for (Task task : taskList) {
            if (task.activate()) {
                currentTask = task;
                task.execute();
                if (task != rest) {
                    return;
                }
            }
        }

        return;
    }

    @Override
    public void stop() {
        System.out.println(TimeUnit.MILLISECONDS.toHours(getRuntime()) + ":" + (TimeUnit.MILLISECONDS.toMinutes(getRuntime()) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(getRuntime()))) + ":" + (TimeUnit.MILLISECONDS.toSeconds(getRuntime()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getRuntime()))));
        ctx.controller.stop();
    }

    public static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);

    @Override
    public void repaint(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics;
        g.setFont(TAHOMA);

        String message = "Waiting for Script to Start.";
        if (currentTask != null) {
            message = currentTask.getMessage();
        }

        final int currentXP = ctx.skills.experience(Constants.SKILLS_STRENGTH);
        final int gainedXP = currentXP - startingXP;
        final int perHourXP = (int) ((gainedXP * 3600000D) / getRuntime());

        g.setColor(Color.BLACK);
        g.fillRect(5, 5, 175, 85);

        g.setColor(Color.WHITE);

        g.drawString("Status: " + message, 10, 20);
        g.drawString(String.format("Runtime: %02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(getRuntime()), (TimeUnit.MILLISECONDS.toMinutes(getRuntime()) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(getRuntime()))) , (TimeUnit.MILLISECONDS.toSeconds(getRuntime()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getRuntime())))), 10, 40);
        g.drawString(String.format("XP Gained: %,d (%,d)", gainedXP, perHourXP), 10, 60);
        g.drawString(String.format("Levels Gained: %,d --> %,d", startingLevel, ctx.skills.realLevel(Constants.SKILLS_STRENGTH)), 10, 80);
    }
}
