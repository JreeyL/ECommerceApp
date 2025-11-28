package dev.ecommerce.model;

import java.util.Objects;
import dev.ecommerce.contracts.Discountable;

public abstract class Model implements Discountable{
	
	private String name;
	private double price;
	private double weight;
	private String from;
	private Category category;
	
	public Model(String name, double price, double weight, String from, Category category) {
		
		if (price < 0) {
			throw new IllegalArgumentException("Price cannot be negative.");
		}
		
		if (weight < 0) {
			throw new IllegalArgumentException("Weight cannot be negative.");
		}
		
		this.name = Objects.requireNonNull(name, "Name cannot be null");
		this.price = price;
		this.weight = weight;
		this.from = Objects.requireNonNull(from, "From cannot be null");
		this.category = Objects.requireNonNull(category, "Category cannot be null");
	}
	
	@Override
    public abstract double applyDiscount(double price);
	
	public abstract double getDiscountedPrice();

	
	public String getName() {
		return name;
	}


	public double getPrice() {
		return price;
	}


	public double getWeight() {
		return weight;
	}


	public String getFrom() {
		return from;
	}


	public Category getCategory() {
		return category;
	}

	@Override
	public String toString() {
		return "Model{" + 
	           "name='" + name + '\'' +
	           ", price=" + price + 
	           ", weight=" + weight + 
	           ", from=" + from + 
	           ", category=" + category + 
	           '}';
	}

}
