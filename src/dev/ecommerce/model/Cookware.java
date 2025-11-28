package dev.ecommerce.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Cookware extends Model {
	
	private String material;
	private List<Product> items = new ArrayList<Product>();
	
	public Cookware(String name, double price, double weight, String from, Category category, String material) {
		super(name, price, weight, from, category);
		this.material = material;
	}
	
	public void addProduct(Product...products) {
		this.items.addAll(Arrays.asList(products));
	}
	
	public List<Product> getIteams() {
		return new ArrayList<>(this.items);
	}
	
	public double getTotal() {
		return 0.0;
	}
	
	@Override
	public double getDiscountedPrice() {
		return applyDiscount(super.getPrice());
	}
	
	@Override
	public double applyDiscount(double price) {
		return price * 0.85;
	}

	public String getMaterial() {
		return material;
	}

	@Override
	public String toString() {
		String parentDetails = super.toString();
        StringBuilder sb = new StringBuilder(parentDetails);
        
        sb.deleteCharAt(sb.length() - 1); 
        sb.append(", material=");
        sb.append(this.material);
        sb.append(", items=");
        sb.append(this.items); 
        sb.append('}'); 
        
        return sb.toString();
	}

}
