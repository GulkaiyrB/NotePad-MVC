class FileNewCommand implements Command {

    private ActionController controller;

    public FileNewCommand(ActionController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        Viewer viewer = controller.getViewer();
        FileHandler fileHandler = controller.getFileHandler();
        
        viewer.clearTextArea();
        fileHandler.newFile(null);
        
        String fileName = fileHandler.getFileName();
        viewer.updateTitle(fileName);

        if (controller.hasAutoSave()) {
            controller.removeAutoSave();
        }
    }

}