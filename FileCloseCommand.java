class FileCloseCommand implements Command {

    private ActionController controller;

    public FileCloseCommand(ActionController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        FileHandler fh = controller.getFileHandler();
        Viewer viewer = controller.getViewer();

        while (!fh.isSaved()) {
            int optionResult = viewer.showExitWithoutSavingDialog();
            
            switch (optionResult) {
            case 0:
                Command command = new FileSaveAsCommand(controller);
                command.execute();

                if (fh.getPath() == null) {
                    break;
                }
                // falls through
            case 1:
                viewer.getFrame().dispose();
                System.exit(0);
                // falls through
            case 2:
            default:
                return;
            }
        }

        viewer.getFrame().dispose();
        System.exit(0);
    }
    
}