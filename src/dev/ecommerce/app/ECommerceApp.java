package dev.ecommerce.app;

import dev.ecommerce.concurrency.CheckoutTask;
import dev.ecommerce.model.Model;
import dev.ecommerce.payment.CreditCard;
import dev.ecommerce.payment.PayPal;
import dev.ecommerce.service.ProductManager;

import java.lang.ScopedValue;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ECommerceApp {

    void main() {
        ProductManager manager = new ProductManager();
        manager.loadProductsFromFile(Path.of("src/resources/products.csv"));

        manager.getProductsGroupedByCategory();
        manager.partitionExpiredFood();
        manager.getMostExpensiveItem();
        manager.getProductNameToPriceMap();
        manager.validateInventory();
        manager.generateAdvancedReport();
        manager.getInventoryInBatches();

        manager.displayLocalizedCatalog(Locale.US);

        Predicate<Model> expensiveProduct = model -> model.getPrice() > 20.0;
        Function<Model, String> productName = model -> model.getName().toUpperCase(Locale.ROOT);
        Consumer<Model> printProduct = model -> System.out.println(model.getName() + " -> " + model.getPrice());
        Supplier<LocalDateTime> currentTimestamp = LocalDateTime::now;

        manager.getInventory().stream().filter(expensiveProduct).map(productName).forEach(System.out::println);
        manager.getInventory().forEach(printProduct);
        System.out.println("Snapshot timestamp: " + currentTimestamp.get());

        List<CheckoutTask> tasks = List.of(
                new CheckoutTask(new CreditCard("1234567890123456", "12/29"), 58.49),
                new CheckoutTask(new PayPal("buyer@example.com"), 12.50),
                new CheckoutTask(new CreditCard("9999888877776666", "08/30"), 99.99));

        ScopedValue.where(CheckoutTask.CURRENT_USER, "Student_Jiyu_Li").run(() -> {
                try (var scope = StructuredTaskScope.<Boolean, Void>open(
                    StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow())) {
                List<StructuredTaskScope.Subtask<Boolean>> subtasks = tasks.stream()
                        .map(scope::fork)
                        .toList();
                scope.join();
                subtasks.forEach(StructuredTaskScope.Subtask::get);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Checkout workflow interrupted", e);
            } catch (Exception e) {
                throw new IllegalStateException("Checkout workflow failed", e);
            }
        });
    }
}
