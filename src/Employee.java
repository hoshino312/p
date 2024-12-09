import org.json.simple.JSONObject;

public abstract class Employee extends Account {
    protected String name;
    protected String phone;
    protected boolean isWorking;

    public Employee(String id, String username, String password, String role, String name, String phone, boolean isWorking) {
        super(id, username, password, role);
        this.name = name;
        this.phone = phone;
        this.isWorking = isWorking;
    }

    public void viewPersonalInformation() {
        // View personal information
        System.out.println("\n--- Personal Information ---");
        System.out.println("Name: " + name);
        System.out.println("Phone: " + phone);
        System.out.println("ID: " + id);
        System.out.println("Role: " + role);
        System.out.println("Status: " + (isWorking ? "Working" : "Not Working"));
    }

    public void editPersonalInformation(String newName, String newPhone) {
        this.name = newName;
        this.phone = newPhone;

        JSONObject users = DatabaseHelper.loadDatabase("users.json");
        JSONObject user = (JSONObject) users.get(username);
        if (user != null) {
            user.put("name", newName);
            user.put("phone", newPhone);
            DatabaseHelper.saveDatabase("users.json", users);
        }
    }
}


