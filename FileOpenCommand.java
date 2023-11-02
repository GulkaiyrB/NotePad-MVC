import java.nio.file.Path;

class FileOpenCommand implements Command {

    private ActionController controller;

    public FileOpenCommand(ActionController controller) {
        this.controller = controller;
    }

    @Override
    public void execute() {
        FileHandler fileHandler = controller.getFileHandler();
        Viewer viewer = controller.getViewer();

        Path path = viewer.showOpenDialog();

        if (path == null) {
            return;
        }

        fileHandler.newFile(path);
        Result<String, Integer> result = fileHandler.readContentFromFile();

        if (result.isFail()) {
            if (result.getFail() == FileHandler.Status.FILE_IS_TOO_LARGE) {
                viewer.showFileIsTooLargeDialog();
            }
            return;
        }

        String content = result.getSuccess();
        viewer.updateTextArea(content);

        String fileName = fileHandler.getFileName();
        viewer.updateTitle(fileName);

        AutoSave autoSave = new AutoSave(controller);
        controller.setAutoSave(autoSave);
    }
}