import org.json.simple.JSONObject;

public abstract class Account {
    protected String id;
    protected String username;
    protected String password;
    protected String role;

    public Account(String id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public boolean login(String username, String password) {
        JSONObject users = DatabaseHelper.loadDatabase("users.json");
        JSONObject user = (JSONObject) users.get(username);

        if (user != null && user.get("password").equals(password)) {
            this.username = username;
            this.role = (String) user.get("role");
            return true;
        }
        return false;
    }

    public void changePassword(String newPassword) {
        JSONObject users = DatabaseHelper.loadDatabase("users.json");
        JSONObject user = (JSONObject) users.get(username);
        if (user != null) {
            user.put("password", newPassword);
            DatabaseHelper.saveDatabase("users.json", users);
        }
    }
}
