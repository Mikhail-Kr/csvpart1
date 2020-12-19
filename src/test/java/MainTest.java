import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MainTest {

  final String fileName = "users.csv";
  final Path filePath = Paths.get(fileName);

  @BeforeEach
  void setUp() throws IOException {
    Files.deleteIfExists(filePath);
    Files.createFile(filePath);
  }

  @AfterEach
  void tearDown() throws IOException {
    Files.deleteIfExists(filePath);
  }

  @Test
  void create_fileIsNotEmpty_shouldSucceed() throws IOException {
    Files.write(filePath,
        List.of(
          "1,Murphy,Aileen,Deborah,38",
          "4,Norton,Robert,\"\",12"
        ),
        StandardCharsets.UTF_8
    );

    List.of(
      new User(15, "Dawson", "Augusta"),
      new User(25, "Ford", "Joseph", "Nicholas"),
      new User(18, "Lambert", "Edward")
    ).forEach(user -> Main.create(fileName, user));

    assertEquals(
        String.join("\n", List.of(
          "1,Murphy,Aileen,Deborah,38",
          "2,Dawson,Augusta,\"\",15",
          "3,Ford,Joseph,Nicholas,25",
          "4,Norton,Robert,\"\",12",
          "5,Lambert,Edward,\"\",18"
        )),
        String.join("\n", Files.readAllLines(filePath))
    );
  }

  @Test
  void create_fileIsEmpty_shouldSucceed() throws IOException {
    List.of(
      new User(15, "Dawson", "Augusta"),
      new User(25, "Ford", "Joseph", "Nicholas"),
      new User(18, "Lambert", "Edward")
    ).forEach(user -> Main.create(fileName, user));

    assertEquals(
        String.join("\n", List.of(
          "1,Dawson,Augusta,\"\",15",
          "2,Ford,Joseph,Nicholas,25",
          "3,Lambert,Edward,\"\",18"
        )),
        String.join("\n", Files.readAllLines(filePath))
    );
  }

  @Test
  void read_fileIsNotEmpty_userExists_shouldReturnUser() throws IOException {
    Files.write(filePath,
        List.of(
          "1,Murphy,Aileen,Deborah,38",
          "4,Norton,Robert,\"\",12"
        ),
        StandardCharsets.UTF_8
    );

    assertEquals(
        new User(12, "Norton", "Robert"),
        Main.read(fileName, 4)
    );
  }

  @Test
  void read_fileIsNotEmpty_userNotExists_shouldReturnNull() throws IOException {
    Files.write(filePath, List.of("1,Murphy,Aileen,Deborah,38"), StandardCharsets.UTF_8);
    assertNull(Main.read(fileName, 10));
  }

  @Test
  void read_fileIsEmpty_shouldReturnNull() {
    assertNull(Main.read(fileName, 10));
  }

  @Test
  void update_fileIsNotEmpty_userExists_shouldSucceed() throws IOException {
    Files.write(filePath,
        List.of(
          "1,Murphy,Aileen,Deborah,38",
          "4,Norton,Robert,\"\",12"
        ),
        StandardCharsets.UTF_8
    );

    Main.update(fileName, 4,
        new User(25, "Ford", "Joseph", "Nicholas"));

    assertEquals(
        String.join("\n", List.of(
          "1,Murphy,Aileen,Deborah,38",
          "4,Ford,Joseph,Nicholas,25"
        )),
        String.join("\n", Files.readAllLines(filePath))
    );
  }

  @Test
  void update_fileIsNotEmpty_userNotExists_shouldThrowNoSuchElementException() throws IOException {
    Files.write(filePath,
        List.of(
          "1,Murphy,Aileen,Deborah,38",
          "4,Norton,Robert,\"\",12"
        ),
        StandardCharsets.UTF_8
    );

    assertThrows(NoSuchElementException.class, () -> Main.update(fileName, 10,
        new User(12, "Norton", "Robert")));
  }

  @Test
  void update_fileIsEmpty_userNotExists_shouldThrowNoSuchElementException() {
    assertThrows(NoSuchElementException.class, () -> Main.update(fileName, 10,
        new User(12, "Norton", "Robert")));
  }

  @Test
  void delete_fileIsNotEmpty_userExists_shouldSucceed() throws IOException {
    Files.write(filePath,
        List.of(
          "1,Murphy,Aileen,Deborah,38",
          "4,Norton,Robert,\"\",12"
        ),
        StandardCharsets.UTF_8
    );

    Main.delete(fileName, 1);

    assertEquals(
        "4,Norton,Robert,\"\",12",
        String.join("\n", Files.readAllLines(filePath))
    );
  }

  @Test
  void delete_fileIsNotEmpty_userNotExists_shouldThrowNoSuchElementException() throws IOException {
    Files.write(filePath,
        List.of(
          "1,Murphy,Aileen,Deborah,38",
          "4,Norton,Robert,\"\",12"
        ),
        StandardCharsets.UTF_8
    );

    assertThrows(NoSuchElementException.class, () -> Main.delete(fileName, 10));
  }

  @Test
  void delete_fileIsEmpty_userNotExists_shouldThrowNoSuchElementException() {
    assertThrows(NoSuchElementException.class, () -> Main.delete(fileName, 10));
  }
}