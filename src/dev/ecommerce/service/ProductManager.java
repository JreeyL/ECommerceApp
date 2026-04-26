package dev.ecommerce.service;

import dev.ecommerce.model.Category;
import dev.ecommerce.model.Cookware;
import dev.ecommerce.model.Food;
import dev.ecommerce.model.Model;
import dev.ecommerce.model.Tableware;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Gatherers;
import java.text.NumberFormat;

public class ProductManager {

    private List<Model> inventory = new ArrayList<>();

    public void loadProductsFromFile(Path path) {
        try (var lines = Files.lines(path)) {
            inventory = lines
                    .filter(line -> !line.isBlank())
                    .map(this::parseLineSafely)
                    .filter(Objects::nonNull)
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load products file: " + path, e);
        }
    }

    public Map<Category, List<Model>> getProductsGroupedByCategory() {
        return inventory.stream().collect(Collectors.groupingBy(Model::getCategory));
    }

    public Map<Boolean, List<Food>> partitionExpiredFood() {
        return inventory.stream()
                .filter(model -> model instanceof Food)
                .map(model -> (Food) model)
                .collect(Collectors.partitioningBy(f -> f.getBestBefore().isBefore(LocalDate.now())));
    }

    public Model getMostExpensiveItem() {
        return inventory.stream()
                .max(Comparator.comparing(Model::getPrice))
                .orElseThrow(() -> new IllegalStateException("Inventory is empty"));
    }

    public Map<String, Double> getProductNameToPriceMap() {
        return inventory.stream().collect(Collectors.toMap(Model::getName, Model::getPrice, (price1, price2) -> price1));
    }

    public boolean validateInventory() {
        boolean allPositivePrice = inventory.stream().allMatch(model -> model.getPrice() > 0);
        boolean anyFromIreland = inventory.stream().anyMatch(model -> "Ireland".equalsIgnoreCase(model.getFrom()));
        boolean noneNegativeWeight = inventory.stream().noneMatch(model -> model.getWeight() < 0);
        return allPositivePrice && anyFromIreland && noneNegativeWeight;
    }

    public long generateAdvancedReport() {
        return inventory.stream()
                .map(Model::getFrom)
                .distinct()
                .sorted()
                .count();
    }

    public List<List<Model>> getInventoryInBatches() {
        return inventory.stream().gather(Gatherers.windowFixed(2)).toList();
    }

    public void displayLocalizedCatalog(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("resources.messages", locale);
        NumberFormat currency = NumberFormat.getCurrencyInstance(locale);

        System.out.println("\n" + bundle.getString("catalog.header"));
        for (Model model : inventory) {
            String line = MessageFormat.format(
                    bundle.getString("catalog.item"),
                    model.getName(),
                    currency.format(model.getPrice()),
                    model.getCategory());
            System.out.println(line);
        }
    }

    public List<Model> getInventory() {
        return inventory;
    }

    private Model parseLine(String line) {
        String[] parts = line.split(",");
        Category category = Category.valueOf(parts[0].trim().toUpperCase(Locale.ROOT));

        return switch (category) {
            case FOOD -> new Food(
                    parts[1].trim(),
                    Double.parseDouble(parts[2].trim()),
                    Double.parseDouble(parts[3].trim()),
                    parts[4].trim(),
                    category,
                    Double.parseDouble(parts[5].trim()),
                    LocalDate.parse(parts[6].trim()));
            case COOKWARE -> new Cookware(
                    parts[1].trim(),
                    Double.parseDouble(parts[2].trim()),
                    Double.parseDouble(parts[3].trim()),
                    parts[4].trim(),
                    category,
                    parts[5].trim());
            case TABLEWARE -> new Tableware(
                    parts[1].trim(),
                    Double.parseDouble(parts[2].trim()),
                    Double.parseDouble(parts[3].trim()),
                    parts[4].trim(),
                    category,
                    parts[5].trim(),
                    parts[6].trim());
        };
    }

    private Model parseLineSafely(String line) {
        try {
            return parseLine(line);
        } catch (RuntimeException e) {
            System.err.println("Warning: skipping malformed product row: " + line);
            return null;
        }
    }
}
