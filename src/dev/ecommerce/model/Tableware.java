package dev.ecommerce.model;

public class Tableware extends Model {
	
	private String material;
	private String color;
	
	public Tableware(String name, double price, double weight, String from, Category category, String material, String color) {
		super(name, price, weight, from, category);
		this.material = material;
		this.color = color;
	}

	@Override
	public double getDiscountedPrice() {
		return applyDiscount(super.getPrice());
	}
	
	@Override
	public double applyDiscount(double price) {
		return price * 0.95;
	}

	public String getMaterial() {
		return material;
	}

	public String getColor() {
		return color;
	}

	@Override
	public String toString() {
		String parentDetails = super.toString();
        StringBuilder sb = new StringBuilder(parentDetails);

        sb.deleteCharAt(sb.length() - 1); 
        sb.append(", material=");
        sb.append(this.material);
        sb.append(", color=");
        sb.append(this.color);
        sb.append('}'); 

        return sb.toString();
	}

}
