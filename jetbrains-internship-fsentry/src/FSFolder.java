import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class FSFolder extends FSEntry {
    private List<FSEntry> content;
    public FSFolder(String name, List<FSEntry> content) {
        super(name);
        this.content = new ArrayList<FSEntry>(content);
    }

    public FSFolder(String name) {
        this(name, new ArrayList<FSEntry>());
    }

    @Override
    public void mountAt(String path) throws IOException {
        final Path p = Paths.get(path, getName());
        Files.createDirectory(p);

        try {
            for (FSEntry entry : content) {
                entry.mountAt(String.valueOf(Paths.get(path, getName())));
            }
        } catch (Exception e) {
            try (Stream<Path> files = Files.walk(p)) {
                files.sorted(Comparator.reverseOrder()) // Delete files first and move up.
                        .map(Path::toFile)
                        .forEach(File::delete);
            }
            throw e;
        }
    }
}
