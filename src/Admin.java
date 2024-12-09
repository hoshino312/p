import org.json.simple.JSONObject;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Admin extends Account {

    // Set to store valid roles, dynamically extendable
    private static final Set<String> validRoles = new HashSet<>(Set.of("manager", "cashier"));

    public Admin(String id, String username, String password) {
        super(id, username, password, "admin");
    }

    // Create a new account
    public void createAccount(String role) {
        if (!isValidRole(role)) {
            System.out.println("Error: Invalid role '" + role + "'. Valid roles are: " + validRoles);
            return;
        }

        // Load existing users
        JSONObject users = DatabaseHelper.loadDatabase("users.json");

        // Generate a unique ID, username, and password
        String id = generateUniqueId(users);
        String username = generateUniqueUsername(users, id);
        String password = generatePassword();

        // Create new user JSON object
        JSONObject newUser = new JSONObject();
        newUser.put("id", id);
        newUser.put("password", password);
        newUser.put("role", role.toLowerCase());
        newUser.put("isWorking", true);

        // Add user to the database
        users.put(username, newUser);
        DatabaseHelper.saveDatabase("users.json", users);

        // Display account details
        System.out.println("Account created successfully!");
        System.out.println("ID: " + id);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Role: " + role);
    }

    public void editEmployeeInfo(String id, String newPassword, String newRole, Boolean newStatus) {
        JSONObject users = DatabaseHelper.loadDatabase("users.json");
        for (Object key : users.keySet()) {
            JSONObject user = (JSONObject) users.get(key);
            if (user.get("id").equals(id)) {
                // Update password if provided
                if (newPassword != null && !newPassword.isEmpty()) {
                    user.put("password", newPassword);
                }

                // Update role if provided and valid
                if (newRole != null && isValidRole(newRole)) {
                    user.put("role", newRole.toLowerCase());
                } else if (newRole != null) {
                    System.out.println("Error: Invalid role '" + newRole + "'. Valid roles are: " + validRoles);
                    return;
                }

                // Update working status if provided
                if (newStatus != null) {
                    user.put("isWorking", newStatus);
                }

                DatabaseHelper.saveDatabase("users.json", users);
                System.out.println("Employee information updated successfully for user ID: " + id);
                return;
            }
        }
        System.out.println("Error: User with ID " + id + " not found.");
    }

    public void adjustWorkingStatus(String id, boolean newStatus) {
        editEmployeeInfo(id, null, null, newStatus);
    }

    public void assignRole(String id, String newRole) {
        editEmployeeInfo(id, null, newRole, null);
    }
    
    public void deleteAccount(String id) {
        JSONObject users = DatabaseHelper.loadDatabase("users.json");
        boolean userFound = users.keySet().removeIf(key -> {
            JSONObject user = (JSONObject) users.get(key);
            return user.get("id").equals(id);
        });

        if (userFound) {
            DatabaseHelper.saveDatabase("users.json", users);
            System.out.println("Account deleted successfully for user ID: " + id);
        } else {
            System.out.println("Error: User with ID " + id + " not found.");
        }
    }

    // Method to check if a role is valid
    private boolean isValidRole(String role) {
        return validRoles.contains(role.toLowerCase());
    }

    // Helper to generate a unique ID
    private String generateUniqueId(JSONObject users) {
        int maxSuffix = 0;

        for (Object key : users.keySet()) {
            JSONObject user = (JSONObject) users.get(key);
            String userId = (String) user.get("id");
            if (userId.startsWith("U")) {
                int suffix = Integer.parseInt(userId.substring(1));
                maxSuffix = Math.max(maxSuffix, suffix);
            }
        }

        return "U" + String.format("%03d", maxSuffix + 1); // e.g., U001
    }

    // Helper to generate a unique username
    private String generateUniqueUsername(JSONObject users, String id) {
        String baseUsername = "user" + id;
        String username = baseUsername;
        int counter = 1;

        while (users.containsKey(username)) {
            username = baseUsername + counter;
            counter++;
        }

        return username;
    }

    // Helper to generate a password
    private String generatePassword() {
        String charPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        int passwordLength = 6;
        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < passwordLength; i++) {
            password.append(charPool.charAt(random.nextInt(charPool.length())));
        }

        return password.toString();
    }
}
