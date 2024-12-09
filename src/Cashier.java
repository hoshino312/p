import java.util.List;
import java.util.Scanner;

public class Cashier extends Employee {
    public Cashier(String id, String username, String password, String name, String phone, boolean isWorking) {
        super(id, username, password, "cashier", name, phone, isWorking);
    }

    public void scanProducts(List<Product> cart, Inventory inventory) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter Product ID (or type 'done' to finish): ");
            String productId = scanner.nextLine();
    
            // Exit condition
            if (productId.equalsIgnoreCase("done")) {
                System.out.println("Scanning complete.");
                break;
            }
    
            System.out.print("Enter Quantity: ");
            int quantity;
            try {
                quantity = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity. Please enter a number.");
                continue;
            }
    
            // Fetch and add product to the cart
            Product product = inventory.getProduct(productId);
            if (product != null && product.getStock() >= quantity) {
                cart.add(new Product(productId, product.getProductName(), product.getPrice(), quantity));
                System.out.println("Scanned: " + product.getProductInfo() + " | Quantity: " + quantity);
            } else {
                System.out.println("Product not found or insufficient stock.");
            }
        }
    }
    

    public boolean processPayment(String paymentMethod, double totalAmount) {
        System.out.println("Payment Method: " + paymentMethod);
        System.out.println("Amount Due: $" + totalAmount);
        Scanner scanner = new Scanner(System.in);
        System.out.print("Is payment successful? (yes/no): ");
        return scanner.nextLine().equalsIgnoreCase("yes");
    }
}