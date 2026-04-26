package dev.ecommerce.model;

import java.time.LocalDate;

public class Food extends Model {
	
	private double calories;
	private java.time.LocalDate bestBefore;

	public Food(String name, double price, double weight, String from, Category category, double calories, LocalDate bestBefore) {
		if (calories < 0) {
			throw new IllegalArgumentException("Calories cannot be negative");
		}
		if (bestBefore == null) {
			throw new IllegalArgumentException("Best before date cannot be null");
		}
		super(name, price, weight, from, category);
		this.calories = calories;
		this.bestBefore = bestBefore;
	}
	
	@Override
	public double getDiscountedPrice() {
		double originalPrice = super.getPrice();
		return applyDiscount(originalPrice);
	}
	
	@Override
	public double applyDiscount(double price) {
		return price * 0.90;
	}

	
	public double getDiscountedPrice(double customRate) {
		if (customRate < 0 || customRate > 1) {
			throw new IllegalArgumentException("Custom rate must be between 0.0 and 1.0");
		}
		return super.getPrice() * (1.0 - customRate);
	}

	public double getCalories() {
		return calories;
	}

	public LocalDate getBestBefore() {
		return bestBefore;
	}

	@Override
	public String toString() {
        String parentDetails = super.toString(); 
        StringBuilder sb = new StringBuilder(parentDetails);
        sb.deleteCharAt(sb.length() - 1); 
        
        sb.append(", calories=");
        sb.append(this.calories);
        sb.append(", bestBefore=");
        sb.append(this.bestBefore);
        sb.append('}'); 
        
        return sb.toString();
	}
	
}
