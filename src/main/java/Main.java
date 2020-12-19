import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
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
   * @param user пользователь.
   */
  public static void create(@NotNull String filePath, @NotNull User user) {
    Map<Integer, String> usersInfo = new HashMap<Integer, String>();
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
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (BufferedWriter wr = new BufferedWriter(new FileWriter(filePath))) {
      for (Map.Entry<Integer, String> item : usersInfo.entrySet()) {
        wr.write(item.getKey() + "," + item.getValue() + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Считывает из файла {@code filePath} формата CSV запись о пользователе
   * с идентификатором {@code id}. Возвращает {@code null}, если записи
   * с таким идентификатором не существует.
   *
   * @param filePath путь к файлу.
   * @param id идентификатор записи о пользователе.
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
               (usersArr[3] == "\"\"" ? usersArr[3] : null));
        }
      }
      return null;
    } catch (FileNotFoundException fileNotFoundException) {
      fileNotFoundException.printStackTrace();
      return null;
    } catch (IOException ioException) {
      ioException.printStackTrace();
      return null;
    }
  }

  /**
   * Заменяет в файле {@code filePath} формата CSV информацию
   * в записи о пользователе с идентификатором {@code id}
   * на информацию пользователя {@code user}.
   *
   * @param filePath путь к файлу.
   * @param id идентификатор записи о пользователе.
   * @param user пользователь.
   * @throws java.util.NoSuchElementException запись о пользователе
   *     с идентификатором {@code id} не существует.
   */
  public static void update(@NotNull String filePath, int id, @NotNull User user) {
    Map<Integer, String> usersInfo = new HashMap<Integer, String>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String users;
      while ((users = br.readLine()) != null) {
        String[] userArr = users.split(",", 2);
        usersInfo.put(Integer.parseInt(userArr[0]), userArr[1]);
      }
      if (!usersInfo.containsKey(id)) {
        throw new NoSuchElementException();
      }
      usersInfo.put(id,
              user.secondName + "," + user.firstName + ","
                    + (user.middleName != null ? user.middleName : "\"\"") + "," + user.age);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    try (BufferedWriter wr = new BufferedWriter(new FileWriter(filePath))) {
      for (Map.Entry<Integer, String> item : usersInfo.entrySet()) {
        wr.write(item.getKey() + "," + item.getValue() + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Удаляет в файле {@code filePath} формата CSV запись
   * о пользователе с идентификатором {@code id}.
   *
   * @param filePath путь к файлу.
   * @param id идентификатор записи о пользователе.
   * @throws java.util.NoSuchElementException запись о пользователе
   *     с идентификатором {@code id} не существует.
   */

  public static void delete(@NotNull String filePath, int id) {
    Map<Integer, String> usersInfo = new HashMap<Integer, String>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String users;
      while ((users = br.readLine()) != null) {
        String[] userArr = users.split(",", 2);
        usersInfo.put(Integer.parseInt(userArr[0]), userArr[1]);
      }
      if (!usersInfo.containsKey(id)) {
        throw new NoSuchElementException();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    usersInfo.remove(id);
    try (BufferedWriter wr = new BufferedWriter(new FileWriter(filePath))) {
      for (Map.Entry<Integer, String> item : usersInfo.entrySet()) {
        wr.write(item.getKey() + "," + item.getValue() + "\n");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}