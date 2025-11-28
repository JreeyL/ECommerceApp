package dev.ecommerce.app;

import dev.ecommerce.model.*;
import dev.ecommerce.payment.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ECommerceApp {

	public static void main(String[] args) {

		System.out.println("--- 1. Instantiation (using var and arrays) ---");

        // Create instances using 'var'
        var food = new Food("Organic Apples", 4.99, 1.0, "Ireland", Category.FOOD, 95, LocalDate.now().plusDays(7));
        
        var cookware = new Cookware("Frying Pan", 45.99, 2.5, "Germany", Category.COOKWARE, "Cast Iron");
        
        // Use the varargs addProduct method
        cookware.addProduct(new Product("Pan Lid", "Glass lid"), new Product("Spatula", "Silicone"));

        var tableware = new Tableware("Dinner Plate", 12.50, 0.8, "Portugal", Category.TABLEWARE, "Ceramic", "White");
        
        var food2 = new Food("Premium Coffee", 18.99, 0.5, "Colombia", Category.FOOD,10, LocalDate.now().plusYears(1));

        // Store all instances in an array
        Model[] productsArray = {food, cookware, tableware, food2};

        // Create a List from the array to demo Polymorphism
        List<Model> productList = new ArrayList<>(Arrays.asList(productsArray));
        
        System.out.println("Created " + productList.size() + " products.\n");


        // --- 2. Data Filtering (Lambda, Predicate & Effectively Final) ---
        System.out.println("--- 2. Filtering products with price < 20.0 ---");

        // Define a local variable (effectively final) 
        double maxPrice = 20.0;

        // Define a Predicate lambda expression
        Predicate<Model> filterByPrice = p -> p.getPrice() < maxPrice;

        // Use the Stream API and the predicate
        List<Model> filteredList = productList.stream()
                                              .filter(filterByPrice)
                                              .toList(); 


        // --- 3. List Processing (Method References & Discounts) ---
        System.out.println("--- 3. Processing filtered list and Showing Discounts ---");
        
        // Use a Method Reference to print each item
        filteredList.forEach(ECommerceApp::printProductDetails);
        System.out.println();


        // --- 4. Payment Processing (Switch Expressions & Pattern Matching) ---
        System.out.println("--- 4. Payment Processing (Switch Expression & Pattern Matching) ---");
        
        // Create payment method instances (Records)
        var card = new CreditCard("1234567890123456", "12/29");
        var paypal = new PayPal("user@example.com");

        // Process payments
        processPayment(card, 58.49);
        processPayment(paypal, 12.50);
    }

	private static void printProductDetails(Model product) {
		System.out.println(product); // to String
        System.out.printf("   >> Original: €%.2f | Discounted: €%.2f%n", 
                product.getPrice(), product.getDiscountedPrice());
	}
	
	
    private static void processPayment(Payment payment, double amount) {
        
        String confirmationMessage = switch (payment) {
            // Use Pattern Matching to deconstruct the record
            case CreditCard c -> "Processing card ending in " + c.cardNumber().substring(c.cardNumber().length() - 4);
            
            case PayPal p -> "Processing PayPal account " + p.email();
            
        };

        System.out.println(confirmationMessage);
        
        // Finally, call the polymorphic pay() method
        payment.pay(amount);
		
	}

}
