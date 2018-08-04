package Scripts.NightmareZone;

import org.powerbot.script.rt4.ClientContext;

public class Rest extends Task<ClientContext> {
    private String message;

    public Rest(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean activate() {
        return true;
    }

    @Override
    public void execute() {
        message = "Waiting for next task.";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
