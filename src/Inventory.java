import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Product> inventoryList;

    public Inventory() {
        this.inventoryList = loadInventory();
    }

    private List<Product> loadInventory() {
        List<Product> inventory = new ArrayList<>();
        JSONArray inventoryData = (JSONArray) DatabaseHelper.loadDatabase("inventory.json").get("products");
        if (inventoryData != null) {
            for (Object obj : inventoryData) {
                JSONObject productData = (JSONObject) obj;
                inventory.add(new Product(
                    (String) productData.get("productId"),
                    (String) productData.get("productName"),
                    (double) productData.get("price"),
                    ((Long) productData.get("stock")).intValue()
                ));
            }
        }
        return inventory;
    }

    public void updateInventory(String productId, int quantity) {
        for (Product product : inventoryList) {
            if (product.getProductId().equals(productId)) {
                product.setStock(product.getStock() + quantity);
                saveInventory();
                return;
            }
        }
        System.out.println("Product not found.");
    }

    public Product getProduct(String productId) {
        for (Product product : inventoryList) {
            if (product.getProductId().equals(productId)) {
                return product;
            }
        }
        return null;
    }

    public List<Product> getLowStockProducts() { 
        List<Product> lowStock = new ArrayList<>(); 
        for (Product product : inventoryList) { 
            if (product.getStock() < 5) { 
                lowStock.add(product); 
            } 
        }
        return lowStock; 
    }
    

    private void saveInventory() {
        JSONArray inventoryData = new JSONArray();
        for (Product product : inventoryList) {
            JSONObject productData = new JSONObject();
            productData.put("productId", product.getProductId());
            productData.put("productName", product.getProductName());
            productData.put("price", product.getPrice());
            productData.put("stock", product.getStock());
            inventoryData.add(productData);
        }
        JSONObject inventoryJson = new JSONObject();
        inventoryJson.put("products", inventoryData);
        DatabaseHelper.saveDatabase("inventory.json", inventoryJson);
    }

    public List<Product> getInventoryList() { 
        return inventoryList;
    }

    public void addProduct(Product product, List<Product> products) {
        for (Product existingProduct : products) {
            if (existingProduct.getProductId().equals(product.getProductId())) {
                System.out.println("Product with this ID already exists.");
                return;
            }
        }
        products.add(product);
        System.out.println("Product added successfully: " + product.getProductInfo());
        saveInventory(); // Save after adding product
    }
    
    public void removeProduct(String productId, List<Product> products) {
        for (Product existingProduct : products) {
            if (existingProduct.getProductId().equals(productId)) {
                products.remove(existingProduct);
                saveInventory();
                System.out.println("Product removed successfully.");
                return;
            }
        }
        System.out.println("Product with this ID does not exist.");
    }
}    

 