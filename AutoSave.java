public class AutoSave implements Runnable {

    private ActionController controller;

    public AutoSave(ActionController controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1_000 * 15);
                Command command = new FileSaveCommand(controller);
                command.execute();
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
