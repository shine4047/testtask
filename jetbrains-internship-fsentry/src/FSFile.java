import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FSFile extends FSEntry {
    private String content;
    public FSFile(String name, String content) {
        super(name);
        this.content = content;
    }

    public FSFile(String name) {
        this(name, "");
    }

    @Override
    public void mountAt(String path) throws IOException {
        final Path p = Paths.get(path, getName());
        Files.createFile(p);
        try {
            Files.writeString(p, content);
        } catch (Exception e) {
            Files.delete(p);
            throw e;
        }
    }
}
