import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Main {
  /**
   * Создаёт новую запись о пользователе {@code user}
   * в файле {@code filePath} формата CSV.
   *
   * @param filePath путь к файлу.
   * @param user     пользователь.
   */
  public static void create(@NotNull String filePath, @NotNull User user) {
    Map<Integer, String> usersInfo = new HashMap<>();
    int id = 1;
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String users;
      while ((users = br.readLine()) != null) {
        String[] userArr = users.split(",", 2);
        usersInfo.put(Integer.parseInt(userArr[0]), userArr[1]);
        if (id == Integer.parseInt(userArr[0])) {
          id++;
        }
      }
      usersInfo.put(id,
              user.secondName + "," + user.firstName + ","
                      + (user.middleName != null ? user.middleName : "\"\"") + "," + user.age);
    } catch (IOException e) {
      e.printStackTrace();
    }
    writer(filePath, id, usersInfo);
  }

  /**
   * Считывает из файла {@code filePath} формата CSV запись о пользователе
   * с идентификатором {@code id}. Возвращает {@code null}, если записи
   * с таким идентификатором не существует.
   *
   * @param filePath путь к файлу.
   * @param id       идентификатор записи о пользователе.
   * @return Пользователь или {@code null}.
   */
  public static @Nullable User read(@NotNull String filePath, int id) {
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String users;
      while ((users = br.readLine()) != null) {
        String[] usersArr = users.split(",");
        if (id == Integer.parseInt(usersArr[0])) {
          return new User(Integer.parseInt(usersArr[4]),
                  usersArr[1],
                  usersArr[2],
                  usersArr[3].equals("\"\"") ? null : usersArr[3]);
        }
      }
      return null;
    } catch (IOException fileNotFoundException) {
      fileNotFoundException.printStackTrace();
      return null;
    }
  }

  /**
   * Заменяет в файле {@code filePath} формата CSV информацию
   * в записи о пользователе с идентификатором {@code id}
   * на информацию пользователя {@code user}.
   *
   * @param filePath путь к файлу.
   * @param id       идентификатор записи о пользователе.
   * @param user     пользователь.
   * @throws java.util.NoSuchElementException запись о пользователе
   *                                          с идентификатором {@code id} не существует.
   */
  public static void update(@NotNull String filePath, int id, @NotNull User user) {
    Map<Integer, String> usersInfo;
    usersInfo = reader(filePath, id);
    usersInfo.put(id,
              user.secondName + "," + user.firstName + ","
                      + (user.middleName != null ? user.middleName : "\"\"") + "," + user.age);
    writer(filePath, id, usersInfo);
  }

  /**
   * Удаляет в файле {@code filePath} формата CSV запись
   * о пользователе с идентификатором {@code id}.
   *
   * @param filePath путь к файлу.
   * @param id       идентификатор записи о пользователе.
   * @throws java.util.NoSuchElementException запись о пользователе
   *                                          с идентификатором {@code id} не существует.
   */
  public static void delete(@NotNull String filePath, int id) {
    Map<Integer, String> usersInfo;
    usersInfo = reader(filePath, id);
    usersInfo.remove(id);
    writer(filePath, id, usersInfo);
  }

  /**
   * Создает коллекцию пользователей из файла {@code filePath} формата CSV
   * идентификатором {@code id}. Возвращает коллекцию {@code Map<Integer, String>} данных
   * о существующих пользователях.
   *
   * @param filePath путь к файлу.
   * @param id       идентификатор записи о пользователе.
   * @throws java.util.NoSuchElementException запись о пользователе
   *                                          с идентификатором {@code id} не существует.
   */
  public static Map<Integer, String> reader(String filePath, int id) {
    Map<Integer, String> usersInfo = new HashMap<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String users;
      while ((users = br.readLine()) != null) {
        String[] userArr = users.split(",", 2);
        usersInfo.put(Integer.parseInt(userArr[0]), userArr[1]);
      }
      if (!usersInfo.containsKey(id)) {
        throw new NoSuchElementException();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return usersInfo;
  }

  /**
   * Записывает в файл {@code filePath} формата CSV
   * нового пользователя с идентификатором {@code id}.
   *
   * @param filePath путь к файлу.
   * @param id       идентификатор записи о пользователе.
   * @param usersInfo       идентификатор записи о пользователе.
   */
  public static void writer(String filePath, int id, Map<Integer, String> usersInfo) {
    try (BufferedWriter wr = new BufferedWriter(new FileWriter(filePath))) {
      for (Map.Entry<Integer, String> item : usersInfo.entrySet()) {
        wr.write(item.getKey() + "," + item.getValue() + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}