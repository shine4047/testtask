import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FSCreatorTest {
    private final String basePath = "/tmp";

    @org.junit.jupiter.api.Test
    void create() throws IOException {
        basicFileAndDirectory();
        directoryExists();
        fileExists();
        invalidFileName();
        duplicateFileNameInsideFolder();
    }

    @org.junit.jupiter.api.Test
    void basicFileAndDirectory() throws IOException {
        final String dirName = "FSTest1";
        FSCreator.create(new FSFolder(dirName), basePath);
        final String dirPath = basePath + File.separator + dirName;
        final String fileName = "a";
        final String fileContent = "content";
        FSCreator.create(new FSFile(fileName, fileContent), dirPath);
        final String filePath = dirPath + File.separator + fileName;

        assert(Files.exists(Paths.get(dirPath)));
        assert(Files.exists(Paths.get(filePath)));
        assert(Files.readString(Paths.get(filePath)).equals(fileContent));

        Files.delete(Paths.get(filePath));
        Files.delete(Paths.get(dirPath));

        // create at once
        FSCreator.create(new FSFolder(dirName, List.of(new FSFile(fileName, fileContent))), basePath);
        assert(Files.exists(Paths.get(dirPath)));
        assert(Files.exists(Paths.get(filePath)));
        assert(Files.readString(Paths.get(filePath)).equals(fileContent));
        Files.delete(Paths.get(filePath));
        Files.delete(Paths.get(dirPath));
    }

    @org.junit.jupiter.api.Test
    void directoryExists() throws IOException {
        final String dirName = "FSTest2";
        FSCreator.create(new FSFolder(dirName), basePath);
        final String dirPath = basePath + File.separator + dirName;
        assert(Files.exists(Paths.get(dirPath)));

        assertThrows(Exception.class, () -> FSCreator.create(new FSFolder(dirName), basePath));

        Files.delete(Paths.get(dirPath));
    }

    @org.junit.jupiter.api.Test
    void fileExists() throws IOException {
        final String fileName = "FSTest3";
        FSCreator.create(new FSFile(fileName), basePath);
        final String filePath = basePath + File.separator + fileName;
        assert(Files.exists(Paths.get(filePath)));

        assertThrows(Exception.class, () -> FSCreator.create(new FSFile(fileName), basePath));

        Files.delete(Paths.get(filePath));
    }

    @org.junit.jupiter.api.Test
    void invalidFileName() {
        final String fileName = "FSTest4" + String.valueOf('\0');
        final String filePath = basePath + File.separator + fileName;
        assertThrows(Exception.class, () -> FSCreator.create(new FSFile(fileName), basePath));
    }

    @org.junit.jupiter.api.Test
    void duplicateFileNameInsideFolder() {
        final String dirName = "FSTest5";
        final String dirPath = basePath + File.separator + dirName;
        final String fileName = "a";
        final String filePath = dirPath + File.separator + fileName;
        assertThrows(Exception.class, () -> FSCreator.create(new FSFolder(dirName, List.of(new FSFile(fileName), new FSFile(fileName))), basePath));
        assert(Files.notExists(Paths.get(dirPath)));
    }
}