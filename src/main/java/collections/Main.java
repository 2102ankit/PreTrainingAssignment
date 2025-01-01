package collections;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

enum SortBy {
    ID, NAME, PRICE, QUANTITY;
}

class Product {
    public int id;
    public String name;
    public double price;
    public int quantity;

    public Product(int id, String name, double price, int quantity) {
        if(id<0) throw new IllegalArgumentException("ID cannot be negative");
        if(price<0) throw new IllegalArgumentException("Price cannot be negative");
        if(quantity<0) throw new IllegalArgumentException("Quantity cannot be negative");

        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if(price<0) throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        if(quantity<0) throw new IllegalArgumentException("Quantity cannot be negative");
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product ID: " + id + ", Name: " + name + ", Price: $" + price + ", Quantity: " + quantity;
    }
}

class Inventory {
    static final Logger logger = LogManager.getLogger();
    // Using a HashMap to store products by their ID
    private Map<Integer, Product> inventory = new HashMap<>();

    public void addProduct(Product product) {
        //product already exists then increment product count by 1;
        if (inventory.containsKey(product.getId())) {
            Product existingProduct = inventory.get(product.getId());
            existingProduct.setQuantity(existingProduct.getQuantity() + 1);
        } else {
            inventory.put(product.getId(), product);
        }
    }

    public void removeProduct(Product product) {

        if (inventory.containsKey(product.getId())) {
            Product existingProduct = inventory.get(product.getId());
            if (existingProduct.getQuantity() > 1) existingProduct.setQuantity(existingProduct.getQuantity() - 1);
            else {
                inventory.remove(product.getId());
            }
        } else {
            logger.warn("Product not found");
        }
    }

    public void deleteProduct(Product product) {
        if (inventory.containsKey(product.getId())) {
            inventory.remove(product.getId());
        } else {
            logger.warn("Product not found");
        }
    }

    public void updateProduct(Product product, String newName, double newPrice, int newQuantity) {
        if (inventory.containsKey(product.getId())) {
            Product existingProduct = inventory.get(product.getId());
            existingProduct.setName(newName);
            existingProduct.setPrice(newPrice);
            existingProduct.setQuantity(newQuantity);
        } else {
            logger.warn("Product not found");
        }
    }

    public void displayAllProducts() {
        List<Product> productList = new ArrayList<>(inventory.values());
        productList.sort(Comparator.comparing(Product::getId));
        for (Product product : productList) {
            logger.debug(product);
        }
    }

    public void displayAllProducts(SortBy sortedBy) {
        List<Product> productList = new ArrayList<>(inventory.values());
        switch (sortedBy) {
            case NAME:
                productList.sort(Comparator.comparing(Product::getName));
                break;
            case PRICE:
                productList.sort(Comparator.comparing(Product::getPrice));
                break;
            case QUANTITY:
                productList.sort(Comparator.comparing(Product::getQuantity));
                break;
            default:
                productList.sort(Comparator.comparing(Product::getId));
                break;
        }
        for (Product product : productList) {
            logger.debug(product);
        }
    }

    public void findProductByName(String name) {
        for (Product product : inventory.values()) {
            if (product.getName().equalsIgnoreCase(name)) {
                logger.debug(product);
            }
        }
    }

    // Check if product exists by ID
    public boolean checkProductExists(int id) {
        return inventory.containsKey(id);
    }

    public void listAllProductNames() {
        Set<String> productNames = new TreeSet<>();

        for (Product product : inventory.values()) {
            productNames.add(product.getName());
        }

        for (String name : productNames) {
            logger.debug(name);
        }
    }

    // Restock a product (add quantity)
    public void restockProduct(Product product, int additionalQuantity) {
        if (inventory.containsKey(product.getId())) {
            Product existingProduct = inventory.get(product.getId());
            existingProduct.setQuantity(existingProduct.getQuantity() + additionalQuantity);
        } else {
            logger.warn("Product Not Found");
        }
    }

}


public class Main {
    static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        // Create Inventory object
        Inventory inventory = new Inventory();

        // Create some Product objects
        Product product1 = new Product(1, "Laptop", 1500.00, 10);
        Product product2 = new Product(2, "Smartphone", 800.00, 20);
        Product product3 = new Product(3, "Headphones", 200.00, 15);

        try {
            // Add products to inventory
            inventory.addProduct(product1);
            inventory.addProduct(product2);
            inventory.addProduct(product3);
            inventory.addProduct(new Product(1, "Laptop", 1500.00, 5)); // Duplicate ID, increments quantity

            logger.info("Initial Inventory:");
            inventory.displayAllProducts();

            // Find a product by name
            logger.info("Finding product by name 'Smartphone':");
            inventory.findProductByName("Smartphone");

            // Sort and display products by price
            logger.info("Inventory sorted by price:");
            inventory.displayAllProducts(SortBy.PRICE);

            // Remove a product
            logger.info("Removing one unit of 'Laptop':");
            inventory.removeProduct(product1);
            inventory.displayAllProducts();

            // Delete a product completely
            logger.info("Deleting 'Headphones' from inventory:");
            inventory.deleteProduct(product3);
            inventory.displayAllProducts();

            // Update product details
            logger.info("Updating 'Smartphone' details:");
            inventory.updateProduct(product2, "Smartphone Pro", 1000.00, 25);
            inventory.displayAllProducts();

            // Check if a product exists
            logger.info("Checking if product with ID 3 exists:");
            boolean exists = inventory.checkProductExists(3);
            logger.info("Product exists: " + exists);

            // Restock a product
            logger.info("Restocking 'Laptop' with 5 units:");
            inventory.restockProduct(product1, 5);
            inventory.displayAllProducts();

            // List all product names
            logger.info("Listing all product names:");
            inventory.listAllProductNames();

        } catch (Exception e) {
            logger.error("An error occurred during inventory operations: " + e.getMessage(), e);
        }

        // Introducing invalid cases
        logger.info("Attempting to add a product with negative price:");
        try {
            Product invalidProduct1 = new Product(4, "InvalidProduct1", -500.00, 5);
            inventory.addProduct(invalidProduct1);
        } catch (IllegalArgumentException e) {
            logger.error("Error adding product: " + e.getMessage(), e);
        }

        logger.info("Attempting to add a product with negative quantity:");
        try {
            Product invalidProduct2 = new Product(5, "InvalidProduct2", 100.00, -5);
            inventory.addProduct(invalidProduct2);
        } catch (IllegalArgumentException e) {
            logger.error("Error adding product: " + e.getMessage(), e);
        }

        logger.info("Attempting to update product with negative price:");
        try {
            inventory.updateProduct(product1, "Laptop Pro", -1200.00, 15);
        } catch (IllegalArgumentException e) {
            logger.error("Error updating product: " + e.getMessage(), e);
        }

        logger.info("Attempting to restock with negative quantity:");
        try {
            inventory.restockProduct(product2, -10);
        } catch (IllegalArgumentException e) {
            logger.error("Error restocking product: " + e.getMessage(), e);
        }

        // Final state of inventory
        logger.info("Final Inventory State:");
        inventory.displayAllProducts();
    }
}