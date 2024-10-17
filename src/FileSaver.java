import java.io.File;
import java.io.IOException;

public class FileSaver {
    public boolean fileCreator(String fileName) {
        String path;
        String separator = File.separator;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().contains("windows")) {
            path = "C:" + separator + "ProgramData" + separator + "DBTest" + separator + fileName + ".json";
        } else if (os.contains("linux") || os.contains("mac") || os.contains("unix") || os.contains("solaris")) {
            path = System.getProperty("user.home") + separator + "DBTest" + separator + fileName + ".json";
        } else {
            return false;
        }
        File file = new File(path);
        try {
            if (file.exists()) {
                return true;
            }
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                if (!parentFile.mkdirs()) {
                    return false;
                }
            }
            return file.createNewFile();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}
