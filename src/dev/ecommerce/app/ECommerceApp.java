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

    private void printSection(String title) {
        System.out.println("\n[DEMO] " + title);
    }

    void main() {

        ProductManager manager = new ProductManager();
        manager.loadProductsFromFile(Path.of("src/resources/products.csv"));

        System.out.println("========================================");
        System.out.println("E-Commerce Backend Demo (OOP#2)");
        System.out.println("========================================");

        printSection("Localization (Locale.US)");
        manager.displayLocalizedCatalog(Locale.US);

        printSection("Streams: grouping by category");
        manager.getProductsGroupedByCategory()
                .forEach((category, products) ->
                        System.out.println("- " + category + ": " + products.size() + " items"));

        printSection("Streams: partition expired food");
        manager.partitionExpiredFood()
                .forEach((expired, products) ->
                        System.out.println("- expired=" + expired + ": " + products.size() + " items"));

        printSection("Streams: max by Comparator.comparing");
        Model expensive = manager.getMostExpensiveItem();
        System.out.println("- most expensive: " + expensive.getName() + " -> " + expensive.getPrice());

        printSection("Streams: name-price map");
        manager.getProductNameToPriceMap()
                .forEach((name, price) -> System.out.println("- " + name + " -> " + price));

        printSection("Streams: allMatch/anyMatch/noneMatch");
        System.out.println("- inventory valid: " + manager.validateInventory());

        printSection("Streams: distinct/sorted/count report");
        System.out.println("- unique origins: " + manager.generateAdvancedReport());

        printSection("Gatherers.windowFixed(2)");
        List<List<Model>> batches = manager.getInventoryInBatches();
        System.out.println("- batches: " + batches.size());
        for (int i = 0; i < batches.size(); i++) {
            List<String> namesInBatch = batches.get(i).stream().map(Model::getName).toList();
            System.out.println("    * Page " + (i + 1) + ": " + namesInBatch);
        }

        // Core functional rules for product filtering and reporting.
        Predicate<Model> expensiveProduct = model -> model.getPrice() > 20.0;
        Function<Model, String> upperCaseName = model -> model.getName().toUpperCase(Locale.ROOT);
        Consumer<Model> printProduct = model -> System.out.println("- " + model.getName() + " -> " + model.getPrice());
        Supplier<LocalDateTime> currentTimestamp = LocalDateTime::now;

        printSection("Functional interfaces: Predicate/Function/Consumer/Supplier");
        manager.getInventory().stream()
                .filter(expensiveProduct)
                .map(upperCaseName)
                .forEach(name -> System.out.println("- filtered upper name: " + name));
        manager.getInventory().forEach(printProduct);
        System.out.println("- snapshot timestamp: " + currentTimestamp.get());

        printSection("StructuredTaskScope + ScopedValue");
        List<CheckoutTask> tasks = List.of(
                new CheckoutTask(new CreditCard("1234567890123456", "12/29"), 58.49),
                new CheckoutTask(new PayPal("buyer@example.com"), 12.50),
                new CheckoutTask(new CreditCard("9999888877776666", "08/30"), 99.99));

        // ScopedValue is bound once, then inherited by forked subtasks.
        ScopedValue.where(CheckoutTask.CURRENT_USER, "Jiyu_Li").run(() -> {
            try (var scope = StructuredTaskScope.<Boolean, Void>open(
                    StructuredTaskScope.Joiner.awaitAllSuccessfulOrThrow())) {
                List<StructuredTaskScope.Subtask<Boolean>> subtasks = tasks.stream()
                        .map(scope::fork)
                        .toList();
                scope.join();
                subtasks.forEach(StructuredTaskScope.Subtask::get);
                System.out.println("- checkout tasks completed successfully");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Checkout workflow interrupted", e);
            } catch (Exception e) {
                throw new IllegalStateException("Checkout workflow failed", e);
            }
        });

        System.out.println("\nDemo complete.");
    }
}
