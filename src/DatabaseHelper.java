import org.json.simple.*;
import org.json.simple.parser.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileReader;
import java.io.FileWriter;

public class DatabaseHelper {

    public static void initializeAdminAccount() {
        JSONObject users = loadDatabase("users.json");

        // Check if the "admin" account exists
        if (!users.containsKey("admin")) {
            System.out.println("Predefined admin account not found. Creating one...");
            JSONObject adminUser = new JSONObject();
            adminUser.put("id", "ADM-001");
            adminUser.put("password", "123456");
            adminUser.put("role", "admin");

            users.put("admin", adminUser);

            // Save the updated database
            saveDatabase("users.json", users);
            System.out.println("Predefined admin account created with username 'admin' and password '123456'.");
        }
    }

    public static JSONObject loadDatabase(String fileName) {
        try (FileReader reader = new FileReader("database/" + fileName)) {
            return (JSONObject) new JSONParser().parse(reader);
        } catch (Exception e) {
            return new JSONObject();
        }
    }


    public static void saveDatabase(String fileName, JSONObject database) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("database/" + fileName)) {
            writer.write(gson.toJson(database));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
