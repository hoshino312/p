import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Bill {
    private String billId;
    private String employeeId;
    private List<Product> productList;
    private double totalAmount;
    private String date;

    public Bill(String billId, String employeeId, List<Product> productList) {
        this.billId = billId;
        this.employeeId = employeeId;
        this.productList = productList;
        this.totalAmount = calculateTotal();
        this.date = getCurrentDate(); // Group bills by date
    }

    private double calculateTotal() {
        double total = 0;
        for (Product product : productList) {
            total += product.getPrice() * product.getStock();
        }
        return total;
    }

    private String getCurrentDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date());
    }

    public void generateBill() {
        JSONObject billData = new JSONObject();
        billData.put("billId", billId);
        billData.put("employeeId", employeeId);
    
        JSONArray productsData = new JSONArray();
        for (Product product : productList) {
            JSONObject productData = new JSONObject();
            productData.put("productId", product.getProductId());
            productData.put("productName", product.getProductName());
            productData.put("quantity", product.getStock());
            productData.put("price", product.getPrice());
            productsData.add(productData);
        }
    
        billData.put("products", productsData);
        billData.put("totalAmount", totalAmount);
    
        // Load database
        JSONObject database = DatabaseHelper.loadDatabase("bills.json");
        if (database == null) {
            database = new JSONObject();
        }
    
        // Ensure "bills" is a JSONObject
        Object billsObject = database.get("bills");
        JSONObject billsByDate;
        if (billsObject instanceof JSONObject) {
            billsByDate = (JSONObject) billsObject;
        } else {
            billsByDate = new JSONObject(); // Initialize if it's not a JSONObject
        }
    
        // Ensure daily bills exist for the current date
        JSONArray dailyBills = (JSONArray) billsByDate.get(date);
        if (dailyBills == null) {
            dailyBills = new JSONArray();
        }
    

        dailyBills.add(billData);
    
        billsByDate.put(date, dailyBills);
        database.put("bills", billsByDate);
    

        try {
            DatabaseHelper.saveDatabase("bills.json", database);
            System.out.println("Bill generated successfully! Total: $" + totalAmount);
            printBill();
        } catch (Exception e) {
            System.out.println("Failed to save bill: " + e.getMessage());
            e.printStackTrace();
        }
    }
    


    public void printBill() {

        System.out.println("Bill ID: " + billId); 
        System.out.println("Employee ID: " + employeeId); 
        System.out.println("=".repeat(60)); 

        // Table header
        System.out.printf("%-20s %-15s %-10s %-15s%n",
                "Product Name", "Price", "Quantity", "Total");
        System.out.println("-".repeat(60)); 

        int totalQuantity = 0; // Track total quantity sold

        for (Product product : productList) {
            totalQuantity += product.getStock(); // Add product quantity to total

            // Print product details in a table row
            System.out.printf("%-20s $%-14.2f %-10d $%-14.2f%n",
                    product.getProductName(),
                    product.getPrice(),
                    product.getStock(),
                    product.getPrice() * product.getStock());
        }

        System.out.println("=".repeat(60)); 
        System.out.printf("%-20s %-15s %-10d $%-15s%n",
                "TOTAL", "", totalQuantity, totalAmount);
        System.out.println("=".repeat(60));
    }

}


