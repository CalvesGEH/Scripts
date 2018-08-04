package Scripts.BowWeaver;

import org.powerbot.script.PaintListener;
import org.powerbot.script.PollingScript;
import org.powerbot.script.Script;
import org.powerbot.script.rt4.ClientContext;
import org.powerbot.script.rt4.GeItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

@Script.Manifest(name = "BowStringWeaver", description = "Weaves BowStrings at Lumbridge Castle.")
public class BowWeaver extends PollingScript<ClientContext> implements PaintListener {
    private List<Task> taskList = new ArrayList<Task>();
    public static final int stringID = 1777;
    private int stringPrice;
    private Bank bank;
    private Weave weave;
    private WalkToSpinner spinner;
    private Task currentTask;

    @Override
    public void start() {
        bank = new Bank(ctx);
        spinner = new WalkToSpinner(ctx);
        weave = new Weave(ctx);
        taskList.addAll(Arrays.asList(spinner, weave, bank));
        stringPrice = new GeItem(stringID).price;
    }

    @Override
    public void poll() {
        for (Task task : taskList) {
            if (task.activate()) {
                currentTask = task;
                task.execute();
                return;
            }
        }
        return;
    }



    @Override
    public void stop() {

    }

    public static final Font TAHOMA = new Font("Tahoma", Font.PLAIN, 12);

    @Override
    public void repaint(Graphics graphics) {
        final Graphics2D g = (Graphics2D) graphics;
        g.setFont(TAHOMA);

        final String message = currentTask.getMessage();
        final int stringsMade = bank.getStringsMade();
        final int profit = stringsMade * stringPrice;
        final int profitHr = (int) ((profit * 3600000D) / getRuntime());
        final int stringsHr = (int) ((stringsMade * 3600000D) / getRuntime());

        g.setColor(Color.BLACK);
        g.fillRect(5, 5, 175, 85);

        g.setColor(Color.WHITE);

        g.drawString("Status: " + message, 10, 20 );
        g.drawString(String.format("Runtime: %02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(getRuntime()), TimeUnit.MILLISECONDS.toMinutes(getRuntime()) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(getRuntime())), TimeUnit.MILLISECONDS.toSeconds(getRuntime()) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getRuntime()))), 10, 40);
        g.drawString(String.format("Strings Made: %,d (%,d)", stringsMade, stringsHr), 10, 60);
        g.drawString(String.format("Profit: %,d (%,d)", profit, profitHr), 10, 80);
    }
}
