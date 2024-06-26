package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.storage.ImportManager;
import seedu.address.storage.JsonAddressBookStorage;
import seedu.address.testutil.TestUtil;

public class MigrateCommandTest {
    @TempDir
    public Path temporaryFolder;

    private Model model = new ModelManager();

    private JsonAddressBookStorage tempAddressBookStorage;

    @BeforeEach
    public void setUp() {
        this.tempAddressBookStorage = TestUtil.setUpForMigration(temporaryFolder);
    }

    @Test
    public void getPathToImportFromReturnCorrectPath() {
        Path pathToImportFrom = Paths.get("path/to/file.csv");
        Path pathToImportTo = Paths.get("path/to/file.json");
        ImportManager importManager = new ImportManager(pathToImportFrom, pathToImportTo);
        MigrateCommand command = new MigrateCommand(pathToImportFrom, importManager);
        assertEquals(pathToImportFrom.toString(), command.getPathToImportFrom());
    }

    @Test
    public void getPathToImportToThrowsNullPointerException() {
        Path pathToImportFrom = Paths.get("path/to/file.csv");
        Path pathToImportTo = Paths.get("path/to/file.json");
        ImportManager importManager = new ImportManager(pathToImportFrom, pathToImportTo);
        MigrateCommand command = new MigrateCommand(pathToImportFrom, importManager);
        assertThrows(NullPointerException.class, () -> command.getPathToImportTo());
    }

    @Test
    public void execute_migrateSuccessReturnsSuccessMessage() {
        TestUtil.createMockCsvFile();
        Path pathToImportFrom = Paths.get("./imports/test.csv");
        ImportManager importManager = new ImportManager(pathToImportFrom,
                tempAddressBookStorage.getAddressBookFilePath());
        MigrateCommand migrateCommand = new MigrateCommand(pathToImportFrom, importManager);

        CommandResult result = migrateCommand.execute(model);
        assertEquals(MigrateCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());
    }

    @Test
    public void execute_migrateFailureReturnsFailureMessage() {
        Path pathToImportFrom = Paths.get("");
        ImportManager importManager = new ImportManager(pathToImportFrom,
                tempAddressBookStorage.getAddressBookFilePath());
        MigrateCommand migrateCommand = new MigrateCommand(pathToImportFrom, importManager);

        CommandResult result = migrateCommand.execute(model);
        assertEquals(MigrateCommand.MESSAGE_FAILURE, result.getFeedbackToUser());
    }

    @Test
    public void equals_differentObjectsreturnsFalse() {
        Path path1 = Paths.get("path/to/file1.csv");
        Path path2 = Paths.get("path/to/file2.csv");
        Path pathJson = Paths.get("path/to/json");
        ImportManager importManager1 = new ImportManager(path1, pathJson);

        MigrateCommand command1 = new MigrateCommand(path1, importManager1);
        MigrateCommand command2 = new MigrateCommand(path2, importManager1);

        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_differentTypesreturnsFalse() {
        Path path1 = Paths.get("path/to/file2.csv");
        Path pathJson = Paths.get("path/to/json");
        ImportManager importManager = new ImportManager(path1, pathJson);
        MigrateCommand command = new MigrateCommand(path1, importManager);
        String string = "String";
        assertFalse(command.equals(string));
    }

    @Test
    public void equals_sameObjectreturnsTrue() {
        Path path1 = Paths.get("path/to/file2.csv");
        Path pathJson = Paths.get("path/to/json");
        ImportManager importManager = new ImportManager(path1, pathJson);
        MigrateCommand command = new MigrateCommand(path1, importManager);
        assertTrue(command.equals(command));
    }

    @Test
    public void equals_samePathReturnsTrue() {
        Path path1 = Paths.get("path/to/file1.csv");
        Path pathJson = Paths.get("path/to/json");
        ImportManager importManager1 = new ImportManager(path1, pathJson);
        ImportManager importManager2 = new ImportManager(path1, pathJson);

        MigrateCommand command1 = new MigrateCommand(path1, importManager1);
        MigrateCommand command2 = new MigrateCommand(path1, importManager2);

        assertTrue(command1.equals(command2));
    }

}
