import java.io.IOException;

public abstract class FSEntry {
    private String name;

    public FSEntry(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void mountAt(String path) throws IOException;
}
