import java.io.IOException;

public class FSCreator {
    public static void create(FSEntry entryToCreate, String destination) throws IOException {
        entryToCreate.mountAt(destination);
    }
}
